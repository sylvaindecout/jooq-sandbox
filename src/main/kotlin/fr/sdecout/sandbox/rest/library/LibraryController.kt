package fr.sdecout.sandbox.rest.library

import fr.sdecout.sandbox.rest.book.Isbn
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/libraries")
class LibraryController(
    private val librarySearch: LibrarySearch,
    private val libraryCreation: LibraryCreation,
    private val bookCollectionUpdate: BookCollectionUpdate,
) {

    @PostMapping("/search", produces = [APPLICATION_JSON_VALUE])
    fun searchLibraries(@RequestParam postalCode: String): List<LibrarySearchResponseItem> = librarySearch
        .searchLibrariesClosestTo(PostalCode(postalCode))

    @PostMapping(consumes = [APPLICATION_JSON_VALUE])
    fun addLibrary(@Valid @RequestBody requestBody: LibraryCreationRequest): ResponseEntity<Void> = libraryCreation
        .addLibrary(requestBody.name!!, requestBody.address!!)
        .let { URI.create("/libraries/${it.value}") }
        .let { ResponseEntity.created(it).build() }

    @PostMapping("/{libraryId}/books")
    @ResponseStatus(NO_CONTENT)
    fun addBook(@PathVariable libraryId: String, @RequestParam isbn: String) {
        bookCollectionUpdate.addBook(LibraryId(libraryId), Isbn(isbn))
    }

}
