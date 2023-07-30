package fr.sdecout.sandbox.persistence.jooq.shared

import fr.sdecout.sandbox.rest.author.AuthorId
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class AuthorIdConverterTest: ShouldSpec({

    should("convert user object from DB object") {
        val dbObject = "123"

        val userObject = AuthorIdConverter().from(dbObject)

        userObject shouldBe AuthorId("123")
    }

    should("convert DB object to user object") {
        val userObject = AuthorId("123")

        val dbObject = AuthorIdConverter().to(userObject)

        dbObject shouldBe "123"
    }

})
