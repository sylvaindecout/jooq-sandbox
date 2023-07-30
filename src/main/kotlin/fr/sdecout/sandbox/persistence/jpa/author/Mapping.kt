package fr.sdecout.sandbox.persistence.jpa.author

import fr.sdecout.sandbox.persistence.jpa.shared.toAuthorReference
import fr.sdecout.sandbox.persistence.jpa.shared.toBookReference
import fr.sdecout.sandbox.rest.author.AuthorResponse
import fr.sdecout.sandbox.rest.author.AuthorSearchResponseItem

internal fun AuthorEntity.toAuthorResponse() = AuthorResponse(
    id = this.id,
    lastName = this.lastName,
    firstName = this.firstName,
    publishedBooks = this.publishedBooks?.map { it.toBookReference() },
)

internal fun AuthorEntity.toAuthorSearchResponseItem() = AuthorSearchResponseItem(this.toAuthorReference())
