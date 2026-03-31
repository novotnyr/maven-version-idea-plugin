package com.github.novotnyr.mavenversion

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.components.service
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.SimpleTextAttributes
import org.jetbrains.idea.maven.project.MavenProjectsManager

private const val POM_XML = "pom.xml"

class MavenVersionProjectViewDecorator : ProjectViewNodeDecorator {

    override fun decorate(node: ProjectViewNode<*>, data: PresentationData) {
        if (!isDecorating()) return

        val project = node.project ?: return
        val mavenProjectsManager = MavenProjectsManager.getInstance(project)
        if (!mavenProjectsManager.isMavenizedProject) return

        node.pomXml
            ?.let { mavenProjectsManager.findProject(it) }
            ?.mavenId?.version
            ?.let { version ->
                data.addText("$separator$version", SimpleTextAttributes.GRAY_ATTRIBUTES)
            }
    }

    private fun isDecorating(): Boolean = service<PluginSettings>().state.showVersion

    private val ProjectViewNode<*>.pomXml: VirtualFile?
        get() = virtualFile?.findChild(POM_XML)

    private val separator: String
        get() = when {
            SystemInfo.isWindows -> "  "
            ApplicationInfo.getInstance().build.baselineVersion >= 261 -> " "
            else -> "\t"
        }
}