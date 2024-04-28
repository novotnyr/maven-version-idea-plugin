package com.github.novotnyr.mavenversion

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.SimpleTextAttributes
import org.jetbrains.idea.maven.project.MavenProject
import org.jetbrains.idea.maven.project.MavenProjectsManager

class MavenVersionProjectViewDecorator : ProjectViewNodeDecorator {
    override fun decorate(node: ProjectViewNode<*>, data: PresentationData) {
        if (!isDecorating) return
        val nodeProject = node.project ?: return
        val projectManager = MavenProjectsManager.getInstance(nodeProject) ?: return
        val pomXml = node.pomXml ?: return
        val mavenProject = projectManager.findProject(pomXml) ?: return
        mavenProject.version?.let { data.version = it }
    }

    private val isDecorating: Boolean
        get() = PluginSettings.getInstance().isShowVersion

    private var PresentationData.version: String
        get() = ""
        set(version) {
            val separator = if (SystemInfo.isWindows) "  " else "\t"
            addText(separator + version, SimpleTextAttributes.GRAY_ATTRIBUTES)
        }

    private val MavenProject.version: String?
        get() = mavenId.version

    private val ProjectViewNode<*>.pomXml: VirtualFile?
        get() = virtualFile?.findChild("pom.xml")
}
