package fr.sdecout.sandbox.persistence.jooq.shared

import fr.sdecout.sandbox.rest.author.AuthorId
import org.jooq.impl.AbstractConverter

class AuthorIdConverter: AbstractConverter<String, AuthorId>(String::class.java, AuthorId::class.java) {
    override fun from(databaseObject: String) = AuthorId(databaseObject)
    override fun to(userObject: AuthorId) = userObject.value
}
