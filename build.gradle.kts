plugins {
    kotlin("multiplatform") version "1.6.0"
}

group = "me.kgalligan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val tryMac = true
    val nativeTarget = if (tryMac) {
        macosX64("native")
    } else {
        linuxX64("native")
    }

//    nativeTarget.binaries.iterator().forEach { binary ->
//        binary.linkerOpts("-L/opt/local/lib", "-L/usr/local/opt/curl/lib" ,"-lcurl")
//    }
    nativeTarget.compilations.forEach { kotlinNativeCompilation ->
        kotlinNativeCompilation.kotlinOptions.freeCompilerArgs += listOf(
            "-linker-options",
            "-L/usr/local/opt/curl/lib -lcurl"
        )
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:1.6.6")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val nativeMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-curl:1.6.6")
            }
        }
        val nativeTest by getting
    }
}
