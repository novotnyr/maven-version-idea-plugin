package com.github.novotnyr.mavenversion;

import com.intellij.ide.projectView.impl.ProjectViewImpl;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowId;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
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
    public void stateChanged(@NotNull ToolWindowManager toolWindowManager) {
        ToolWindow projectView = toolWindowManager.getToolWindow(ToolWindowId.PROJECT_VIEW);
        if (projectView instanceof ToolWindowEx) {
            var projectViewEx = (ToolWindowEx) projectView;
            this.decorateToolWindow(projectViewEx);
        }
    }

    private void decorateToolWindow(ToolWindowEx toolWindow) {
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

    private Optional<ToolWindowEx> getToolWindow() {
        return Optional.of(ToolWindowManagerEx.getInstanceEx(this.project))
                .map(toolWindowManager -> toolWindowManager.getToolWindow(ToolWindowId.PROJECT_VIEW))
                .filter(ToolWindowEx.class::isInstance)
                .map(ToolWindowEx.class::cast);
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
