package com.github.anhnh01.chatc0de.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager

open class BaseAction : AnAction() {


    lateinit var toolWindow: ToolWindow;

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("MyToolWindow")
        this.toolWindow = toolWindow ?: return
        if(!toolWindow.isVisible) {
            toolWindow.show()
        }
    }
}