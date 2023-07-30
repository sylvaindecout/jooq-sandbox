package fr.sdecout.sandbox.rest.author

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength

class AuthorIdTest : ShouldSpec({

    should("fail to initialize from blank value") {
        shouldThrow<IllegalArgumentException> { AuthorId("") }
            .message shouldBe "Author ID must not be blank"
    }

    should("fail to initialize from value with more than 36 characters") {
        val value = "1111111111111111111111111111111111111"
        value shouldHaveLength 37

        shouldThrow<IllegalArgumentException> { AuthorId(value) }
            .message shouldBe "Author ID must not have more than 36 characters"
    }

    should("display as string") {
        val value = "111111111111111111111111111111111111"
        value shouldHaveLength 36

        AuthorId(value).toString() shouldBe value
    }

})
