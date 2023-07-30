package fr.sdecout.sandbox.persistence.jpa.author

import fr.sdecout.sandbox.persistence.jpa.book.BookEntity
import fr.sdecout.sandbox.persistence.jpa.shared.toAuthorReference
import fr.sdecout.sandbox.persistence.jpa.shared.toBookReference
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class MappingKtTest : ShouldSpec({

    context("map AuthorEntity to AuthorSearchResponseItem") {
        val validAuthor = AuthorEntity("1", "X", "X", mutableListOf())

        should("with author") {
            val domainObject = validAuthor.copy(id = "123")

            val dto = domainObject.toAuthorSearchResponseItem()

            dto.author shouldBe domainObject.toAuthorReference()
        }
    }

    context("map AuthorEntity to AuthorResponse") {
        val validAuthor = AuthorEntity("1", "X", "X", mutableListOf())
        val validBook = BookEntity("0000000000000", "X", mutableListOf(), mutableListOf())

        should("with ID") {
            val domainObject = validAuthor.copy(id = "123")

            val dto = domainObject.toAuthorResponse()

            dto.id shouldBe "123"
        }

        should("with last name") {
            val domainObject = validAuthor.copy(lastName = "Moroder")

            val dto = domainObject.toAuthorResponse()

            dto.lastName shouldBe "Moroder"
        }

        should("with first name") {
            val domainObject = validAuthor.copy(firstName = "Giorgio")

            val dto = domainObject.toAuthorResponse()

            dto.firstName shouldBe "Giorgio"
        }

        should("with published books") {
            val book1 = validBook.copy(isbn = "1111111111111")
            val book2 = validBook.copy(isbn = "2222222222222")
            val domainObject = validAuthor.copy(publishedBooks = mutableListOf(book1, book2))

            val dto = domainObject.toAuthorResponse()

            dto.publishedBooks shouldBe listOf(book1.toBookReference(), book2.toBookReference())
        }
    }

})
