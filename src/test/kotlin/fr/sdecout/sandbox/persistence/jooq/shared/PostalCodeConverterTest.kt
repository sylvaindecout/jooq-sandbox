package fr.sdecout.sandbox.persistence.jooq.shared

import fr.sdecout.sandbox.rest.library.PostalCode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class PostalCodeConverterTest: ShouldSpec({

    should("convert user object from DB object") {
        val dbObject = "92400"

        val userObject = PostalCodeConverter().from(dbObject)

        userObject shouldBe PostalCode("92400")
    }

    should("convert DB object to user object") {
        val userObject = PostalCode("92400")

        val dbObject = PostalCodeConverter().to(userObject)

        dbObject shouldBe "92400"
    }

})
