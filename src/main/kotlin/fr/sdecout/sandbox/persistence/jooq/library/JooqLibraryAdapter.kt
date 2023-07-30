package fr.sdecout.sandbox.persistence.jooq.library

import fr.sdecout.sandbox.persistence.jooq.tables.references.*
import fr.sdecout.sandbox.rest.book.*
import fr.sdecout.sandbox.rest.library.*
import fr.sdecout.sandbox.rest.shared.AddressField
import org.jooq.DSLContext
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("jooq")
class JooqLibraryAdapter(
    private val dsl: DSLContext,
) : LibrarySearch, LibraryCreation, BookCollectionUpdate {

    override fun addBook(libraryId: LibraryId, isbn: Isbn) {
        dsl.insertInto(LIBRARY_BOOK)
            .set(newLibraryBookRecord(isbn, libraryId))
            .onDuplicateKeyIgnore()
            .execute()
    }

    override fun addLibrary(name: String, address: AddressField): LibraryId = LibraryId.next().also {
        dsl.insertInto(LIBRARY)
            .set(newLibraryRecord(it, name, address))
            .execute()
    }

    override fun searchLibrariesClosestTo(postalCode: PostalCode): List<LibrarySearchResponseItem> = dsl
        .selectFrom(LIBRARY)
        .where(LIBRARY.POSTAL_CODE.cast(String::class.java).startsWithIgnoreCase(postalCode.departmentCode))
        .fetch { it.toLibrarySearchResponseItem() }

    private fun newLibraryBookRecord(isbn: Isbn, libraryId: LibraryId) = LIBRARY_BOOK.newRecord()
        .with(LIBRARY_BOOK.BOOK, isbn)
        .with(LIBRARY_BOOK.LIBRARY, libraryId)

    private fun newLibraryRecord(id: LibraryId, name: String, address: AddressField) = LIBRARY.newRecord()
        .with(LIBRARY.ID, id)
        .with(LIBRARY.NAME, name)
        .with(LIBRARY.ADDRESS_LINE_1, address.line1)
        .with(LIBRARY.ADDRESS_LINE_2, address.line2)
        .with(LIBRARY.POSTAL_CODE, PostalCode(address.postalCode!!))
        .with(LIBRARY.CITY, address.city)

}
