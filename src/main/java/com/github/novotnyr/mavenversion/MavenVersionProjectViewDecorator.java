package com.github.novotnyr.mavenversion;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ProjectViewNodeDecorator;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.idea.maven.model.MavenId;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

import java.util.Optional;
import java.util.function.Consumer;

public class MavenVersionProjectViewDecorator implements ProjectViewNodeDecorator {
    @Override
    public void decorate(ProjectViewNode<?> node, PresentationData data) {
        Optional.of(node)
                .filter(this::isDecorating)
                .map(ProjectViewNode::getProject)
                .map(MavenProjectsManager::getInstance)
                .filter(MavenProjectsManager::isMavenizedProject)
                .flatMap(mavenProjectsManager -> getPomXml(node)
                        .map(mavenProjectsManager::findProject)
                        .flatMap(MavenVersionProjectViewDecorator.this::getVersion))
                .ifPresent(doDecorate(data));
    }

    private boolean isDecorating(@SuppressWarnings("unused") ProjectViewNode<?> node) {
        return PluginSettings.getInstance().isShowVersion();
    }

    private Consumer<String> doDecorate(PresentationData data) {
        String separator = SystemInfo.isWindows ? "  " : "\t";
        return version -> data.addText(separator + version, SimpleTextAttributes.GRAY_ATTRIBUTES);
    }

    private Optional<String> getVersion(MavenProject mavenProject) {
        return Optional.of(mavenProject.getMavenId())
                .map(MavenId::getVersion);
    }

    private Optional<VirtualFile> getPomXml(ProjectViewNode<?> node) {
        return Optional.ofNullable(node)
                .map(ProjectViewNode::getVirtualFile)
                .map(virtualFile -> virtualFile.findChild("pom.xml"));
    }
}
