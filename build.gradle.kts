plugins {
    kotlin("multiplatform") version "1.6.0"
}

group = "me.kgalligan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }
    val linkerArg = when {
        hostOs == "Mac OS X" -> "-L/usr/local/opt/curl/lib -lcurl"
        hostOs == "Linux" -> "-L/usr/lib64 -L/usr/lib/x86_64-linux-gnu -lcurl"
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }


    nativeTarget.compilations.forEach { kotlinNativeCompilation ->
        kotlinNativeCompilation.kotlinOptions.freeCompilerArgs += listOf(
            "-linker-options",
            linkerArg
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
