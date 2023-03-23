plugins {
    kotlin("js") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
    id("org.jmailen.kotlinter") version "3.14.0"
}

group = "net.turtton"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.turtton.net")
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.github.mysticfall:kotlin-react-test:18.2.0-pre.346+build.17")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.346")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.2.0-pre.346")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.9.3-pre.346")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.3.0-pre.346")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains:markdown:0.4.1")

    implementation("io.github.xxfast:kstore:0.3.0")

    // I'm waiting for supporting to ESM package support!!!!!!!!!!
    // implementation(npm("refractor", "4.8.1"))
    // implementation(npm("hast-util-to-html", "8.0.4"))
}

kotlin {
    js {
        nodejs {
            testTask {
                useMocha()
            }
        }
        binaries.library()
    }
}
