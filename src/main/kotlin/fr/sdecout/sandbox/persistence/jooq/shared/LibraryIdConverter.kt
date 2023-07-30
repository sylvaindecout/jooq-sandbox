package fr.sdecout.sandbox.persistence.jooq.shared

import fr.sdecout.sandbox.rest.library.LibraryId
import org.jooq.impl.AbstractConverter

class LibraryIdConverter: AbstractConverter<String, LibraryId>(String::class.java, LibraryId::class.java) {
    override fun from(databaseObject: String) = LibraryId(databaseObject)
    override fun to(userObject: LibraryId) = userObject.value
}
