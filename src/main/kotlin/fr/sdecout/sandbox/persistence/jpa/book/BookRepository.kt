package fr.sdecout.sandbox.persistence.jpa.book

import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository: JpaRepository<BookEntity, String> {
    fun findByTitleLikeIgnoringCase(hint: String): List<BookEntity>
}
