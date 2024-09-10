package com.github.anhnh01.chatc0de.ui

import com.github.anhnh01.chatc0de.ChatC0deIcons
import com.github.anhnh01.chatc0de.utils.notify
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Cursor
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.Icon
import javax.swing.JEditorPane

class Message(content: String, isPrompt: Boolean = false) : JBPanel<JBPanel<*>>() {

    private val msgPanel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
        isOpaque = false
    }

    private val copyBtn = JBLabel(ChatC0deIcons.COPY).apply {
        isOpaque = false
        cursor = Cursor(Cursor.HAND_CURSOR)
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                val stringSelection = StringSelection(content)
                val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(stringSelection, null)

                notify(
                    "Copied",
                    "Response copied successfully",
                )
            }
        })
    }

    private val thumbsUpBtn = JBLabel(ChatC0deIcons.THUMBS_SUP).apply {
        isOpaque = false
        cursor = Cursor(Cursor.HAND_CURSOR)
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                notify("Liked", "You have approved this response")
            }
        })
    }

    private val thumbsDownBtn = JBLabel(ChatC0deIcons.THUMBS_DOWN).apply {
        isOpaque = false
        cursor = Cursor(Cursor.HAND_CURSOR)
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                notify("Disliked", "You have disapproved this response")
            }
        })
    }

    private val actionPanel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
        border = JBUI.Borders.empty(JBUI.scale(7), JBUI.scale(7), JBUI.scale(7), JBUI.scale(7))
        isOpaque = false

        val iconPanels = JBPanel<JBPanel<*>>().apply {
            add(copyBtn)
            if (!isPrompt) {
                add(thumbsUpBtn)
                add(thumbsDownBtn)
            }
        }

        add(iconPanels, BorderLayout.WEST)
    }

    private val avatarPanel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
        isOpaque = false
        border = JBUI.Borders.empty(JBUI.scale(7), JBUI.scale(7), JBUI.scale(7), JBUI.scale(7))
        val avatar: Icon = if (isPrompt) ChatC0deIcons.USER else ChatC0deIcons.CHATBOT
        add(JBLabel(avatar), BorderLayout.NORTH)
    }

    init {
        layout = BorderLayout()
        background = if (isPrompt) JBColor(0xF7F7F7, 0x3C3F41) else JBColor(0xEBEBEB, 0x2d2f30)

        val msgArea = JEditorPane().apply {
            contentType = "text/html;charset=UTF-8"
            isOpaque = false
            isEditable = false
            text = content
        }
        msgPanel.add(msgArea, BorderLayout.CENTER)

        add(avatarPanel, BorderLayout.WEST)
        add(msgPanel, BorderLayout.CENTER)
        add(actionPanel, BorderLayout.SOUTH)
    }



}