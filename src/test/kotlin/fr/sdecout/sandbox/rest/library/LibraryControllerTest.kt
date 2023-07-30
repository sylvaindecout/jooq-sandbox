package fr.sdecout.sandbox.rest.library

import fr.sdecout.sandbox.TestApp
import fr.sdecout.sandbox.rest.BNF
import fr.sdecout.sandbox.rest.LES_SENS_DU_VOTE
import fr.sdecout.sandbox.rest.book.Isbn
import fr.sdecout.sandbox.rest.shared.AddressField
import io.kotest.core.NamedTag
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.LOCATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest(classes = [TestApp::class])
@AutoConfigureMockMvc
class LibraryControllerTest : ShouldSpec() {

    @MockBean
    private lateinit var librarySearch: LibrarySearch
    @MockBean
    private lateinit var libraryCreation : LibraryCreation
    @MockBean
    private lateinit var bookCollectionUpdate: BookCollectionUpdate

    @Autowired
    private lateinit var mockMvc: MockMvc

    init {
        tags(NamedTag("Integration"))
        extension(SpringExtension)

        should("search libraries closest to postal code") {
            val postalCode = PostalCode("75000")
            val expectedResponseBody = """[{
                "id":"${BNF.id}",
                "name": "${BNF.name}",
                "address": {
                    "line1": "${BNF.address?.line1}",
                    "postalCode": "${BNF.address?.postalCode}",
                    "city": "${BNF.address?.city}"
                }
            }]""".trimIndent()
            given(librarySearch.searchLibrariesClosestTo(postalCode)).willReturn(listOf(LibrarySearchResponseItem(BNF)))

            mockMvc.perform(post("/libraries/search")
                    .queryParam("postalCode", postalCode.value))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody))
        }

        should("add library") {
            val name = "Médiathèque de Châteaudun"
            val address = AddressField(
                line1 = "36, boulevard Grindelle",
                postalCode = "28200",
                city = "Châteaudun",
            )
            val requestBody = """{
                "name": "$name",
                "address": {
                    "line1": "${address.line1}",
                    "postalCode": "${address.postalCode}",
                    "city": "${address.city}"
                }
            }""".trimMargin()
            val newLibraryId = LibraryId("123")
            given(libraryCreation.addLibrary(name, address)).willReturn(newLibraryId)

            mockMvc.perform(post("/libraries")
                    .contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/libraries/${newLibraryId.value}"))
        }

        should("add book") {
            val libraryId = LibraryId(BNF.id!!)
            val isbn = Isbn(LES_SENS_DU_VOTE.isbn!!)

            mockMvc.perform(post("/libraries/{libraryId}/books", libraryId)
                    .queryParam("isbn", isbn.formattedValue))
                .andExpect(status().isNoContent())
            then(bookCollectionUpdate).should().addBook(libraryId, isbn)
        }
    }

}
