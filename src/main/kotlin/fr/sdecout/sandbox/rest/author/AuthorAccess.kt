package fr.sdecout.sandbox.rest.author

fun interface AuthorAccess {
    fun findAuthor(id: AuthorId): AuthorResponse?
}
