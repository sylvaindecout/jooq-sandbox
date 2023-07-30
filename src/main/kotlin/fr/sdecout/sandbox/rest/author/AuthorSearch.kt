package fr.sdecout.sandbox.rest.author

fun interface AuthorSearch {
    fun searchAuthors(hint: String): List<AuthorSearchResponseItem>
}
