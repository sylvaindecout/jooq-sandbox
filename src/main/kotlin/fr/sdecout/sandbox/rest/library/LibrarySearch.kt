package fr.sdecout.sandbox.rest.library

fun interface LibrarySearch {
    fun searchLibrariesClosestTo(postalCode: PostalCode): List<LibrarySearchResponseItem>
}
