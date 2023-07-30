package fr.sdecout.sandbox.persistence.jpa.library

import org.springframework.data.jpa.repository.JpaRepository

interface LibraryRepository: JpaRepository<LibraryEntity, String> {
    fun findByAddress_postalCodeStartingWith(departmentId: String): List<LibraryEntity>
}
