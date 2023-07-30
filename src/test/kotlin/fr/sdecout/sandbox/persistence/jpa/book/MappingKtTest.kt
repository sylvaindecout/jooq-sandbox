package fr.sdecout.sandbox.persistence.jpa.book

import fr.sdecout.sandbox.persistence.jpa.author.AuthorEntity
import fr.sdecout.sandbox.persistence.jpa.library.Address
import fr.sdecout.sandbox.persistence.jpa.library.LibraryEntity
import fr.sdecout.sandbox.persistence.jpa.shared.toAuthorReference
import fr.sdecout.sandbox.persistence.jpa.shared.toBookReference
import fr.sdecout.sandbox.persistence.jpa.shared.toLibraryReference
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class MappingKtTest : ShouldSpec({

    context("map BookEntity to BookSearchResponse") {
        val validBook = BookEntity("0000000000000", "X", mutableListOf(), mutableListOf())

        should("with book") {
            val domainObject = validBook.copy(isbn = "1234567890123")

            val dto = domainObject.toBookSearchResponse()

            dto.book shouldBe domainObject.toBookReference()
        }
    }

    context("map BookEntity to BookResponse") {
        val validAuthor = AuthorEntity("1", "X", "X", mutableListOf())
        val validBook = BookEntity("0000000000000", "X", mutableListOf(), mutableListOf())
        val validAddress = Address("XXX", null, "11111", "XXX")
        val validLibrary = LibraryEntity("1", "XXX", validAddress, mutableListOf())

        should("with ISBN") {
            val domainObject = validBook.copy(isbn = "1234567890123")

            val dto = domainObject.toBookResponse()

            dto.isbn shouldBe "123-4-56-789012-3"
        }

        should("with title") {
            val domainObject = validBook.copy(title = "Moby Dick")

            val dto = domainObject.toBookResponse()

            dto.title shouldBe "Moby Dick"
        }

        should("with authors") {
            val author1 = validAuthor.copy(id = "1")
            val author2 = validAuthor.copy(id = "2")
            val domainObject = validBook.copy(authors = mutableListOf(author1, author2))

            val dto = domainObject.toBookResponse()

            dto.authors shouldBe listOf(author1.toAuthorReference(), author2.toAuthorReference())
        }

        should("with availability") {
            val library1 = validLibrary.copy(id = "1")
            val library2 = validLibrary.copy(id = "2")
            val domainObject = validBook.copy(availableInLibraries = mutableListOf(library1, library2))

            val dto = domainObject.toBookResponse()

            dto.availability shouldBe listOf(library1.toLibraryReference(), library2.toLibraryReference())
        }
    }

})
