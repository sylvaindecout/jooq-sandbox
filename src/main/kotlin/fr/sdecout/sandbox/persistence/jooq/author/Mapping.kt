package fr.sdecout.sandbox.persistence.jooq.author

import fr.sdecout.sandbox.persistence.jooq.shared.authorBooks
import fr.sdecout.sandbox.persistence.jooq.shared.toAuthorField
import fr.sdecout.sandbox.persistence.jooq.tables.references.AUTHOR
import fr.sdecout.sandbox.rest.author.AuthorId
import fr.sdecout.sandbox.rest.author.AuthorResponse
import fr.sdecout.sandbox.rest.author.AuthorSearchResponseItem
import fr.sdecout.sandbox.rest.shared.BookField
import org.jooq.Record3
import org.jooq.Record4

internal fun Record4<AuthorId?, String?, String?, List<BookField>>.toAuthorResponse() = AuthorResponse(
    id = this.get(AUTHOR.ID)?.value,
    lastName = this.get(AUTHOR.LAST_NAME),
    firstName = this.get(AUTHOR.FIRST_NAME),
    publishedBooks = this.get(authorBooks),
)

internal fun Record3<AuthorId?, String?, String?>.toAuthorSearchResponseItem() = AuthorSearchResponseItem(this.toAuthorField())
