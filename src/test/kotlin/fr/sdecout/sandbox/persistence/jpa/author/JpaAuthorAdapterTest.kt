package fr.sdecout.sandbox.persistence.jpa.author

import fr.sdecout.sandbox.TestApp
import fr.sdecout.sandbox.rest.CRIME_AND_PUNISHMENT
import fr.sdecout.sandbox.rest.DOSTOEVSKY
import fr.sdecout.sandbox.rest.author.*
import io.kotest.core.NamedTag
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD
import org.testcontainers.containers.PostgreSQLContainer

@ActiveProfiles("jpa")
@SpringBootTest(
    classes = [TestApp::class],
    properties = ["spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true"]
)
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = ["/init_data.sql"])
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = ["/clear_data.sql"])
class JpaAuthorAdapterTest : ShouldSpec() {

    companion object {
        private val DB = PostgreSQLContainer("postgres:15.3")

        @JvmStatic
        @DynamicPropertySource
        fun registerDbProperties(registry: DynamicPropertyRegistry) {
            DB.start()
            registry.apply {
                with(DB) {
                    add("spring.datasource.url") { jdbcUrl }
                    add("spring.datasource.username") { username }
                    add("spring.datasource.password") { password }
                }
            }
        }
    }

    @Autowired
    private lateinit var authorAdapter: JpaAuthorAdapter

    init {
        tags(NamedTag("Integration"))
        extension(SpringExtension)

        should("fail to find unknown author") {
            val id = AuthorId("XXX")

            val author = authorAdapter.findAuthor(id)

            author shouldBe null
        }

        should("find author") {
            val id = AuthorId(DOSTOEVSKY.id!!)

            val author = authorAdapter.findAuthor(id)

            author shouldBe AuthorResponse(
                id = DOSTOEVSKY.id,
                firstName = DOSTOEVSKY.firstName,
                lastName = DOSTOEVSKY.lastName,
                publishedBooks = mutableListOf(CRIME_AND_PUNISHMENT),
            )
        }

        should("search authors matching hint") {
            val hint = "ostoev"

            val authors = authorAdapter.searchAuthors(hint)

            authors shouldBe listOf(
                AuthorSearchResponseItem(DOSTOEVSKY)
            )
        }

        should("add author") {
            val firstName = "Frank"
            val lastName = "Herbert"

            val authorId = authorAdapter.addAuthor(lastName, firstName)

            authorId shouldNotBe null
        }
    }
}
