package fr.sdecout.sandbox.persistence.jpa.shared

import fr.sdecout.sandbox.persistence.jpa.author.AuthorEntity
import fr.sdecout.sandbox.persistence.jpa.book.BookEntity
import fr.sdecout.sandbox.persistence.jpa.library.Address
import fr.sdecout.sandbox.persistence.jpa.library.LibraryEntity
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class MappingKtTest : ShouldSpec({

    context("map AuthorEntity to AuthorReference") {
        val validAuthor = AuthorEntity("1", "X", "X", mutableListOf())

        should("with ID") {
            val domainObject = validAuthor.copy(id = "123")

            val dto = domainObject.toAuthorReference()

            dto.id shouldBe "123"
        }

        should("with last name") {
            val domainObject = validAuthor.copy(lastName = "Moroder")

            val dto = domainObject.toAuthorReference()

            dto.lastName shouldBe "Moroder"
        }

        should("with first name") {
            val domainObject = validAuthor.copy(firstName = "Giorgio")

            val dto = domainObject.toAuthorReference()

            dto.firstName shouldBe "Giorgio"
        }
    }

    context("map BookEntity to BookReference") {
        val validAuthor = AuthorEntity("1", "X", "X", mutableListOf())
        val validBook = BookEntity("0000000000000", "X", mutableListOf(), mutableListOf())

        should("with ISBN") {
            val domainObject = validBook.copy(isbn = "1234567890123")

            val dto = domainObject.toBookReference()

            dto.isbn shouldBe "123-4-56-789012-3"
        }

        should("with title") {
            val domainObject = validBook.copy(title = "Moby Dick")

            val dto = domainObject.toBookReference()

            dto.title shouldBe "Moby Dick"
        }

        should("with authors") {
            val author1 = validAuthor.copy(id = "1")
            val author2 = validAuthor.copy(id = "2")
            val domainObject = validBook.copy(authors = mutableListOf(author1, author2))

            val dto = domainObject.toBookReference()

            dto.authors shouldBe listOf(author1.toAuthorReference(), author2.toAuthorReference())
        }
    }

    context("map LibraryEntity to LibraryReference") {
        val validAddress = Address("XXX", null, "11111", "XXX")
        val validLibrary = LibraryEntity("1", "XXX", validAddress, mutableListOf())

        should("with ID") {
            val domainObject = validLibrary.copy(id = "123")

            val dto = domainObject.toLibraryReference()

            dto.id shouldBe "123"
        }

        should("with name") {
            val domainObject = validLibrary.copy(name = "La bibli")

            val dto = domainObject.toLibraryReference()

            dto.name shouldBe "La bibli"
        }

        should("with address") {
            val address = Address("3 rue des Lilas", "Cedex 1234", "28200", "Châteaudun")
            val domainObject = validLibrary.copy(address = address)

            val dto = domainObject.toLibraryReference()

            dto.address shouldBe address.toAddressField()
        }
    }

    context("map Address to AddressField") {
        val validAddress = Address("XXX", null, "11111", "XXX")

        should("with line 1") {
            val domainObject = validAddress.copy(line1 = "3 rue des Lilas")

            val dto = domainObject.toAddressField()

            dto.line1 shouldBe "3 rue des Lilas"
        }

        should("with line 2") {
            val domainObject = validAddress.copy(line2 = "Cedex 1234")

            val dto = domainObject.toAddressField()

            dto.line2 shouldBe "Cedex 1234"
        }

        should("with no line 2 if undefined") {
            val domainObject = validAddress.copy(line2 = null)

            val dto = domainObject.toAddressField()

            dto.line2 shouldBe null
        }

        should("with postal code") {
            val domainObject = validAddress.copy(postalCode = "28200")

            val dto = domainObject.toAddressField()

            dto.postalCode shouldBe "28200"
        }

        should("with city name") {
            val domainObject = validAddress.copy(city = "Châteaudun")

            val dto = domainObject.toAddressField()

            dto.city shouldBe "Châteaudun"
        }
    }

})
