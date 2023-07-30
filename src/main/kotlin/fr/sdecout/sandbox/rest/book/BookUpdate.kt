package fr.sdecout.sandbox.rest.book

import fr.sdecout.sandbox.rest.author.AuthorId

fun interface BookUpdate {
    fun save(isbn: Isbn, title: String, authors: Collection<AuthorId>)
}
