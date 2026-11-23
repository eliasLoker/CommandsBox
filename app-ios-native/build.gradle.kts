import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.moko.resources)
    alias(libs.plugins.metro)
}

val commonResourcesDirectory = rootProject.layout.projectDirectory
    .dir("common-resources/src/commonMain/composeResources")
val generatedMokoResourcesDirectory = layout.buildDirectory.dir("generated/mokoResources")
val generatedMokoAssetsDirectory = layout.buildDirectory
    .dir("generated/moko-resources/iosSimulatorArm64Main/assets")

val mokoStringsLocales = mapOf(
    "values" to "base",
    "values-de" to "de",
    "values-es" to "es",
    "values-fr" to "fr",
    "values-hi" to "hi",
    "values-id" to "id",
    "values-ja" to "ja",
    "values-ko" to "ko",
    "values-pt" to "pt",
    "values-ru" to "ru",
)

val mokoVectorImages = listOf(
    "ic_add_square",
    "ic_arrow_right",
    "ic_arrow_up",
    "ic_arrow_up_with_tail",
    "ic_clear",
    "ic_command",
    "ic_copy",
    "ic_delete",
    "ic_details",
    "ic_edit",
    "ic_failure",
    "ic_settings",
    "ic_success",
    "ic_tab",
)

val mokoLogoScales = mapOf(
    1 to 180,
    2 to 360,
    3 to 540,
)

fun androidAttribute(element: Element, name: String): String? =
    element.getAttributeNS("http://schemas.android.com/apk/res/android", name)
        .takeIf { it.isNotEmpty() }

fun xmlAttribute(value: String): String = value
    .replace("&", "&amp;")
    .replace("\"", "&quot;")
    .replace("<", "&lt;")
    .replace(">", "&gt;")

fun vectorXmlToSvg(file: File): String {
    val factory = DocumentBuilderFactory.newInstance().apply {
        isNamespaceAware = true
    }
    val document = factory.newDocumentBuilder().parse(file)
    val vector = document.documentElement
    val unsupportedElements = (0 until vector.childNodes.length)
        .map { vector.childNodes.item(it) }
        .filterIsInstance<Element>()
        .filter { it.tagName != "path" }
    require(unsupportedElements.isEmpty()) {
        "Unsupported vector elements in ${file.path}: ${unsupportedElements.map { it.tagName }}"
    }

    val width = androidAttribute(vector, "width")?.removeSuffix("dp")
    val height = androidAttribute(vector, "height")?.removeSuffix("dp")
    val viewportWidth = androidAttribute(vector, "viewportWidth") ?: width
    val viewportHeight = androidAttribute(vector, "viewportHeight") ?: height
    require(width != null && height != null && viewportWidth != null && viewportHeight != null) {
        "Vector drawable ${file.path} is missing dimensions"
    }

    val pathElements = document.getElementsByTagName("path")
    val paths = buildList {
        for (index in 0 until pathElements.length) {
            val path = pathElements.item(index) as Element
            val fillAlpha = androidAttribute(path, "fillAlpha")?.toFloatOrNull()
            if (fillAlpha != null && fillAlpha == 0f) continue

            val pathData = androidAttribute(path, "pathData") ?: continue
            val fillColor = androidAttribute(path, "fillColor")
            val strokeColor = androidAttribute(path, "strokeColor")
            val attributes = mutableListOf("d=\"${xmlAttribute(pathData)}\"")

            when (fillColor) {
                null, "#00000000" -> attributes += "fill=\"none\""
                else -> attributes += "fill=\"${xmlAttribute(fillColor)}\""
            }
            fillAlpha?.let { attributes += "fill-opacity=\"$it\"" }
            androidAttribute(path, "fillType")?.let { fillType ->
                if (fillType == "evenOdd") attributes += "fill-rule=\"evenodd\""
            }
            strokeColor?.let { attributes += "stroke=\"${xmlAttribute(it)}\"" }
            androidAttribute(path, "strokeWidth")?.let { attributes += "stroke-width=\"$it\"" }
            androidAttribute(path, "strokeAlpha")?.let { attributes += "stroke-opacity=\"$it\"" }
            androidAttribute(path, "strokeLineCap")?.let { attributes += "stroke-linecap=\"$it\"" }
            androidAttribute(path, "strokeLineJoin")?.let { attributes += "stroke-linejoin=\"$it\"" }

            add("  <path ${attributes.joinToString(" ")}/>")
        }
    }

    return buildString {
        appendLine("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"$width\" height=\"$height\" viewBox=\"0 0 $viewportWidth $viewportHeight\">")
        paths.forEach(::appendLine)
        appendLine("</svg>")
    }
}

fun convertLogoToPng(source: File, target: File, size: Int) {
    val process = if (System.getProperty("os.name").contains("Mac", ignoreCase = true)) {
        ProcessBuilder(
            "sips",
            "-s",
            "format",
            "png",
            "--resampleHeightWidth",
            size.toString(),
            size.toString(),
            source.absolutePath,
            "--out",
            target.absolutePath,
        )
    } else {
        ProcessBuilder(
            "python3",
            "-c",
            """
                from PIL import Image
                import sys

                image = Image.open(sys.argv[1]).convert("RGBA")
                image = image.resize((int(sys.argv[2]), int(sys.argv[2])), Image.Resampling.LANCZOS)
                image.save(sys.argv[3], format="PNG")
            """.trimIndent(),
            source.absolutePath,
            size.toString(),
            target.absolutePath,
        )
    }
        .redirectErrorStream(true)
        .start()

    val output = process.inputStream.bufferedReader().use { it.readText() }
    check(process.waitFor() == 0) {
        if (System.getProperty("os.name").contains("Mac", ignoreCase = true)) {
            "Failed to convert shared logo to PNG:\n$output"
        } else {
            "Failed to convert shared logo to PNG. Non-macOS builds require Python 3 with Pillow installed:\n$output"
        }
    }
}

