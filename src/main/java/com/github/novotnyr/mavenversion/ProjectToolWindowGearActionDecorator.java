package com.github.novotnyr.mavenversion;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowId;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.switcher.QuickActionProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectToolWindowGearActionDecorator implements ToolWindowManagerListener {
    private final Project project;

    public ProjectToolWindowGearActionDecorator(Project project) {
        this.project = project;
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
                .map(this::getDefaultProjectViewActions)
                .orElse(new ArrayList<>());

        if (actions.size() > 2) {
            // see com.intellij.ide.projectView.impl.ProjectViewImpl.getActions
            // ignore 'Change View' and separator
            actions = new ArrayList<>(actions.subList(2, actions.size()));
        }
        actions.add(createToggleMavenVersionAction());
        toolWindow.setAdditionalGearActions(new DefaultActionGroup(actions));
    }

    private List<AnAction> getDefaultProjectViewActions(ProjectView projectView) {
        var defaultActions = new ArrayList<AnAction>();
        if (projectView instanceof QuickActionProvider) {
            var actionProvider = (QuickActionProvider) projectView;
            defaultActions.addAll(actionProvider.getActions(false));
        }
        return defaultActions;
    }


    private Optional<ProjectView> getProjectView() {
        return Optional.ofNullable(ProjectView.getInstance(this.project))
                       .filter(QuickActionProvider.class::isInstance);
    }

    @NotNull
    private ToggleShowMavenSettingsAction createToggleMavenVersionAction() {
        ToggleShowMavenSettingsAction action = new ToggleShowMavenSettingsAction();
        action.setOnUpdateListener(this::refreshProjectView);
        return action;
    }

    private void refreshProjectView() {
        getProjectView().ifPresent(ProjectView::refresh);
    }
}
