package com.github.anhnh01.chatc0de.ui

import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBTextArea
import java.awt.BorderLayout
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import javax.swing.JButton
import javax.swing.JEditorPane
import javax.swing.border.Border

enum class AGENT(val label: String) {
    CHAT_BOT("VPBank bot"),
    USER("You"),
}

class Message(content: String, isPrompt: Boolean = false) : JBPanel<JBPanel<*>>() {

    private val labelPanel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
        isOpaque = false
    }
    private val msgPanel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
        isOpaque = false
    }

    init {
        layout = BorderLayout()
        background = if (isPrompt) JBColor(0xfaadaa, 0x33345) else JBColor(JBColor.DARK_GRAY, JBColor.DARK_GRAY)

        val agent = if (isPrompt) AGENT.USER.label else AGENT.CHAT_BOT.label
        val msgLabel = JBLabel(agent)
        labelPanel.add(msgLabel, BorderLayout.WEST)

        val msgArea = JEditorPane().apply {
            contentType = "text/html;charset=UTF-8"
            isOpaque = false
            isEditable = false
            text = content
        }
        msgPanel.add(msgArea, BorderLayout.CENTER)

        val copyBtn = JButton("Copy").apply {
            addActionListener {
                val stringSelection = StringSelection(content)
                val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(stringSelection, null)
            }
        }

        labelPanel.add(copyBtn, BorderLayout.EAST)

        add(labelPanel, BorderLayout.NORTH)
        add(msgPanel, BorderLayout.CENTER)

    }

}