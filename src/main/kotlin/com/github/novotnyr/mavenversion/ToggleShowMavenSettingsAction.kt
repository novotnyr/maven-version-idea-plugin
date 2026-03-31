package com.github.novotnyr.mavenversion

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

class ToggleShowMavenSettingsAction : ToggleAction() {
    override fun isSelected(e: AnActionEvent): Boolean {
        return service<PluginSettings>().state.showVersion
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        service<PluginSettings>().state.showVersion = state
        e.project.projectView?.refresh()
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    private val Project?.projectView: ProjectView?
        get() = this?.let { ProjectView.getInstance(it) }
}