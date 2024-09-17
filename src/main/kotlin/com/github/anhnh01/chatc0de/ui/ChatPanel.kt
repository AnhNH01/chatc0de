package com.github.anhnh01.chatc0de.ui

import com.github.anhnh01.chatc0de.services.MyProjectService
import com.github.anhnh01.chatc0de.ui.textarea.PromptPanel
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.panels.VerticalLayout
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.ScrollPaneConstants

class ChatPanel(private val service: MyProjectService) : JBPanel<JBPanel<*>>(BorderLayout()) {


    private var isSendingMsg = false

    private val listMsg = JBPanel<JBPanel<*>>().apply {
        layout = VerticalLayout(JBUI.scale(8))
        border = BorderFactory.createEmptyBorder(5, 10, 5, 10)
    }

    private val listMsgScrollPane = JBScrollPane(
        listMsg,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
    ).apply {
        border = null
        verticalScrollBar.autoscrolls = true
    }

    val promptPanel = PromptPanel(service.project, ::sendMessage, ::clearConversation)

    init {
        val splitter = OnePixelSplitter(true, .98f)
        splitter.dividerWidth = 2

        add(splitter)
        splitter.firstComponent = listMsgScrollPane

        add(promptPanel, BorderLayout.SOUTH)
    }


    fun sendMessage(msg: String) {
        val msgContent = msg.trim()
        if (msgContent.isEmpty() || isSendingMsg) {
            return
        }
        addMessage(msgContent, true)
        this.service.getBotResponseMessage(msgContent)
    }

    fun addMessage(msgContent: String, isPrompt: Boolean = false) {
        listMsg.add(Message(msgContent, isPrompt))
        listMsg.revalidate()
        listMsg.repaint()
    }

    fun clearConversation() {
        listMsg.removeAll()
        listMsg.revalidate()
        listMsg.repaint()
    }
    // function to stream text messages like chat gpt

}