package fr.sdecout.sandbox.persistence.jooq.shared

import fr.sdecout.sandbox.rest.library.LibraryId
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class LibraryIdConverterTest: ShouldSpec({

    should("convert user object from DB object") {
        val dbObject = "123"

        val userObject = LibraryIdConverter().from(dbObject)

        userObject shouldBe LibraryId("123")
    }

    should("convert DB object to user object") {
        val userObject = LibraryId("123")

        val dbObject = LibraryIdConverter().to(userObject)

        dbObject shouldBe "123"
    }

})
