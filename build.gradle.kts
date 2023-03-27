plugins {
    id("java")
    id("io.spring.dependency-management") version Versions.SPRING_DEPENDENCY_MANAGEMENT_VERSION
    id("org.springframework.boot") version Versions.SPRING_BOOT_VERSION apply false
    jacoco
    `java-library`
}

group = "com.project"
version = "1.0-SNAPSHOT"


allprojects {
    repositories {
        mavenCentral()
    }
}

java.sourceCompatibility = JavaVersion.VERSION_11


dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("org.antlr:antlr4-runtime:${Versions.ANTLR_RUNTIME_VERSION}")
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

subprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "java-library")

    java {
        withSourcesJar()
    }

    dependencies {
        implementation("io.vavr:vavr:${Versions.IO_VAVR_VERSION}")
        testImplementation("org.assertj:assertj-core:${Versions.ASSERTJ_VERSION}")
        testImplementation("org.jeasy:easy-random-core:${Versions.EASY_RANDOM_VERSION}")
        testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT_JUPITER_VERSION}")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT_JUPITER_VERSION}")
        testImplementation("org.junit.jupiter:junit-jupiter-params:${Versions.JUNIT_JUPITER_VERSION}")
    }

    configurations {
        all {
            exclude("org.springframework.boot", "spring-boot-starter-logging")
        }
    }
}