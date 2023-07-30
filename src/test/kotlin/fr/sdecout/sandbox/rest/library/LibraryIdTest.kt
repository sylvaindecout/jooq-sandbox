package fr.sdecout.sandbox.rest.library

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength

class LibraryIdTest : ShouldSpec({

    should("fail to initialize from blank value") {
        shouldThrow<IllegalArgumentException> { LibraryId("") }
            .message shouldBe "Library ID must not be blank"
    }

    should("fail to initialize from value with more than 36 characters") {
        val value = "1111111111111111111111111111111111111"
        value shouldHaveLength 37

        shouldThrow<IllegalArgumentException> { LibraryId(value) }
            .message shouldBe "Library ID must not have more than 36 characters"
    }

    should("display as string") {
        val value = "111111111111111111111111111111111111"
        value shouldHaveLength 36

        LibraryId(value).toString() shouldBe value
    }

})
