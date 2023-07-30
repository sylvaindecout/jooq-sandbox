package fr.sdecout.sandbox.persistence.jpa.book

import fr.sdecout.sandbox.persistence.jpa.author.AuthorEntity
import fr.sdecout.sandbox.persistence.jpa.library.LibraryEntity
import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY

@Entity
@Table(name = "BOOK")
data class BookEntity(

    @Id @Column(nullable = false) val isbn: String,

    @Column(nullable = false) val title: String,

    @ManyToMany
    @JoinTable(
        name = "BOOK_AUTHOR",
        joinColumns = [JoinColumn(name = "BOOK", referencedColumnName = "ISBN")],
        inverseJoinColumns = [JoinColumn(name = "AUTHOR", referencedColumnName = "ID")]
    )
    val authors: MutableList<AuthorEntity>,

    @ManyToMany(fetch = LAZY, mappedBy = "availableBooks")
    var availableInLibraries: MutableList<LibraryEntity>? = null

)
