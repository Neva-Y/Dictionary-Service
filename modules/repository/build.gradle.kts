import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("idea")
    id("java-library")
    id("java-test-fixtures")
    id("org.springframework.boot")
}

dependencies {
    runtimeOnly("org.apache.logging.log4j:log4j-api:${Versions.APACHE_LOGGING_LOG4J_VERSION}")
    runtimeOnly("org.apache.logging.log4j:log4j-core:${Versions.APACHE_LOGGING_LOG4J_VERSION}")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:${Versions.APACHE_LOGGING_LOG4J_VERSION}")

    implementation("org.apache.commons:commons-lang3:${Versions.APACHE_COMMONS_LANG}")
    implementation("org.slf4j:slf4j-api:${Versions.SLF4J_VERSION}")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
