package com.github.anhnh01.chatc0de.ui.textarea

import com.github.anhnh01.chatc0de.ChatC0deIcons
import com.github.anhnh01.chatc0de.ui.IconActionButton
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.JBUI
import java.awt.BasicStroke
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Insets
import java.awt.RenderingHints

class PromptPanel(project: Project, val onSubmit: (String) -> Unit, val onClear: () -> Unit) :
    JBPanel<JBPanel<*>>(BorderLayout()) {

    var isSendingMsg = false

    private val promptTextField = PromptTextField(project, onSubmit)

    private val text: String
        get() = promptTextField.text

    private val submitButton = IconActionButton(
        object : AnAction(
            "Send Message",
            "Send message",
            ChatC0deIcons.SEND
        ) {
            override fun actionPerformed(e: AnActionEvent) {
                handleSubmit(text)
                promptTextField.clear()
            }
        }
    )

    private val clearConversationButton = IconActionButton(
        object : AnAction(
            "Clear Conversation",
            "Clear conversation",
            ChatC0deIcons.REFRESH
        ) {
            override fun actionPerformed(e: AnActionEvent) {
                onClear()
            }
        }
    )


    init {
        isOpaque = false
        add(promptTextField, BorderLayout.CENTER)
        add(footer(), BorderLayout.SOUTH)
    }

    private fun footer(): JBPanel<JBPanel<*>> {
        val panel = JBPanel<JBPanel<*>>().apply {
            layout = FlowLayout(FlowLayout.RIGHT)
            add(submitButton)
            add(clearConversationButton)
        }
        return panel
    }

    override fun paintComponent(g: Graphics) {
        val g2 = g.create() as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = background
        g2.fillRoundRect(0, 0, width - 1, height - 1, 16, 16)
        super.paintComponent(g)
        g2.dispose()
    }

    override fun paintBorder(g: Graphics) {
        val g2 = g.create() as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = JBUI.CurrentTheme.ActionButton.focusedBorder()
        if (promptTextField.isFocusOwner) {
            g2.stroke = BasicStroke(1.5F)
        }
        g2.stroke = BasicStroke(1.5F)
        g2.drawRoundRect(0, 0, width - 1, height - 1, 16, 16)
        g2.dispose()
    }

    override fun getInsets(): Insets? {
        return JBUI.insets(4)
    }

    fun handleSubmit(msg: String) {
        if (msg.isBlank()) return
        if (!isSendingMsg) onSubmit(msg)
    }

    fun toggleSendButton() {
        submitButton.isEnabled = !submitButton.isEnabled
    }
}