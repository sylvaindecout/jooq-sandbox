package fr.sdecout.sandbox.persistence.jooq.shared

import fr.sdecout.sandbox.persistence.jooq.tables.references.AUTHOR
import fr.sdecout.sandbox.persistence.jooq.tables.references.BOOK
import fr.sdecout.sandbox.persistence.jooq.tables.references.LIBRARY
import fr.sdecout.sandbox.rest.author.AuthorId
import fr.sdecout.sandbox.rest.book.Isbn
import fr.sdecout.sandbox.rest.library.LibraryId
import fr.sdecout.sandbox.rest.library.PostalCode
import fr.sdecout.sandbox.rest.shared.AddressField
import fr.sdecout.sandbox.rest.shared.AuthorField
import fr.sdecout.sandbox.rest.shared.BookField
import fr.sdecout.sandbox.rest.shared.LibraryField
import org.jooq.Record3
import org.jooq.Record6

fun Record3<AuthorId?, String?, String?>.toAuthorField() = AuthorField(
    id = this.get(AUTHOR.ID)?.value,
    lastName = this.get(AUTHOR.LAST_NAME),
    firstName = this.get(AUTHOR.FIRST_NAME),
)

fun Record3<Isbn?, String?, List<AuthorField>>.toBookField() = BookField(
    isbn = this.get(BOOK.ISBN)?.formattedValue,
    title = this.get(BOOK.TITLE),
    authors = this.get(bookAuthors),
)

fun Record6<LibraryId?, String?, String?, String?, PostalCode?, String?>.toLibraryField() = LibraryField(
    id = this.get(LIBRARY.ID)?.value,
    name = this.get(LIBRARY.NAME),
    address = this.toAddressField(),
)

private fun Record6<LibraryId?, String?, String?, String?, PostalCode?, String?>.toAddressField() = AddressField(
    line1 = this.get(LIBRARY.ADDRESS_LINE_1),
    line2 = this.get(LIBRARY.ADDRESS_LINE_2),
    postalCode = this.get(LIBRARY.POSTAL_CODE)?.value,
    city = this.get(LIBRARY.CITY),
)
