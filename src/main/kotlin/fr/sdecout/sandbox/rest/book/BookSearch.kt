package fr.sdecout.sandbox.rest.book

fun interface BookSearch {
    fun searchBooks(hint: String): List<BookSearchResponseItem>
}
