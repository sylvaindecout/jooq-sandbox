package fr.sdecout.sandbox

import fr.sdecout.sandbox.rest.author.AuthorAccess
import fr.sdecout.sandbox.rest.author.AuthorCreation
import fr.sdecout.sandbox.rest.author.AuthorId
import fr.sdecout.sandbox.rest.author.AuthorSearch
import fr.sdecout.sandbox.rest.book.BookAccess
import fr.sdecout.sandbox.rest.book.BookSearch
import fr.sdecout.sandbox.rest.book.BookUpdate
import fr.sdecout.sandbox.rest.library.BookCollectionUpdate
import fr.sdecout.sandbox.rest.library.LibraryCreation
import fr.sdecout.sandbox.rest.library.LibraryId
import fr.sdecout.sandbox.rest.library.LibrarySearch
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("!jpa")
@Configuration
class StubConfig {

    @Bean
    fun authorAccess(): AuthorAccess = AuthorAccess { null }

    @Bean
    fun authorCreation(): AuthorCreation = AuthorCreation { _, _ -> AuthorId.next() }

    @Bean
    fun authorSearch(): AuthorSearch = AuthorSearch { emptyList() }

    @Bean
    fun bookAccess(): BookAccess = BookAccess { null }

    @Bean
    fun bookSearch(): BookSearch = BookSearch { emptyList() }

    @Bean
    fun bookUpdate(): BookUpdate = BookUpdate { _, _, _ -> run {} }

    @Bean
    fun bookCollectionUpdate(): BookCollectionUpdate = BookCollectionUpdate { _, _ -> run {} }

    @Bean
    fun libraryCreation(): LibraryCreation = LibraryCreation { _, _ -> LibraryId.next() }

    @Bean
    fun librarySearch(): LibrarySearch = LibrarySearch { emptyList() }

}
