plugins {
    id("org.jetbrains.intellij") version "1.12.0"
}

group = "com.github.novotnyr"
version = "3-SNAPSHOT"

repositories {
    mavenCentral()
}

intellij {
    version.set("2021.3")
    type.set("IC")

    plugins.set(listOf("org.jetbrains.idea.maven"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild.set("213")
    }
}
