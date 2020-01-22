plugins {
    kotlin("plugin.jpa")
    kotlin("plugin.allopen")
}

dependencies {
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("org.hibernate:hibernate-core")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

// spingframework.boot plugin will complain if we don't a main method. But, we don't want to disable the plugin
// since the plugin takes care of library versions.
// https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/#managing-dependencies
tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
