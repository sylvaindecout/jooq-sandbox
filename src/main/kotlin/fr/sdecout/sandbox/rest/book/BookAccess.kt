package fr.sdecout.sandbox.rest.book

fun interface BookAccess {
    fun findBook(isbn: Isbn): BookResponse?
}
