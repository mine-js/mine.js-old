plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories {
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    implementation("com.eclipsesource.j2v8:j2v8:6.2.0")
    implementation("com.eclipsesource.j2v8:j2v8_win32_x86_64:4.6.0")
    implementation("com.eclipsesource.j2v8:j2v8_linux_x86_64:4.6.0")
    implementation("com.eclipsesource.j2v8:j2v8_macosx_x86_64:4.6.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "16"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "16"
    }

    create<Jar>("sourceJar") {
        archiveClassifier.set("source")
        from(sourceSets["main"].allSource)
    }
}