package fr.sdecout.sandbox.rest.shared

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class BookField(
    @field:NotBlank val isbn: String?,
    @field:NotBlank val title: String?,
    @field:Valid @field:NotEmpty val authors: List<AuthorField>?,
)
