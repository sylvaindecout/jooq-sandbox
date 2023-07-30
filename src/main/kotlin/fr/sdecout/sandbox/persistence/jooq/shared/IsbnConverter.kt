package fr.sdecout.sandbox.persistence.jooq.shared

import fr.sdecout.sandbox.rest.book.Isbn
import org.jooq.impl.AbstractConverter

class IsbnConverter: AbstractConverter<String, Isbn>(String::class.java, Isbn::class.java) {
    override fun from(databaseObject: String) = Isbn(databaseObject)
    override fun to(userObject: Isbn) = userObject.compressedValue
}
