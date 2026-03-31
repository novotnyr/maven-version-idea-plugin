import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

plugins {
    id("org.jetbrains.intellij.platform") version "2.13.1"
}

group = "com.github.novotnyr"
version = "7-SNAPSHOT"

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

val runIde261 by intellijPlatformTesting.runIde.registering {
    type = IntelliJPlatformType.IntellijIdea
    version = "2026.1"
}