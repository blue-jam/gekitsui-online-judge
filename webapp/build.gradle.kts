plugins {
    kotlin("plugin.spring")
    id("com.moowork.node")
}

dependencies {
    implementation(project(":shared"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.postgresql:postgresql")
    implementation("commons-codec:commons-codec")

    implementation("com.giffing.bucket4j.spring.boot.starter:bucket4j-spring-boot-starter:0.2.0")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.ehcache:ehcache")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
    testImplementation("com.ninja-squad:springmockk:2.0.0")
    testImplementation("com.h2database:h2")
}

node {
    version = "12.x"
    npmVersion = "6.x"
}

tasks {
    npmInstall {
        dependsOn("nodeSetup", "npmSetup")
    }

    named("npm_run_build") {
        dependsOn("npm_run_lint")
    }

    bootJar {
        dependsOn("npm_run_build")
    }
}
