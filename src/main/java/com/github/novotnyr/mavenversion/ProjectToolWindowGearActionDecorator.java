package com.github.novotnyr.mavenversion;

import com.intellij.ide.projectView.impl.ProjectViewImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ex.ToolWindowManagerAdapter;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.openapi.wm.impl.ToolWindowImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectToolWindowGearActionDecorator extends ToolWindowManagerAdapter implements ProjectComponent {
    private final Project project;

    public ProjectToolWindowGearActionDecorator(Project project) {
        this.project = project;
    }

    @Override
    public void stateChanged() {
        getToolWindow().ifPresent(this::decorateToolWindow);
    }

    @Override
    public void projectOpened() {
        getToolWindowManager().addToolWindowManagerListener(this);
    }

    @Override
    public void projectClosed() {
        getToolWindowManager().removeToolWindowManagerListener(this);
    }

    private void decorateToolWindow(ToolWindowImpl toolWindow) {
        List<AnAction> actions = getProjectView()
                .map(projectView -> projectView.getActions(false))
                .orElse(new ArrayList<>());

        if (actions.size() > 2) {
            // see com.intellij.ide.projectView.impl.ProjectViewImpl.getActions
            // ignore 'Change View' and separator
            actions = new ArrayList<>(actions.subList(2, actions.size()));
        }
        actions.add(createToggleMavenVersionAction());
        toolWindow.setAdditionalGearActions(new DefaultActionGroup(actions));
    }

    private ToolWindowManagerEx getToolWindowManager() {
        return ToolWindowManagerEx.getInstanceEx(this.project);
    }

    private Optional<ProjectViewImpl> getProjectView() {
        return Optional.of(ProjectViewImpl.getInstance(this.project))
                .filter(ProjectViewImpl.class::isInstance)
                .map(ProjectViewImpl.class::cast)
                .filter(projectView -> projectView.getContentManager() != null);
    }

    private Optional<ToolWindowImpl> getToolWindow() {
        ToolWindowManagerEx toolWindowManager = ToolWindowManagerEx.getInstanceEx(this.project);
        return Optional.of(toolWindowManager)
                .map(ToolWindowManagerEx::getLastActiveToolWindowId)
                .filter("Project"::equals)
                .map(toolWindowManager::getToolWindow)
                .filter(ToolWindowImpl.class::isInstance)
                .map(ToolWindowImpl.class::cast);
    }

    @NotNull
    private ToggleShowMavenSettingsAction createToggleMavenVersionAction() {
        ToggleShowMavenSettingsAction action = new ToggleShowMavenSettingsAction();
        action.setOnUpdateListener(this::refreshProjectView);
        return action;
    }

    private void refreshProjectView() {
        getProjectView().ifPresent(ProjectViewImpl::refresh);
    }
}
