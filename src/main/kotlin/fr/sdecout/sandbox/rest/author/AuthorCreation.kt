package fr.sdecout.sandbox.rest.author

fun interface AuthorCreation {
    fun addAuthor(lastName: String, firstName: String): AuthorId
}
