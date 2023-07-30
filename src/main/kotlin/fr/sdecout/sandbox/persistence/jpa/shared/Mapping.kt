package fr.sdecout.sandbox.persistence.jpa.shared

import fr.sdecout.sandbox.persistence.jpa.author.AuthorEntity
import fr.sdecout.sandbox.persistence.jpa.book.BookEntity
import fr.sdecout.sandbox.persistence.jpa.library.Address
import fr.sdecout.sandbox.persistence.jpa.library.LibraryEntity
import fr.sdecout.sandbox.rest.book.Isbn
import fr.sdecout.sandbox.rest.shared.AddressField
import fr.sdecout.sandbox.rest.shared.AuthorField
import fr.sdecout.sandbox.rest.shared.BookField
import fr.sdecout.sandbox.rest.shared.LibraryField

fun AuthorEntity.toAuthorReference() = AuthorField(
    id = this.id,
    lastName = this.lastName,
    firstName = this.firstName,
)

fun BookEntity.toBookReference() = BookField(
    isbn = this.isbn.formatAsIsbn(),
    title = this.title,
    authors = this.authors.map { it.toAuthorReference() },
)

fun LibraryEntity.toLibraryReference() = LibraryField(
    id = this.id,
    name = this.name,
    address = this.address.toAddressField(),
)

fun String.formatAsIsbn() = Isbn(this).toString()

fun AddressField.toAddress() = Address(
    line1 = this.line1!!,
    line2 = this.line2,
    postalCode = this.postalCode!!,
    city = this.city!!,
)

internal fun Address.toAddressField() = AddressField(
    line1 = this.line1,
    line2 = this.line2,
    postalCode = this.postalCode,
    city = this.city,
)
