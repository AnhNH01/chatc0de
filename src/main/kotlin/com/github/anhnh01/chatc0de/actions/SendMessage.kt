package com.github.anhnh01.chatc0de.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.DumbAwareAction
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.Icon

class SendMessage(text: String) : AbstractAction() {

    init {

    }

    override fun actionPerformed(e: ActionEvent?) {
        TODO("Not yet implemented")
        thisLogger().debug("SendMessageAction")

    }
}