package fr.sdecout.sandbox.rest.author

import jakarta.validation.constraints.NotBlank

data class AuthorCreationRequest(
    @field:NotBlank val lastName: String?,
    @field:NotBlank val firstName: String?,
)
