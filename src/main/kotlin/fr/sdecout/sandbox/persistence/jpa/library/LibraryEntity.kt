package fr.sdecout.sandbox.persistence.jpa.library

import fr.sdecout.sandbox.persistence.jpa.book.BookEntity
import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY

@Entity
@Table(name = "LIBRARY")
data class LibraryEntity(

    @Id @Column(nullable = false) val id: String,

    @Column(nullable = false) val name: String,

    @Embedded val address: Address,

    @ManyToMany(fetch = LAZY)
    @JoinTable(
        name = "LIBRARY_BOOK",
        joinColumns = [JoinColumn(name = "LIBRARY", referencedColumnName = "ID")],
        inverseJoinColumns = [JoinColumn(name = "BOOK", referencedColumnName = "ISBN")]
    )
    var availableBooks: MutableList<BookEntity>? = null

)
