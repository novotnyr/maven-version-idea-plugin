import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

plugins {
    id("org.jetbrains.intellij.platform") version "2.13.1"
    kotlin("jvm") version "2.3.20"
}

group = "com.github.novotnyr"
version = "7-SNAPSHOT"

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.1")
        bundledPlugins(
            "org.jetbrains.idea.maven",
            "org.jetbrains.idea.maven.model",
            "org.jetbrains.idea.maven.server.api",
            "org.jetbrains.idea.reposearch"
        )
    }
}


intellijPlatform {
    instrumentCode = false
    buildSearchableOptions = false

    pluginConfiguration {
        ideaVersion {
            sinceBuild = "241"
        }
        changeNotes = """
            <ul>
            <li>Require at least Platform 2024.1</li>
            <li>Improve compatibility with 2026.1</li>
            </ul>
        """.trimIndent()
    }

    pluginVerification {
        ides {
            recommended()
        }
    }

    publishing {
        val intellijPublishToken: String by project
        token = intellijPublishToken
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

val runIde251 by intellijPlatformTesting.runIde.registering {
    type = IntelliJPlatformType.IntellijIdeaCommunity
    version = "2025.1"
}

val runIde253 by intellijPlatformTesting.runIde.registering {
    type = IntelliJPlatformType.IntellijIdea
    version = "2025.3"
}

val runIde261 by intellijPlatformTesting.runIde.registering {
    type = IntelliJPlatformType.IntellijIdea
    version = "2026.1"
}