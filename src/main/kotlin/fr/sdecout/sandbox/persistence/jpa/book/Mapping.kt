package fr.sdecout.sandbox.persistence.jpa.book

import fr.sdecout.sandbox.persistence.jpa.shared.formatAsIsbn
import fr.sdecout.sandbox.persistence.jpa.shared.toAuthorReference
import fr.sdecout.sandbox.persistence.jpa.shared.toBookReference
import fr.sdecout.sandbox.persistence.jpa.shared.toLibraryReference
import fr.sdecout.sandbox.rest.book.BookResponse
import fr.sdecout.sandbox.rest.book.BookSearchResponseItem

internal fun BookEntity.toBookSearchResponse() = BookSearchResponseItem(this.toBookReference())

internal fun BookEntity.toBookResponse() = BookResponse(
    isbn = this.isbn.formatAsIsbn(),
    title = this.title,
    authors = this.authors.map { it.toAuthorReference() },
    availability = this.availableInLibraries?.map { it.toLibraryReference() },
)
