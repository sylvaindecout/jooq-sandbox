package fr.sdecout.sandbox.rest.author

import com.fasterxml.jackson.annotation.JsonUnwrapped
import fr.sdecout.sandbox.rest.shared.AuthorField
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

data class AuthorSearchResponseItem(
    @field:JsonUnwrapped @field:NotNull @field:Valid val author: AuthorField?,
)
