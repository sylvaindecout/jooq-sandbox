package fr.sdecout.sandbox.persistence.jooq.shared

import fr.sdecout.sandbox.rest.book.Isbn
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class IsbnConverterTest: ShouldSpec({

    should("convert user object from DB object") {
        val dbObject = "9783161484100"

        val userObject = IsbnConverter().from(dbObject)

        userObject shouldBe Isbn("9783161484100")
    }

    should("convert DB object to user object") {
        val userObject = Isbn("9783161484100")

        val dbObject = IsbnConverter().to(userObject)

        dbObject shouldBe "9783161484100"
    }

})
