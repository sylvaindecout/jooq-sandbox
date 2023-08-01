package fr.sdecout.sandbox.persistence.jpa.book

import fr.sdecout.sandbox.persistence.jpa.author.AuthorRepository
import fr.sdecout.sandbox.persistence.jpa.book.JpaBookAdapter.Mode.*
import fr.sdecout.sandbox.rest.author.AuthorId
import fr.sdecout.sandbox.rest.book.*
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
@Profile("jpa")
@Transactional
class JpaBookAdapter(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
    private val entityManager: EntityManager,
    @Value("\${mode:SPRING_DATA}") private val mode: Mode,
) : BookAccess, BookSearch, BookUpdate {

    enum class Mode { BASIC, JPQL, CRITERIA_QUERY, SPRING_DATA }

    override fun findBook(isbn: Isbn): BookResponse? = when (mode) {
        BASIC -> entityManager.find(BookEntity::class.java, isbn.compressedValue)
        JPQL -> entityManager
            .createQuery("FROM BookEntity b WHERE b.isbn = :isbn", BookEntity::class.java)
            .setParameter("isbn", isbn.compressedValue)
            .resultList.firstOrNull()
        CRITERIA_QUERY -> entityManager.criteriaBuilder
            .createQuery(BookEntity::class.java)
            .also {
                it.from(BookEntity::class.java).let { book ->
                    it.select(book)
                        .where(entityManager.criteriaBuilder.equal(book.get<String>("isbn"), isbn.compressedValue))
                }
            }.let {
                entityManager
                    .createQuery(it)
                    .resultList.firstOrNull()
            }
        SPRING_DATA -> bookRepository.findByIdOrNull(isbn.compressedValue)
    }?.toBookResponse()

    override fun searchBooks(hint: String): List<BookSearchResponseItem> = bookRepository.findByTitleLikeIgnoringCase("%$hint%")
        .map { it.toBookSearchResponse() }

    override fun save(isbn: Isbn, title: String, authors: Collection<AuthorId>) {
        newBookEntity(isbn, title, authors).let { bookRepository.save(it) }
    }

    private fun newBookEntity(isbn: Isbn, title: String, authors: Collection<AuthorId>) = BookEntity(
        isbn = isbn.compressedValue,
        title = title,
        authors = authors.map { authorRepository.getReferenceById(it.value) }.toMutableList(),
    )

}
