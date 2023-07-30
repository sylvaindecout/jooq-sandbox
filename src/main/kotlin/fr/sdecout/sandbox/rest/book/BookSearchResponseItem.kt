package fr.sdecout.sandbox.rest.book

import com.fasterxml.jackson.annotation.JsonUnwrapped
import fr.sdecout.sandbox.rest.shared.BookField
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

data class BookSearchResponseItem(
    @field:JsonUnwrapped @field:NotNull @field:Valid val book: BookField?,
)
