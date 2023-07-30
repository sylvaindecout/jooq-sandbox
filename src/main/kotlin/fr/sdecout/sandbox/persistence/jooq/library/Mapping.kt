package fr.sdecout.sandbox.persistence.jooq.library

import fr.sdecout.sandbox.persistence.jooq.shared.toLibraryField
import fr.sdecout.sandbox.persistence.jooq.tables.records.LibraryRecord
import fr.sdecout.sandbox.rest.library.LibrarySearchResponseItem

internal fun LibraryRecord.toLibrarySearchResponseItem() = LibrarySearchResponseItem(this.toLibraryField())
