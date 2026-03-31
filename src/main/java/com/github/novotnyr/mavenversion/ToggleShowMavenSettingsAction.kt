package com.github.novotnyr.mavenversion

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import java.util.*

class ToggleShowMavenSettingsAction(private val onUpdate: Runnable) : ToggleAction("Show Maven Version in Modules") {
    override fun isSelected(e: AnActionEvent): Boolean {
        return PluginSettings.getInstance().isShowVersion
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        PluginSettings.getInstance().isShowVersion = state
        onUpdate.run()
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}

