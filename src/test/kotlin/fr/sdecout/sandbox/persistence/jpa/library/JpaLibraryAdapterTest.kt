package fr.sdecout.sandbox.persistence.jpa.library

import fr.sdecout.sandbox.TestApp
import fr.sdecout.sandbox.rest.BNF
import fr.sdecout.sandbox.rest.LES_SENS_DU_VOTE
import fr.sdecout.sandbox.rest.book.BookAccess
import fr.sdecout.sandbox.rest.book.Isbn
import fr.sdecout.sandbox.rest.library.*
import fr.sdecout.sandbox.rest.shared.AddressField
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
class JpaLibraryAdapterTest : ShouldSpec() {

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
    private lateinit var libraryAdapter: JpaLibraryAdapter

    @Autowired
    private lateinit var bookAccess: BookAccess

    init {
        tags(NamedTag("Integration"))
        extension(SpringExtension)

        should("search libraries closest to postal code") {
            val postalCode = PostalCode("75000")

            val author = libraryAdapter.searchLibrariesClosestTo(postalCode)

            author shouldBe listOf(LibrarySearchResponseItem(BNF))
        }

        should("add library") {
            val name = "Médiathèque de Châteaudun"
            val address = AddressField(
                line1 = "36, boulevard Grindelle",
                postalCode = "28200",
                city = "Châteaudun",
            )

            val authorId = libraryAdapter.addLibrary(name, address)

            authorId shouldNotBe null
        }

        should("add book") {
            val libraryId = LibraryId(BNF.id!!)
            val isbn = Isbn(LES_SENS_DU_VOTE.isbn!!)
            bookAccess.findBook(isbn)?.availability shouldBe listOf()

            libraryAdapter.addBook(libraryId, isbn)

            bookAccess.findBook(isbn)?.availability shouldBe listOf(BNF)
        }
    }
}
