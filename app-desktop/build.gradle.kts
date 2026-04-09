plugins {
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvmToolchain(21)
    jvm("desktop")
    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(project(":common-ui"))
                implementation(project(":common-resources"))
                implementation(project(":data"))
                implementation(project(":multi-mvi"))
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)

                implementation(libs.compose.resources)
                implementation(libs.compose.runtime)
                implementation(libs.koin.core)
                implementation(libs.koin.compose.viewmodel)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.magictools.commandsbox.appdesktop.MainKt"

        nativeDistributions {
            modules("java.sql")
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Rpm,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe
            )
            packageName = "CommandsBox"
            packageVersion = libs.versions.appVersionName.get()
            description = "CommandsBox Desktop App"
            copyright = "© 2026 MagicTools"
            vendor = "MagicTools"

            linux {
                shortcut = true
                appCategory = "Utility"
                menuGroup = "Network"
                iconFile.set(project.file("src/desktopMain/resources/images/commands_box_rounded_linux.png"))
            }
            windows {
                menu = true
                menuGroup = "CommandsBox"
                iconFile.set(project.file("src/desktopMain/resources/images/commands_box_rounded_windows.ico"))
                upgradeUuid = "8B2C1A5E-F73D-4D91-A9E6-30B1C8429F25"
                shortcut = true
                dirChooser = true
                packageName = "CommandsBox"
            }
            modules("java.sql", "java.naming", "java.desktop", "jdk.unsupported")
        }
        jvmArgs(
            "-Dapp.versionName=${libs.versions.appVersionName.get()}",
            "-Dapp.versionCode=${libs.versions.appVersionCode.get()}",
        )
    }
}