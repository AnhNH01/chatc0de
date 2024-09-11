package com.github.anhnh01.chatc0de.ui

import com.github.anhnh01.chatc0de.ChatC0deIcons
import com.github.anhnh01.chatc0de.services.MyProjectService
import com.intellij.ui.JBColor
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.panels.VerticalLayout
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.BorderFactory
import javax.swing.JButton
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

    private val textArea = JBTextArea(5, 20).apply {

        emptyText.setText("Ask anything...")
        lineWrap = true
        wrapStyleWord = true
        border = BorderFactory.createEmptyBorder(0, 10, 0, 10)
        autoscrolls = true
        background = JBColor(0xFFFFFF, 0x303134)
        foreground = JBColor(0x303134, 0xFFFFFF)
        caretColor = JBColor(0x303134, 0xFFFFFF)

        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_ENTER) {
                    if (e.isShiftDown)
                        insert("\n", caretPosition)
                    else {
                        e.consume()

                        // Only allow for one message at one time, user have to wait for the bot to respond before
                        // sending another message
                        if (!isSendingMsg) sendMessage(text.trim())
                    }
                } else {
                    super.keyPressed(e)
                }

            }
        })
    }
    private val sendMessageButton = JButton("Send", ChatC0deIcons.SEND).apply {
        isOpaque = false
        addActionListener {
            sendMessage(textArea.text)
        }
    }

    private val actionPanel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
        isOpaque = false
        add(sendMessageButton, BorderLayout.EAST)
    }

    init {
        val splitter = OnePixelSplitter(true, .98f)
        splitter.dividerWidth = 2

        add(splitter)
        splitter.firstComponent = listMsgScrollPane

        val inputPanel = JBPanel<JBPanel<*>>().apply {
            layout = BorderLayout()
            background = JBColor(0xFFFFFF, 0x303134)
            border = JBUI.Borders.customLineTop(JBUI.CurrentTheme.Editor.BORDER_COLOR)
            isOpaque = true
            val textPane = JBScrollPane(textArea).apply {
                border = null
            }

            add(textPane, BorderLayout.CENTER)
            add(actionPanel, BorderLayout.SOUTH)
        }

        add(inputPanel, BorderLayout.SOUTH)
    }


    fun sendMessage(msg: String) {
        val msgContent = msg.trim()
        this.textArea.text = ""
        if (msgContent.isEmpty() || isSendingMsg) {
            return
        }
        toggleSendBtn()
        addMessage(msgContent, true)
        this.service.getBotResponseMessage(msgContent)
    }

    fun addMessage(msgContent: String, isPrompt: Boolean = false) {
        listMsg.add(Message(msgContent, isPrompt))
        listMsg.revalidate()
        listMsg.repaint()
    }

    fun toggleSendBtn() {
        this.textArea.isEnabled = !this.textArea.isEnabled
        this.isSendingMsg = !this.isSendingMsg
        this.sendMessageButton.isEnabled = !this.sendMessageButton.isEnabled
    }
}