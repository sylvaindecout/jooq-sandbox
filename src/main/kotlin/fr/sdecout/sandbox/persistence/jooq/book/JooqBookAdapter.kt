package fr.sdecout.sandbox.persistence.jooq.book

import fr.sdecout.sandbox.persistence.jooq.shared.bookAuthors
import fr.sdecout.sandbox.persistence.jooq.shared.bookLibraries
import fr.sdecout.sandbox.persistence.jooq.tables.references.*
import fr.sdecout.sandbox.rest.author.AuthorId
import fr.sdecout.sandbox.rest.book.*
import org.jooq.DSLContext
import org.jooq.impl.DSL.*
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("jooq")
class JooqBookAdapter(
    private val dsl: DSLContext,
) : BookAccess, BookSearch, BookUpdate {

    override fun findBook(isbn: Isbn): BookResponse? = dsl
        .select(BOOK.ISBN, BOOK.TITLE, bookAuthors, bookLibraries)
        .from(BOOK)
        .where(BOOK.ISBN.eq(isbn))
        .fetchOne { it.toBookResponse() }

    override fun searchBooks(hint: String): List<BookSearchResponseItem> = dsl
        .select(BOOK.ISBN, BOOK.TITLE, bookAuthors)
        .from(BOOK)
        .where(BOOK.TITLE.likeIgnoreCase("%$hint%"))
        .fetch { it.toBookSearchResponseItem() }

    override fun save(isbn: Isbn, title: String, authors: Collection<AuthorId>) {
        val upsertOperation = prepareBookUpdate(isbn, title)
        val removeAuthorOperation = prepareAuthorRemovals(isbn, authors)
        val addAuthorOperations = prepareAuthorInsertions(isbn, authors)
        dsl.batch(listOf(upsertOperation) + removeAuthorOperation + addAuthorOperations).execute()
    }

    private fun prepareBookUpdate(isbn: Isbn, title: String) = bookRecord(isbn, title)
        .let { dsl.insertInto(BOOK).set(it).onDuplicateKeyUpdate().set(it) }

    private fun bookRecord(isbn: Isbn, title: String) = BOOK.newRecord()
        .with(BOOK.ISBN, isbn)
        .with(BOOK.TITLE, title)

    private fun prepareAuthorRemovals(isbn: Isbn, authors: Collection<AuthorId>) = dsl.deleteFrom(BOOK_AUTHOR)
        .where(BOOK_AUTHOR.BOOK.eq(isbn))
        .and(BOOK_AUTHOR.AUTHOR.notIn(authors))

    private fun prepareAuthorInsertions(isbn: Isbn, authors: Collection<AuthorId>) = authors
        .map { bookAuthorRecord(isbn, it) }
        .map { dsl.insertInto(BOOK_AUTHOR).set(it).onDuplicateKeyIgnore() }

    private fun bookAuthorRecord(isbn: Isbn, authorId: AuthorId) = BOOK_AUTHOR.newRecord()
        .with(BOOK_AUTHOR.BOOK, isbn)
        .with(BOOK_AUTHOR.AUTHOR, authorId)

}
