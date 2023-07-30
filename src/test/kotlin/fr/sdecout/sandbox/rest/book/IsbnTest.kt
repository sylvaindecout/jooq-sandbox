package fr.sdecout.sandbox.rest.book

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength

class IsbnTest : ShouldSpec({

    should("fail to initialize from value with less than 13 digits") {
        val value = "978316148410"
        value shouldHaveLength 12

        shouldThrow<IllegalArgumentException> { Isbn(value) }
            .message shouldBe "ISBN must contain 13 numeric digits"
    }

    should("fail to initialize from value with more than 13 digits") {
        val value = "97831614841000"
        value shouldHaveLength 14

        shouldThrow<IllegalArgumentException> { Isbn(value) }
            .message shouldBe "ISBN must contain 13 numeric digits"
    }

    should("fail to initialize from value including non-numeric characters") {
        val value = "978316148410a"
        value shouldHaveLength 13

        shouldThrow<IllegalArgumentException> { Isbn(value) }
            .message shouldBe "ISBN must contain 13 numeric digits"
    }

    should("display as string") {
        Isbn("9783161484100").toString() shouldBe "978-3-16-148410-0"
    }

})
