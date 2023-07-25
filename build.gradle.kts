val jvmVersion = "17"

val kotestVersion = "5.6.2"
val kotlinLoggingVersion = "3.0.5"

plugins {
  kotlin("jvm") version "1.9.0"
  kotlin("plugin.spring") version "1.9.0"
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

  testImplementation("io.kotest", "kotest-runner-junit5", kotestVersion)
  testImplementation("io.kotest", "kotest-property", kotestVersion)
  testImplementation("io.kotest", "kotest-assertions-core", kotestVersion)

  testImplementation("org.springframework.boot", "spring-boot-starter-test") {
    exclude("org.junit.vintage", "junit-vintage-engine")
  }
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
}

springBoot {
  mainClass.set("fr.sdecout.sandbox.AppKt")
}
