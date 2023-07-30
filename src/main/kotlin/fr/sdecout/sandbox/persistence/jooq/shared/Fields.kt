package fr.sdecout.sandbox.persistence.jooq.shared

import fr.sdecout.sandbox.persistence.jooq.tables.references.*
import org.jooq.impl.DSL.multiset
import org.jooq.impl.DSL.select

val bookAuthors = multiset(
    select(AUTHOR.ID, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
        .from(AUTHOR.join(BOOK_AUTHOR).on(BOOK_AUTHOR.AUTHOR.eq(AUTHOR.ID)))
        .where(BOOK_AUTHOR.BOOK.eq(BOOK.ISBN))
).`as`("authors").convertFrom { record -> record.map { it.toAuthorField() } }

val bookLibraries = multiset(
    select(LIBRARY.ID, LIBRARY.NAME, LIBRARY.ADDRESS_LINE_1, LIBRARY.ADDRESS_LINE_2, LIBRARY.POSTAL_CODE, LIBRARY.CITY)
        .from(LIBRARY.join(LIBRARY_BOOK).on(LIBRARY_BOOK.LIBRARY.eq(LIBRARY.ID)))
        .where(LIBRARY_BOOK.BOOK.eq(BOOK.ISBN))
).`as`("libraries").convertFrom { record -> record.map { it.toLibraryField() } }

val authorBooks = multiset(
    select(BOOK.ISBN, BOOK.TITLE, bookAuthors)
        .from(BOOK.join(BOOK_AUTHOR).on(BOOK_AUTHOR.BOOK.eq(BOOK.ISBN)))
        .where(BOOK_AUTHOR.AUTHOR.eq(AUTHOR.ID))
).`as`("books").convertFrom { record -> record.map { it.toBookField() } }