val syncMokoResources = tasks.register("syncMokoResources") {
    group = "moko-resources"
    description = "Generates Moko resource inputs from :common-resources Compose resources."

    inputs.files(
        fileTree(commonResourcesDirectory) {
            mokoStringsLocales.keys.forEach { include("$it/strings.xml") }
            mokoVectorImages.forEach { include("drawable/$it.xml") }
            include("drawable-xxxhdpi/commandsbox_logo_transparent.webp")
            include("font/CascadiaCodeItalic.ttf")
        }
    )
    outputs.dir(generatedMokoResourcesDirectory)

    doLast {
        val sourceDirectory = commonResourcesDirectory.asFile
        val outputDirectory = generatedMokoResourcesDirectory.get().asFile
        outputDirectory.deleteRecursively()
        outputDirectory.mkdirs()

        mokoStringsLocales.forEach { (sourceLocale, targetLocale) ->
            val source = File(sourceDirectory, "$sourceLocale/strings.xml")
            require(source.isFile) { "Missing shared strings: ${source.path}" }
            val target = File(outputDirectory, "$targetLocale/strings.xml")
            target.parentFile.mkdirs()
            source.copyTo(target, overwrite = true)
        }

        val imagesDirectory = File(outputDirectory, "images").apply { mkdirs() }
        mokoVectorImages.forEach { name ->
            val source = File(sourceDirectory, "drawable/$name.xml")
            require(source.isFile) { "Missing shared vector: ${source.path}" }
            File(imagesDirectory, "$name.svg").writeText(vectorXmlToSvg(source))
        }

        val logoSources = listOf(
            File(sourceDirectory, "drawable-xxxhdpi/commandsbox_logo_transparent.webp"),
            File(sourceDirectory, "drawable-xxhdpi/commandsbox_logo_transparent.webp"),
            File(sourceDirectory, "drawable-xhdpi/commandsbox_logo_transparent.webp"),
            File(sourceDirectory, "drawable-hdpi/commandsbox_logo_transparent.webp"),
            File(sourceDirectory, "drawable-mdpi/commandsbox_logo_transparent.webp"),
        )
        val logoScaledSource = logoSources.firstOrNull { it.isFile }
        require(logoScaledSource != null) { "Missing shared logo" }
        mokoLogoScales.forEach { (scale, size) ->
            val target = File(imagesDirectory, "commandsbox_logo_transparent@${scale}x.png")
            convertLogoToPng(logoScaledSource, target, size)
        }
        val fontSource = File(sourceDirectory, "font/CascadiaCodeItalic.ttf")
        require(fontSource.isFile) { "Missing shared font: ${fontSource.path}" }
        val fontsDirectory = File(outputDirectory, "fonts").apply { mkdirs() }
        fontSource.copyTo(File(fontsDirectory, fontSource.name), overwrite = true)
    }
}

multiplatformResources {
    resourcesPackage.set("com.magictools.commandsbox.appiosnative.resources")
    resourcesClassName.set("MR")
    iosBaseLocalizationRegion.set("en")
    iosMinimalDeploymentTarget.set("15.0")

    resourcesSourceSets.configureEach {
        if (name == "iosSimulatorArm64Main") {
            setSrcDirs(listOf(generatedMokoResourcesDirectory.get().asFile))
        }
    }
}

tasks.withType<dev.icerock.gradle.tasks.GenerateMultiplatformResourcesTask>().configureEach {
    dependsOn(syncMokoResources)
}

fun developerDirectory(): String? = System.getenv("DEVELOPER_DIR")
    ?: File("/Applications/Xcode.app/Contents/Developer")
        .takeIf { it.isDirectory }
        ?.absolutePath

tasks.configureEach {
    if (name == "compileKotlinIosSimulatorArm64") {
        doFirst {
            val assetsDirectory = generatedMokoAssetsDirectory.get().asFile
            val assetCatalog = File(assetsDirectory, "Assets.xcassets")
            if (assetCatalog.isDirectory) {
                val process = ProcessBuilder(
                    "xcrun",
                    "actool",
                    assetCatalog.name,
                    "--compile",
                    ".",
                    "--platform",
                    "iphoneos",
                    "--minimum-deployment-target",
                    "15.0",
                )
                    .directory(assetsDirectory)
                    .redirectErrorStream(true)
                    .apply {
                        developerDirectory()?.let { environment()["DEVELOPER_DIR"] = it }
                    }
                    .start()
                val output = process.inputStream.bufferedReader().use { it.readText() }
                check(process.waitFor() == 0) { "Failed to compile Moko assets:\n$output" }
                assetCatalog.deleteRecursively()
            }
        }
    }
}

kotlin {
    iosSimulatorArm64 {
        binaries.framework {
            baseName = "NativeApp"
            isStatic = true

            export(project(":common-presentation"))
            export(project(":multi-mvi"))
            export(project(":data"))
            export("dev.icerock.moko:resources:${libs.versions.moko.resources.get()}")
        }
    }

    sourceSets {
        val iosSimulatorArm64Main by getting {
            dependencies {
                api(project(":common-presentation"))
                api(project(":multi-mvi"))
                api(project(":data"))
                api(libs.moko.resources)

                implementation(project(":common-resources"))

                implementation(libs.androidx.lifecycle.viewmodel.savedstate)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.compose.resources)
                implementation(libs.metrox.viewmodel)
            }
        }
    }
}
