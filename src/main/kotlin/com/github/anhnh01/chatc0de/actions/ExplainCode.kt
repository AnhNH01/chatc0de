package com.github.anhnh01.chatc0de.actions

import com.github.anhnh01.chatc0de.services.MyProjectService
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service

class ExplainCode: BaseAction() {

    override fun actionPerformed(event: AnActionEvent) {
        super.actionPerformed(event)

        val project = this.toolWindow.project
        val service =project.service<MyProjectService>()
        val ui = service.ui

        val editor = event.getRequiredData(CommonDataKeys.EDITOR)
        val caretModel = editor.caretModel
        val selectedText = caretModel.currentCaret.selectedText

        ui.sendMessage("Explain this code snippet\n $selectedText")

    }


}