import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("plugin.jpa") version "1.3.61"
	id("org.springframework.boot") version "2.2.2.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
	kotlin("plugin.allopen") version "1.3.61"
}

repositories {
	mavenCentral()
}

allprojects {
	group = "bluejam.hobby"
	version = "0.0.1-SNAPSHOT"
}

tasks.bootJar {
	enabled = false
}

subprojects {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")
	apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
	apply(plugin = "org.jetbrains.kotlin.plugin.allopen")

	java.sourceCompatibility = JavaVersion.VERSION_11

	val developmentOnly by configurations.creating
	configurations {
		runtimeClasspath {
			extendsFrom(developmentOnly)
		}
	}

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
		implementation("org.springframework.boot:spring-boot-starter-security")
		implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		implementation("org.postgresql:postgresql")
		developmentOnly("org.springframework.boot:spring-boot-devtools")
		testImplementation("org.springframework.boot:spring-boot-starter-test") {
			exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		}
		testImplementation("org.springframework.security:spring-security-test")
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "1.8"
		}
	}

	allOpen {
		annotation("javax.persistence.Entity")
		annotation("javax.persistence.Embeddable")
		annotation("javax.persistence.MappedSuperclass")
	}
}

project(":shared") {
	tasks.bootJar {
		enabled = false
	}

	tasks.jar {
		enabled = true
	}
}

project(":webapp") {
	dependencies {
		implementation(project(":shared"))
	}
}
