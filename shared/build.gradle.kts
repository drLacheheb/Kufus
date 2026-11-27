import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

abstract class GenerateAppBuildConfigTask : DefaultTask() {
    @get:Input
    abstract val appVersion: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val targetFile =
            outputDir
                .file("io/github/drlacheheb/kufus/AppBuildConfig.kt")
                .get()
                .asFile
        targetFile.parentFile.mkdirs()
        val escapedVersion = appVersion.get().replace("\\", "\\\\").replace("\"", "\\\"")
        targetFile.writeText(
            """
            package io.github.drlacheheb.kufus

            object AppBuildConfig {
                const val VERSION: String = "$escapedVersion"
            }
            """.trimIndent() + "\n",
        )
    }
}

val releaseVersionProvider =
    providers
        .gradleProperty("kufus.releaseVersion")
        .orElse(libs.versions.app.version)

val generateAppBuildConfig by tasks.registering(GenerateAppBuildConfigTask::class) {
    appVersion.set(releaseVersionProvider)
    outputDir.set(layout.buildDirectory.dir("generated/kufusVersion/kotlin"))
}

kotlin {
    jvmToolchain(21)

    compilerOptions {
        allWarningsAsErrors.set(true)
    }

    jvm {
        compilerOptions {
            allWarningsAsErrors.set(true)
        }
    }

    sourceSets {
        commonMain {
            kotlin.srcDir(generateAppBuildConfig.flatMap { it.outputDir })
        }
        commonMain.dependencies {
            api(project(":disk-core"))

            // Compose Multiplatform & Lifecycle
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Decompose Navigation
            api(libs.decompose.core)
            api(libs.decompose.compose)

            // Dependency Injection (Koin)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Serialization & Datetime
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutinesTest)
            implementation(libs.turbine)
            implementation(libs.kotest.assertions)
        }
        jvmTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.drlacheheb.kufus.resources"
}

val jvmTestCompilation = kotlin.jvm().compilations.getByName("test")

tasks.register<JavaExec>("generateScreenshots") {
    group = "documentation"
    description = "Renders high-DPI headless Compose screenshots into docs/screenshots/"
    dependsOn(jvmTestCompilation.compileTaskProvider)
    classpath = jvmTestCompilation.output.allOutputs + jvmTestCompilation.runtimeDependencyFiles
    mainClass.set("io.github.drlacheheb.kufus.ui.flasher.ScreenshotGeneratorKt")
    args(
        rootProject.layout.projectDirectory
            .dir("docs/screenshots")
            .asFile
            .absolutePath,
    )
}
