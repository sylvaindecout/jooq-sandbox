package fr.sdecout.sandbox.persistence.jpa.author

import org.springframework.data.jpa.repository.JpaRepository

interface AuthorRepository: JpaRepository<AuthorEntity, String> {
    fun findByLastNameLikeIgnoringCase(hint: String): List<AuthorEntity>
}
