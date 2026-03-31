package com.github.novotnyr.mavenversion

import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service

@Service
@State(name = "mavenversion", storages = [Storage("mavenversion.xml")])
class PluginSettings : SimplePersistentStateComponent<PluginSettings.State>(State()) {
    class State : BaseState() {
        var showVersion by property(true)
    }

    companion object {
        private val instance: PluginSettings
            get() = service<PluginSettings>()

        var showVersion: Boolean
            get() = instance.state.showVersion
            set(value) {
                instance.state.showVersion = value
            }
    }
}