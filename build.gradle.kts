val jvmVersion = "17"

val liquibaseVersion = "4.23.0"
val kotestVersion = "5.6.2"
val kotestSpringExtensionVersion = "1.1.3"
val kotlinLoggingVersion = "3.0.5"
val testcontainersVersion = "1.18.3"

plugins {
  kotlin("jvm") version "1.9.0"
  kotlin("plugin.spring") version "1.9.0"
  id("org.jetbrains.kotlin.plugin.jpa") version "1.9.0"
  id("org.springframework.boot") version "3.1.2"
  id("io.spring.dependency-management") version "1.1.2"
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

  implementation("org.liquibase", "liquibase-core", liquibaseVersion)
  runtimeOnly("org.postgresql", "postgresql")
  testRuntimeOnly("com.h2database", "h2")

  testImplementation("io.kotest", "kotest-runner-junit5", kotestVersion)
  testImplementation("io.kotest", "kotest-property", kotestVersion)
  testImplementation("io.kotest", "kotest-assertions-core", kotestVersion)

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
