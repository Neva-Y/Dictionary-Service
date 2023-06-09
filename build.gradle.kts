plugins {
    id("java")
    id("io.spring.dependency-management") version Versions.SPRING_DEPENDENCY_MANAGEMENT_VERSION
    id("org.springframework.boot") version Versions.SPRING_BOOT_VERSION apply false
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

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("org.antlr:antlr4-runtime:${Versions.ANTLR_RUNTIME_VERSION}")
    }
}


subprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "java-library")

    java {
        withSourcesJar()
    }

    configurations {
        all {
            exclude("org.springframework.boot", "spring-boot-starter-logging")
        }
    }
}
