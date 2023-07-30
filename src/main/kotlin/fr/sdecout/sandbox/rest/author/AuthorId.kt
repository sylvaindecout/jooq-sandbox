package fr.sdecout.sandbox.rest.author

import java.util.*

@JvmInline
value class AuthorId(val value: String) {

    init {
        if (value.isBlank()) {
            throw IllegalArgumentException("Author ID must not be blank")
        }
        if (value.length > 36) {
            throw IllegalArgumentException("Author ID must not have more than 36 characters")
        }
    }

    override fun toString() = value

    companion object {
        fun next(): AuthorId = AuthorId(UUID.randomUUID().toString())
    }

}
