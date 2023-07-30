package fr.sdecout.sandbox.rest.library

@JvmInline
value class PostalCode(val value: String) {

    init {
        if (!value.matches(Regex("[0-9]{5}"))) {
            throw IllegalArgumentException("Postal code must contain 5 numeric digits")
        }
    }

    val departmentCode get() = value.substring(0..1)

    override fun toString() = value

}
