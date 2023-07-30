package fr.sdecout.sandbox.persistence.jpa.library

import fr.sdecout.sandbox.persistence.jpa.shared.toLibraryReference
import fr.sdecout.sandbox.rest.library.LibrarySearchResponseItem

internal fun LibraryEntity.toLibrarySearchResponseItem() = LibrarySearchResponseItem(this.toLibraryReference())
