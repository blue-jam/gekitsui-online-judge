import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("plugin.jpa") version "1.3.61"
	id("org.springframework.boot") version "2.2.2.RELEASE" apply false
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
	kotlin("plugin.allopen") version "1.3.61"
    id("jacoco")
}

repositories {
	mavenCentral()
}

allprojects {
	group = "bluejam.hobby"
	version = "0.0.1-SNAPSHOT"
}

subprojects {
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "jacoco")

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
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		developmentOnly("org.springframework.boot:spring-boot-devtools")
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
}

task("codeCoverageReport", JacocoReport::class) {
	executionData(fileTree(project.rootDir.absoluteFile).include("*/build/jacoco/*.exec"))

	subprojects.forEach { project ->
		sourceSets(project.sourceSets["main"])
	}

	reports {
		xml.isEnabled = true
        xml.destination = File("${buildDir}/reports/jacoco/report.xml")
		csv.isEnabled = false
		html.isEnabled = true
	}

	dependsOn(
			subprojects.map { project -> project.tasks.withType<Test>() }
	)
}
