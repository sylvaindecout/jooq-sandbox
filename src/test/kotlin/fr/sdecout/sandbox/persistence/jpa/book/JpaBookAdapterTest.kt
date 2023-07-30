package fr.sdecout.sandbox.persistence.jpa.book

import fr.sdecout.sandbox.TestApp
import fr.sdecout.sandbox.rest.*
import fr.sdecout.sandbox.rest.author.AuthorId
import fr.sdecout.sandbox.rest.book.*
import io.kotest.core.NamedTag
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
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
class JpaBookAdapterTest : ShouldSpec() {

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
    private lateinit var bookAdapter: JpaBookAdapter

    init {
        tags(NamedTag("Integration"))
        extension(SpringExtension)

        should("fail to find unknown book") {
            val isbn = Isbn("0000000000000")

            val author = bookAdapter.findBook(isbn)

            author shouldBe null
        }

        should("find book") {
            val isbn = Isbn(CRIME_AND_PUNISHMENT.isbn!!)

            val author = bookAdapter.findBook(isbn)

            author shouldBe BookResponse(
                isbn = CRIME_AND_PUNISHMENT.isbn,
                title = CRIME_AND_PUNISHMENT.title,
                authors = CRIME_AND_PUNISHMENT.authors,
                availability = mutableListOf(BNF),
            )
        }

        should("search books matching hint") {
            val hint = "rime and punish"

            val authors = bookAdapter.searchBooks(hint)

            authors shouldBe listOf(
                BookSearchResponseItem(CRIME_AND_PUNISHMENT)
            )
        }

        should("add new book") {
            val isbn = Isbn(THE_IDIOT.isbn!!)
            val title = THE_IDIOT.title!!
            val authors = THE_IDIOT.authors!!

            bookAdapter.save(isbn, title, authors.map{ AuthorId(it.id!!) })

            bookAdapter.findBook(isbn) shouldBe BookResponse(
                isbn = isbn.formattedValue,
                title = title,
                authors = authors,
                availability = mutableListOf(),
            )
        }

        should("update existing book") {
            val isbn = Isbn(CRIME_AND_PUNISHMENT.isbn!!)
            bookAdapter.findBook(isbn) shouldBe BookResponse(
                isbn = CRIME_AND_PUNISHMENT.isbn,
                title = "Crime and Punishment",
                authors = mutableListOf(DOSTOEVSKY),
                availability = mutableListOf(BNF),
            )
            val title = "Crime & Punishment"
            val authors = mutableListOf(DOSTOEVSKY, DANIEL_GAXIE)

            bookAdapter.save(isbn, title, authors.map { AuthorId(it.id!!) })

            bookAdapter.findBook(isbn) shouldBe BookResponse(
                isbn = isbn.formattedValue,
                title = title,
                authors = authors,
                availability = mutableListOf(BNF),
            )
        }
    }
}
