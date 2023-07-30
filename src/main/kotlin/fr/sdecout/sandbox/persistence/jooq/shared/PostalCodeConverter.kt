package fr.sdecout.sandbox.persistence.jooq.shared

import fr.sdecout.sandbox.rest.library.PostalCode
import org.jooq.impl.AbstractConverter

class PostalCodeConverter: AbstractConverter<String, PostalCode>(String::class.java, PostalCode::class.java) {
    override fun from(databaseObject: String) = PostalCode(databaseObject)
    override fun to(userObject: PostalCode) = userObject.value
}
