plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvm()
    androidLibrary {
        namespace = "com.magictools.commandsbox.commonResources"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        androidResources {
            enable = true
        }
    }
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.compose.resources)
                implementation(libs.compose.runtime)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.magictools.commandsbox.resources"
    publicResClass = true
}