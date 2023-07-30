package fr.sdecout.sandbox.rest.library

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength

class PostalCodeTest : ShouldSpec({

    should("fail to initialize from value with less than 5 digits") {
        val value = "1111"
        value shouldHaveLength 4

        shouldThrow<IllegalArgumentException> { PostalCode(value) }
            .message shouldBe "Postal code must contain 5 numeric digits"
    }

    should("fail to initialize from value with more than 5 digits") {
        val value = "111111"
        value shouldHaveLength 6

        shouldThrow<IllegalArgumentException> { PostalCode(value) }
            .message shouldBe "Postal code must contain 5 numeric digits"
    }

    should("fail to initialize from value including non-numeric digits") {
        val value = "1111a"
        value shouldHaveLength 5

        shouldThrow<IllegalArgumentException> { PostalCode(value) }
            .message shouldBe "Postal code must contain 5 numeric digits"
    }

    should("get department code") {
        PostalCode("28200").departmentCode shouldBe "28"
    }

    should("display as string") {
        val value = "11111"
        value shouldHaveLength 5

        PostalCode(value).toString() shouldBe value
    }

})
