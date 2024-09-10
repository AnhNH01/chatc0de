package com.github.anhnh01.chatc0de.toolWindow

import com.github.anhnh01.chatc0de.ChatC0deIcons
import com.github.anhnh01.chatc0de.services.MyProjectService
import com.github.anhnh01.chatc0de.ui.Message
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.panels.VerticalLayout
import com.intellij.ui.content.ContentFactory
import com.intellij.util.ui.JBUI
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.ScrollPaneConstants


class MyToolWindowFactory : ToolWindowFactory {

    init {
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {

        private val service = toolWindow.project.service<MyProjectService>()
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
                            if (!isSendingMsg) performAction()
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
                performAction()
            }
        }

        private val actionPanel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
            isOpaque = false
            add(sendMessageButton, BorderLayout.EAST)
        }


        fun getContent() = JBPanel<JBPanel<*>>().apply {
            layout = BorderLayout()
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


        private fun performAction() {
            val msgContent = this.textArea.text.trim()
            this.textArea.text = ""
            if (msgContent.isEmpty()) {
                return
            }
            toggleSendBtn()
            addMessage(msgContent, isPrompt = true)

            this.service.getBotResponseMessage(msgContent, this)
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
}
