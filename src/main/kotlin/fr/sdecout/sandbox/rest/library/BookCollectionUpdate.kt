package fr.sdecout.sandbox.rest.library

import fr.sdecout.sandbox.rest.book.Isbn

fun interface BookCollectionUpdate {
    fun addBook(libraryId: LibraryId, isbn: Isbn)
}
