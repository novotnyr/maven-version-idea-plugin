package com.github.novotnyr.mavenversion

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.ActionUpdateThread.BGT
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.project.Project
import java.util.*

class ToggleShowMavenSettingsAction : ToggleAction() {
    override fun isSelected(e: AnActionEvent): Boolean {
        return PluginSettings.getInstance().isShowVersion
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        PluginSettings.getInstance().isShowVersion = state
        e.project.projectView?.refresh()
    }

    override fun getActionUpdateThread() = BGT

    private val Project?.projectView: ProjectView?
        get() = this?.let { ProjectView.getInstance(it) }
}

