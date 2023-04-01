plugins {
    id("idea")
    id("java")
    id("java-library")
    id("java-test-fixtures")
    id("org.springframework.boot")
}

repositories {
    mavenCentral()
    maven("https://www.jetbrains.com/intellij-repository/releases")
    maven ("https://jetbrains.bintray.com/intellij-third-party-dependencies")
}

dependencies {
    runtimeOnly("org.apache.logging.log4j:log4j-api:${Versions.APACHE_LOGGING_LOG4J_VERSION}")
    runtimeOnly("org.apache.logging.log4j:log4j-core:${Versions.APACHE_LOGGING_LOG4J_VERSION}")
    runtimeOnly("org.apache.logging.log4j:log4j-layout-template-json:${Versions.APACHE_LOGGING_LOG4J_VERSION}")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:${Versions.APACHE_LOGGING_LOG4J_VERSION}")

    implementation(project(":modules:repository"))
    implementation("org.jeasy:easy-random-core:${Versions.EASY_RANDOM_VERSION}")
    implementation("org.slf4j:slf4j-api:${Versions.SLF4J_VERSION}")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework:spring-context:${Versions.SPRING_CONTEXT_VERSION}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${Versions.JACKSON_VERSION}")

    compileOnly("com.jetbrains.intellij.java:java-gui-forms-rt:203.7148.30")
}

springBoot {
    mainClass.set("com.project.application.server.DictionaryServer")
}

tasks {
    bootJar {
        archiveClassifier.set("boot")
    }
}
