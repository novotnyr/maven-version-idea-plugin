plugins {
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.intellij.platform") version "2.0.0-beta8"
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
        intellijIdeaCommunity("2022.3")
        bundledPlugins(
            "org.jetbrains.idea.maven",
            "org.jetbrains.idea.maven.model",
            "org.jetbrains.idea.maven.server.api"
        )
        pluginVerifier()
        instrumentationTools()
    }
}


intellijPlatform {
    instrumentCode = false
    buildSearchableOptions = false

    pluginConfiguration {
        ideaVersion {
            sinceBuild = "223"
            untilBuild = "241.*"
        }
        changeNotes = """
            <ul>
            <li>Improve compatibility with the recent Platform versions</li>
            </ul>
        """.trimIndent()
    }

    verifyPlugin {
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