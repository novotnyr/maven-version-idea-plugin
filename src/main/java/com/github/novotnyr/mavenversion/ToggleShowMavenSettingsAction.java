package com.github.novotnyr.mavenversion;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ToggleShowMavenSettingsAction extends ToggleAction {
    private Runnable onUpdateListener = () -> {};

    public ToggleShowMavenSettingsAction() {
        super("Show Maven Version in Modules");
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        return PluginSettings.getInstance().isShowVersion();
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        PluginSettings.getInstance().setShowVersion(state);
        this.onUpdateListener.run();
    }

    public void setOnUpdateListener(Runnable onUpdateListener) {
        this.onUpdateListener = Objects.requireNonNull(onUpdateListener);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}

