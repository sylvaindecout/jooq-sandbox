package fr.sdecout.sandbox.rest.book

import fr.sdecout.sandbox.TestApp
import fr.sdecout.sandbox.rest.BNF
import fr.sdecout.sandbox.rest.CRIME_AND_PUNISHMENT
import fr.sdecout.sandbox.rest.DOSTOEVSKY
import fr.sdecout.sandbox.rest.THE_IDIOT
import fr.sdecout.sandbox.rest.author.AuthorId
import io.kotest.core.NamedTag
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = [TestApp::class])
@AutoConfigureMockMvc
class BookControllerTest : ShouldSpec() {

    @MockBean
    private lateinit var bookAccess: BookAccess
    @MockBean
    private lateinit var bookSearch: BookSearch
    @MockBean
    private lateinit var bookUpdate: BookUpdate

    @Autowired
    private lateinit var mockMvc: MockMvc

    init {
        tags(NamedTag("Integration"))
        extension(SpringExtension)

        should("fail to find unknown book") {
            mockMvc.perform(get("/books/{isbn}", "0000000000000"))
                .andExpect(status().isNotFound())
        }

        should("find book") {
            val expectedResponseBody = """{
                "isbn": "${CRIME_AND_PUNISHMENT.isbn}",
                "title": "${CRIME_AND_PUNISHMENT.title}",
                "authors": [
                    {
                        "id":"${DOSTOEVSKY.id}",
                        "lastName": "${DOSTOEVSKY.lastName}",
                        "firstName": "${DOSTOEVSKY.firstName}"
                    }
                ],
                "availability": [
                    {
                        "id": "${BNF.id}",
                        "name": "${BNF.name}",
                        "address": {
                            "line1": "${BNF.address?.line1}",
                            "line2": "${BNF.address?.line2}",
                            "postalCode": "${BNF.address?.postalCode}",
                            "city": "${BNF.address?.city}"
                        }
                    }
                ]
            }""".trimIndent()
            given(bookAccess.findBook(Isbn(CRIME_AND_PUNISHMENT.isbn!!))).willReturn(
                BookResponse(
                    isbn = CRIME_AND_PUNISHMENT.isbn,
                    title = CRIME_AND_PUNISHMENT.title,
                    authors = listOf(DOSTOEVSKY),
                    availability = listOf(BNF),
                )
            )

            mockMvc.perform(get("/books/{isbn}", CRIME_AND_PUNISHMENT.isbn))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody))
        }

        should("search books matching hint") {
            val hint = "rime and punish"
            val expectedResponseBody = """[{
                "isbn": "${CRIME_AND_PUNISHMENT.isbn}",
                "title": "${CRIME_AND_PUNISHMENT.title}",
                "authors": [
                    {
                        "id":"${DOSTOEVSKY.id}",
                        "lastName": "${DOSTOEVSKY.lastName}",
                        "firstName": "${DOSTOEVSKY.firstName}"
                    }
                ]
            }]""".trimIndent()
            given(bookSearch.searchBooks(hint)).willReturn(listOf(
                BookSearchResponseItem(CRIME_AND_PUNISHMENT)
            ))

            mockMvc.perform(post("/books/search")
                    .queryParam("hint", hint))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody))
        }

        should("add book") {
            val isbn = Isbn(THE_IDIOT.isbn!!)
            val title = THE_IDIOT.title!!
            val authors = listOf(DOSTOEVSKY.id!!)
            val requestBody = """{
                "title": "$title",
                "authors": ["${DOSTOEVSKY.id}"]
            }""".trimMargin()

            mockMvc.perform(put("/books/{isbn}", isbn)
                    .contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNoContent())
            then(bookUpdate).should().save(isbn, title, authors.map { AuthorId(it) })
        }
    }

}
