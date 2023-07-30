package fr.sdecout.sandbox.rest.author

import fr.sdecout.sandbox.TestApp
import fr.sdecout.sandbox.rest.CRIME_AND_PUNISHMENT
import fr.sdecout.sandbox.rest.DOSTOEVSKY
import io.kotest.core.NamedTag
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.LOCATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest(classes = [TestApp::class])
@AutoConfigureMockMvc
class AuthorControllerTest : ShouldSpec() {

    @MockBean
    private lateinit var authorAccess: AuthorAccess
    @MockBean
    private lateinit var authorSearch: AuthorSearch
    @MockBean
    private lateinit var authorCreation: AuthorCreation

    @Autowired
    private lateinit var mockMvc: MockMvc

    init {
        tags(NamedTag("Integration"))
        extension(SpringExtension)

        should("fail to find unknown author") {
            mockMvc.perform(get("/authors/{id}", "XXX"))
                .andExpect(status().isNotFound())
        }

        should("find author") {
            val expectedResponseBody = """{
                "id": "${DOSTOEVSKY.id}",
                "lastName": "${DOSTOEVSKY.lastName}",
                "firstName": "${DOSTOEVSKY.firstName}",
                "publishedBooks": [
                    {
                        "isbn": "${CRIME_AND_PUNISHMENT.isbn}",
                        "title": "${CRIME_AND_PUNISHMENT.title}",
                        "authors": [
                            {
                                "id":"${DOSTOEVSKY.id}",
                                "lastName": "${DOSTOEVSKY.lastName}",
                                "firstName": "${DOSTOEVSKY.firstName}"
                            }
                        ]
                    }
                ]
            }""".trimIndent()
            given(authorAccess.findAuthor(AuthorId(DOSTOEVSKY.id!!))).willReturn(
                AuthorResponse(
                    id = DOSTOEVSKY.id,
                    lastName = DOSTOEVSKY.lastName,
                    firstName = DOSTOEVSKY.firstName,
                    publishedBooks = listOf(CRIME_AND_PUNISHMENT)
                )
            )

            mockMvc.perform(get("/authors/{id}", DOSTOEVSKY.id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody))
        }

        should("search authors matching hint") {
            val hint = "ostoev"
            val expectedResponseBody = """[{
                "id":"${DOSTOEVSKY.id}",
                "lastName": "${DOSTOEVSKY.lastName}",
                "firstName": "${DOSTOEVSKY.firstName}"
            }]""".trimIndent()
            given(authorSearch.searchAuthors(hint)).willReturn(listOf(
                AuthorSearchResponseItem(DOSTOEVSKY)
            ))

            mockMvc.perform(post("/authors/search")
                    .queryParam("hint", hint))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody))
        }

        should("add author") {
            val lastName = "Herbert"
            val firstName = "Frank"
            val requestBody = """{
                "lastName": "$lastName",
                "firstName": "$firstName"
            }""".trimMargin()
            val newAuthorId = AuthorId("123")
            given(authorCreation.addAuthor(lastName, firstName)).willReturn(newAuthorId)

            mockMvc.perform(post("/authors")
                    .contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/authors/${newAuthorId.value}"))
        }
    }

}
