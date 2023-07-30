package fr.sdecout.sandbox.persistence.jpa.library

import fr.sdecout.sandbox.persistence.jpa.shared.toLibraryReference
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class MappingKtTest : ShouldSpec({

    context("map LibraryEntity to LibrarySearchResponseItem") {
        val validAddress = Address("XXX", null, "11111", "XXX")
        val validLibrary = LibraryEntity("1", "XXX", validAddress, mutableListOf())

        should("with library") {
            val domainObject = validLibrary.copy(id = "123")

            val dto = domainObject.toLibrarySearchResponseItem()

            dto.library shouldBe domainObject.toLibraryReference()
        }
    }

})
