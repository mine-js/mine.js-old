plugins {
    kotlin("jvm") version "1.6.0"
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(16))
        }
    }

    repositories {
        mavenCentral()
    }
}

subprojects {
    dependencies {
        implementation(kotlin("stdlib"))
    }
}