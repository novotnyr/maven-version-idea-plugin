plugins {
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.intellij.platform") version "2.0.0-beta1"
}

group = "com.github.novotnyr"
version = "6-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2022.3")
        bundledPlugins("org.jetbrains.idea.maven", "org.jetbrains.idea.maven.model", "org.jetbrains.idea.maven.server.api")
        instrumentationTools()

    }
}


intellijPlatform {
    instrumentCode = false
    buildSearchableOptions = false
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "223"
        }
        changeNotes = """
            <ul>
            <li>Bugs and improvements</li>
            </ul>
        """.trimIndent()
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}
