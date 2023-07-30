package fr.sdecout.sandbox.rest.library

import fr.sdecout.sandbox.rest.shared.AddressField

fun interface LibraryCreation {
    fun addLibrary(name: String, address: AddressField): LibraryId
}
