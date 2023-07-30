package fr.sdecout.sandbox.persistence.jpa.book

import fr.sdecout.sandbox.persistence.jpa.author.AuthorRepository
import fr.sdecout.sandbox.rest.author.AuthorId
import fr.sdecout.sandbox.rest.book.*
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Profile
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
@Profile("jpa")
@Transactional
class JpaBookAdapter(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
) : BookAccess, BookSearch, BookUpdate {

    override fun findBook(isbn: Isbn): BookResponse? = bookRepository.findByIdOrNull(isbn.compressedValue)
        ?.toBookResponse()

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
