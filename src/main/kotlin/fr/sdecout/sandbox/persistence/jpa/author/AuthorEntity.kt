package fr.sdecout.sandbox.persistence.jpa.author

import fr.sdecout.sandbox.persistence.jpa.book.BookEntity
import jakarta.persistence.*
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.FetchType.LAZY

@Entity
@Table(name = "AUTHOR")
data class AuthorEntity(

    @Id @Column(nullable = false) val id: String,

    @Column(nullable = false) val lastName: String,

    @Column(nullable = false) val firstName: String,

    @ManyToMany(fetch = LAZY, cascade = [ALL], mappedBy = "authors")
    var publishedBooks: MutableList<BookEntity>? = null

)
