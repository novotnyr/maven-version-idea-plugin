plugins {
    id("org.jetbrains.intellij") version "1.16.1"
}

group = "com.github.novotnyr"
version = "6-SNAPSHOT"

repositories {
    mavenCentral()
}

intellij {
    version = "2021.3"
    type = "IC"

    plugins = listOf("org.jetbrains.idea.maven")
    updateSinceUntilBuild = false
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild = "213"
        changeNotes = """
            <ul>
                <li>Bugs and improvements</li>
            </ul>
        """.trimIndent()
    }
}
