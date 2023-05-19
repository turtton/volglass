plugins {
    kotlin("multiplatform") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
    id("org.jmailen.kotlinter") version "3.14.0"
}

group = "net.turtton"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.turtton.net")
}

kotlin {
    jvm {
        compilations {
            all {
                kotlinOptions.jvmTarget = "1.8"
            }
        }
    }
    js {
        nodejs {
            testTask {
                useMocha()
            }
        }
        binaries.library()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains:markdown:0.4.1")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.554"))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

                implementation("io.github.xxfast:kstore:0.6.0")?.version?.also {
                    implementation("io.github.xxfast:kstore-file:$it")
                }

                // I'm waiting for supporting to ESM package support!!!!!!!!!!
                // implementation(npm("refractor", "4.8.1"))
                // implementation(npm("hast-util-to-html", "8.0.4"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.github.mysticfall:kotlin-react-test:18.2.0-pre.522+build.19")
            }
        }
        // For converting JavaFlex to Kotlin
        val jvmMain by getting
    }
}
