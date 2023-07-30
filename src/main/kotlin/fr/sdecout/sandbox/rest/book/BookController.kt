package fr.sdecout.sandbox.rest.book

import fr.sdecout.sandbox.rest.author.AuthorId
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(
    private val bookAccess: BookAccess,
    private val bookSearch: BookSearch,
    private val bookUpdate: BookUpdate,
) {

    @GetMapping("/{isbn}", produces = [APPLICATION_JSON_VALUE])
    fun getBook(@PathVariable isbn: String): ResponseEntity<BookResponse> = bookAccess.findBook(Isbn(isbn))
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    @PostMapping("/search", produces = [APPLICATION_JSON_VALUE])
    fun searchBooks(@RequestParam hint: String): List<BookSearchResponseItem> = bookSearch.searchBooks(hint)

    @PutMapping("/{isbn}", consumes = [APPLICATION_JSON_VALUE])
    @ResponseStatus(NO_CONTENT)
    fun addOrUpdateBook(@PathVariable isbn: String, @Valid @RequestBody requestBody: BookUpdateRequest) {
        bookUpdate.save(Isbn(isbn), requestBody.title!!, requestBody.authors!!.map { AuthorId(it) })
    }

}
