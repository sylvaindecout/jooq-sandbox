package fr.sdecout.sandbox.persistence.jpa.library

import fr.sdecout.sandbox.persistence.jpa.book.BookRepository
import fr.sdecout.sandbox.persistence.jpa.shared.toAddress
import fr.sdecout.sandbox.rest.book.Isbn
import fr.sdecout.sandbox.rest.library.*
import fr.sdecout.sandbox.rest.shared.AddressField
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("jpa")
@Transactional
class JpaLibraryAdapter(
    private val libraryRepository: LibraryRepository,
    private val bookRepository: BookRepository,
) : LibrarySearch, LibraryCreation, BookCollectionUpdate {

    override fun searchLibrariesClosestTo(postalCode: PostalCode): List<LibrarySearchResponseItem> =
        libraryRepository.findByAddress_postalCodeStartingWith(postalCode.departmentCode)
            .map { it.toLibrarySearchResponseItem() }

    override fun addLibrary(name: String, address: AddressField): LibraryId = newLibraryEntity(name, address)
        .let { libraryRepository.save(it) }
        .let { LibraryId(it.id) }

    override fun addBook(libraryId: LibraryId, isbn: Isbn) {
        libraryRepository.getReferenceById(libraryId.value)
            .apply { bookRepository.getReferenceById(isbn.compressedValue).let { availableBooks?.add(it) } }
            .let { libraryRepository.save(it) }
    }

    private fun newLibraryEntity(name: String, address: AddressField) = LibraryEntity(
        id = LibraryId.next().value,
        name = name,
        address = address.toAddress(),
        availableBooks = mutableListOf(),
    )

}
