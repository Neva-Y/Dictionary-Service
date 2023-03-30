plugins {
    id("idea")
    id("java-library")
    id("java-test-fixtures")
    id("org.springframework.boot")
}

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("org.apache.logging.log4j:log4j-api:${Versions.APACHE_LOGGING_LOG4J_VERSION}")
    runtimeOnly("org.apache.logging.log4j:log4j-core:${Versions.APACHE_LOGGING_LOG4J_VERSION}")
    runtimeOnly("org.apache.logging.log4j:log4j-layout-template-json:${Versions.APACHE_LOGGING_LOG4J_VERSION}")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:${Versions.APACHE_LOGGING_LOG4J_VERSION}")

    implementation(project(":modules:repository"))
    implementation("org.slf4j:slf4j-api:${Versions.SLF4J_VERSION}")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework:spring-context:${Versions.SPRING_CONTEXT_VERSION}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${Versions.JACKSON_VERSION}")
}

springBoot {
    mainClass.set("com.project.application.DictionaryServiceApplication")
}

tasks {
    bootJar {
        archiveClassifier.set("boot")
    }
}
