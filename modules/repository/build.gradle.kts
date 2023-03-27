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

    implementation("io.vavr:vavr:${Versions.IO_VAVR_VERSION}")
    implementation("org.flywaydb:flyway-core:${Versions.FLYWAY_DB_VERSION}")
    implementation("org.postgresql:postgresql:${Versions.POSTGRES_DRIVER_VERSION}")
    implementation("org.slf4j:slf4j-api:${Versions.SLF4J_VERSION}")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
