package com.github.novotnyr.mavenversion;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "mavenversion", storages = @Storage("mavenversion.xml"))
public class PluginSettings implements PersistentStateComponent<PluginSettings> {
    public static PluginSettings getInstance() {
        return ServiceManager.getService(PluginSettings.class);
    }

    private boolean showVersion = true;

    @Nullable
    @Override
    public PluginSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PluginSettings state) {
        this.showVersion = state.showVersion;
    }

    public boolean isShowVersion() {
        return showVersion;
    }

    public void setShowVersion(boolean showVersion) {
        this.showVersion = showVersion;
    }
}
