package com.github.novotnyr.mavenversion;

import com.intellij.ide.projectView.impl.ProjectViewImpl;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.openapi.wm.impl.ToolWindowImpl;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectToolWindowGearActionDecorator implements ToolWindowManagerListener, ProjectComponent, Disposable {
    private final Project project;
    private final MessageBusConnection projectBusConnection;

    public ProjectToolWindowGearActionDecorator(Project project) {
        this.project = project;
        this.projectBusConnection = this.project.getMessageBus().connect();
        this.projectBusConnection.subscribe(ToolWindowManagerListener.TOPIC, this);
    }

    @Override
    public void stateChanged() {
        getToolWindow().ifPresent(this::decorateToolWindow);
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

    @Override
    public void dispose() {
        this.projectBusConnection.disconnect();
    }
}
