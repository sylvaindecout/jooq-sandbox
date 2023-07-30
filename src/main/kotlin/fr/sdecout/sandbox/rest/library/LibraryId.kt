package fr.sdecout.sandbox.rest.library

import java.util.*

@JvmInline
value class LibraryId(val value: String) {

    init {
        if (value.isBlank()) {
            throw IllegalArgumentException("Library ID must not be blank")
        }
        if (value.length > 36) {
            throw IllegalArgumentException("Library ID must not have more than 36 characters")
        }
    }

    override fun toString() = value

    companion object {
        fun next(): LibraryId = LibraryId(UUID.randomUUID().toString())
    }

}
