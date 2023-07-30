package fr.sdecout.sandbox.persistence.jooq.book

import fr.sdecout.sandbox.persistence.jooq.shared.bookAuthors
import fr.sdecout.sandbox.persistence.jooq.shared.bookLibraries
import fr.sdecout.sandbox.persistence.jooq.shared.toBookField
import fr.sdecout.sandbox.persistence.jooq.tables.references.BOOK
import fr.sdecout.sandbox.rest.book.BookResponse
import fr.sdecout.sandbox.rest.book.BookSearchResponseItem
import fr.sdecout.sandbox.rest.book.Isbn
import fr.sdecout.sandbox.rest.shared.AuthorField
import fr.sdecout.sandbox.rest.shared.LibraryField
import org.jooq.Record3
import org.jooq.Record4

internal fun Record4<Isbn?, String?, List<AuthorField>, List<LibraryField>>.toBookResponse() = BookResponse(
    isbn = this.get(BOOK.ISBN)?.formattedValue,
    title = this.get(BOOK.TITLE),
    authors = this.get(bookAuthors),
    availability = this.get(bookLibraries),
)

internal fun Record3<Isbn?, String?, List<AuthorField>>.toBookSearchResponseItem() = BookSearchResponseItem(this.toBookField())
