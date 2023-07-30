package fr.sdecout.sandbox.persistence.jpa.author

import fr.sdecout.sandbox.rest.author.*
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Profile
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
@Profile("jpa")
@Transactional
class JpaAuthorAdapter(
    private val authorRepository: AuthorRepository,
) : AuthorAccess, AuthorSearch, AuthorCreation {

    override fun findAuthor(id: AuthorId): AuthorResponse? = authorRepository.findByIdOrNull(id.value)
        ?.toAuthorResponse()

    override fun searchAuthors(hint: String): List<AuthorSearchResponseItem> = authorRepository.findByLastNameLikeIgnoringCase("%$hint%")
        .map { it.toAuthorSearchResponseItem() }

    override fun addAuthor(lastName: String, firstName: String): AuthorId = newAuthorEntity(lastName, firstName)
        .let{ authorRepository.save(it) }
        .let{ AuthorId(it.id) }

    private fun newAuthorEntity(lastName: String, firstName: String) = AuthorEntity(
        id = AuthorId.next().value,
        lastName = lastName,
        firstName = firstName,
        publishedBooks = mutableListOf(),
    )

}
