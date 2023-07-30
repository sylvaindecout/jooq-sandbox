import nu.studer.gradle.jooq.JooqEdition
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Nullability
import org.jooq.meta.jaxb.Property

val jvmVersion = "17"

val jooqLiquibaseVersion = "3.18.5"
val kotestVersion = "5.6.2"
val kotestSpringExtensionVersion = "1.1.3"
val kotestTestcontainersExtensionVersion = "2.0.2"
val kotlinLoggingVersion = "3.0.5"
val liquibaseVersion = "4.23.0"
val testcontainersVersion = "1.18.3"

plugins {
  kotlin("jvm") version "1.9.0"
  kotlin("plugin.spring") version "1.9.0"
  id("org.jetbrains.kotlin.plugin.jpa") version "1.9.0"
  id("org.springframework.boot") version "3.1.2"
  id("io.spring.dependency-management") version "1.1.2"
  id("nu.studer.jooq") version "8.2.1"
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))

  implementation("io.github.microutils", "kotlin-logging", kotlinLoggingVersion)
  implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")

  implementation("org.springframework.boot", "spring-boot-starter-web")
  implementation("org.springframework.boot", "spring-boot-starter-validation")
  implementation("org.springframework.boot", "spring-boot-starter-actuator")
  implementation("org.springframework.boot", "spring-boot-starter-data-jpa")
  implementation("org.springframework.boot", "spring-boot-starter-jooq")

  implementation("org.liquibase", "liquibase-core", liquibaseVersion)
  runtimeOnly("org.postgresql", "postgresql")
  testRuntimeOnly("com.h2database", "h2")

  implementation("org.jooq", "jooq-meta-extensions-liquibase", jooqLiquibaseVersion)
  implementation("org.jooq", "jooq-kotlin")
  jooqGenerator("org.jooq", "jooq-meta-extensions-liquibase")
  jooqGenerator("org.liquibase", "liquibase-core", liquibaseVersion)
  jooqGenerator("org.yaml", "snakeyaml")
  jooqGenerator("org.postgresql", "postgresql")

  testImplementation("io.kotest", "kotest-runner-junit5", kotestVersion)
  testImplementation("io.kotest", "kotest-property", kotestVersion)
  testImplementation("io.kotest", "kotest-assertions-core", kotestVersion)

  testImplementation(platform("org.testcontainers:testcontainers-bom:$testcontainersVersion"))
  testImplementation("org.testcontainers", "junit-jupiter")
  testImplementation("org.testcontainers", "postgresql")
  testImplementation("io.kotest.extensions", "kotest-extensions-testcontainers", kotestTestcontainersExtensionVersion)

  testImplementation("org.springframework.boot", "spring-boot-starter-test") {
    exclude("org.junit.vintage", "junit-vintage-engine")
  }
  testImplementation("io.kotest.extensions", "kotest-extensions-spring", kotestSpringExtensionVersion)
}

tasks {
  compileKotlin {
    kotlinOptions {
      jvmTarget = jvmVersion
    }
  }
  compileTestKotlin {
    kotlinOptions{
      jvmTarget = jvmVersion
    }
  }
  withType<Test>().configureEach {
    useJUnitPlatform()
  }
  bootJar {
    destinationDirectory.set(file(".batect/sandbox-service/"))
  }
}

springBoot {
  mainClass.set("fr.sdecout.sandbox.AppKt")
}

jooq {
  version.set(dependencyManagement.importedProperties["jooq.version"])
  edition.set(JooqEdition.OSS)

  configurations {
    create("main") {
      generateSchemaSourceOnCompilation.set(true)

      jooqConfiguration.apply {
        logging = Logging.WARN
        generator.apply {
          name = "org.jooq.codegen.KotlinGenerator"
          database.apply {
            name = "org.jooq.meta.extensions.liquibase.LiquibaseDatabase"
            properties.add(Property().withKey("rootPath").withValue("$projectDir/src/main/resources"))
            properties.add(Property().withKey("scripts").withValue("/db/changelog/db.changelog-master.yaml"))
            properties.add(Property().withKey("includeLiquibaseTables").withValue("false"))
            forcedTypes = listOf(
              ForcedType().apply {
                userType = "fr.sdecout.sandbox.rest.author.AuthorId"
                converter = "fr.sdecout.sandbox.persistence.jooq.shared.AuthorIdConverter"
                includeExpression = "AUTHOR.ID|BOOK_AUTHOR.AUTHOR"
                nullability = Nullability.NOT_NULL
              },
              ForcedType().apply {
                userType = "fr.sdecout.sandbox.rest.book.Isbn"
                converter = "fr.sdecout.sandbox.persistence.jooq.shared.IsbnConverter"
                includeExpression = "BOOK.ISBN|BOOK_AUTHOR.BOOK|LIBRARY_BOOK.BOOK"
                nullability = Nullability.NOT_NULL
              },
              ForcedType().apply {
                userType = "fr.sdecout.sandbox.rest.library.LibraryId"
                converter = "fr.sdecout.sandbox.persistence.jooq.shared.LibraryIdConverter"
                includeExpression = "LIBRARY.ID|LIBRARY_BOOK.LIBRARY"
                nullability = Nullability.NOT_NULL
              },
              ForcedType().apply {
                userType = "fr.sdecout.sandbox.rest.library.PostalCode"
                converter = "fr.sdecout.sandbox.persistence.jooq.shared.PostalCodeConverter"
                includeExpression = "LIBRARY.POSTAL_CODE"
                nullability = Nullability.NOT_NULL
              },
            )
          }
          target.apply {
            packageName = "fr.sdecout.sandbox.persistence.jooq"
            directory = "src/generated/jooq"
          }
        }
      }
    }
  }
}
