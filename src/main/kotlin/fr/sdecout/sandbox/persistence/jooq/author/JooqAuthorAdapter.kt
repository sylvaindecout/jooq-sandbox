package fr.sdecout.sandbox.persistence.jooq.author

import fr.sdecout.sandbox.persistence.jooq.shared.authorBooks
import fr.sdecout.sandbox.persistence.jooq.tables.references.*
import fr.sdecout.sandbox.rest.author.*
import fr.sdecout.sandbox.rest.book.*
import org.jooq.DSLContext
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("jooq")
class JooqAuthorAdapter(
    private val dsl: DSLContext,
) : AuthorAccess, AuthorSearch, AuthorCreation {

    override fun findAuthor(id: AuthorId): AuthorResponse? = dsl
        .select(AUTHOR.ID, AUTHOR.LAST_NAME, AUTHOR.FIRST_NAME, authorBooks)
        .from(AUTHOR)
        .where(AUTHOR.ID.eq(id))
        .fetchOne { it.toAuthorResponse() }

    override fun addAuthor(lastName: String, firstName: String): AuthorId = AuthorId.next().also {
        dsl.insertInto(AUTHOR)
            .set(newAuthorRecord(it, lastName, firstName))
            .execute()
    }

    override fun searchAuthors(hint: String): List<AuthorSearchResponseItem> = dsl
        .select(AUTHOR.ID, AUTHOR.LAST_NAME, AUTHOR.FIRST_NAME)
        .from(AUTHOR)
        .where(AUTHOR.LAST_NAME.likeIgnoreCase("%$hint%"))
        .fetch { it.toAuthorSearchResponseItem() }

    private fun newAuthorRecord(id: AuthorId, lastName: String, firstName: String) = AUTHOR.newRecord()
        .with(AUTHOR.ID, id)
        .with(AUTHOR.LAST_NAME, lastName)
        .with(AUTHOR.FIRST_NAME, firstName)

}
