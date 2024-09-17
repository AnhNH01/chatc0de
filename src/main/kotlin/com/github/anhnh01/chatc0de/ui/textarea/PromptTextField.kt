package com.github.anhnh01.chatc0de.ui.textarea

import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.runInEdt
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.fileTypes.FileTypes
import com.intellij.openapi.project.Project
import com.intellij.ui.ComponentUtil.findParentByCondition
import com.intellij.ui.EditorTextField
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import com.jetbrains.rd.util.UUID
import java.awt.AWTEvent
import java.awt.Dimension
import java.awt.KeyboardFocusManager
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent

class PromptTextField(project: Project, val onSubmit: (String) -> Unit) :
    EditorTextField(project, FileTypes.PLAIN_TEXT), Disposable {

    val dispatcherId: UUID = UUID.randomUUID()

    init {
        isOneLineMode = false
        background = JBColor.background()
        minimumSize = Dimension(100, 50)
        setPlaceholder("Ask chatbot anything...")

        IdeEventQueue.getInstance().addDispatcher(
            PromptTextFieldEventDispatcher(this) {
                handleSubmit(text)
            }, this
        )
    }

    fun handleSubmit(msg: String) {
        onSubmit(msg)
        clear()
    }

    fun clear() {
        runInEdt {
            text = ""
        }
    }

    override fun dispose() {
        clear()
    }

    override fun updateBorder(editor: EditorEx) {
        editor.setBorder(JBUI.Borders.empty(4, 8))

    }
}

class PromptTextFieldEventDispatcher(private val textField: PromptTextField, private val onSubmit: () -> Unit) :
    IdeEventQueue.EventDispatcher {


    override fun dispatch(e: AWTEvent): Boolean {
        val owner =
            findParentByCondition(KeyboardFocusManager.getCurrentKeyboardFocusManager().focusOwner) { component ->
                component is PromptTextField
            }

        if ((e is KeyEvent || e is MouseEvent)
            && owner is PromptTextField
            && owner.dispatcherId == textField.dispatcherId
        ) {
            if (e is KeyEvent) {
                if (e.id == KeyEvent.KEY_PRESSED) {
                    when (e.keyCode) {
                        KeyEvent.VK_BACK_SPACE -> {
//                            if (textField.text.let { it.isNotEmpty() && it.last() == AT_CHAR }) {
//                                suggestionsPopupManager.reset()
//                            }

//                            val appliedInlay = appliedInlays.find {
//                                it.inlay.offset == owner.caretModel.offset - 1
//                            }
//                            if (appliedInlay != null) {
//                                appliedInlay.inlay.dispose()
//                                appliedInlays.remove(appliedInlay)
//                            }
                        }

                        KeyEvent.VK_TAB -> {
//                            selectNextSuggestion(e)
                        }

                        KeyEvent.VK_ENTER -> {
                            if (e.modifiersEx and InputEvent.SHIFT_DOWN_MASK == 0
                                && e.modifiersEx and InputEvent.ALT_DOWN_MASK == 0
                                && e.modifiersEx and InputEvent.CTRL_DOWN_MASK == 0
                            ) {
                                onSubmit()
                                e.consume()
                            }
                        }

//                        KeyEvent.VK_UP -> selectPreviousSuggestion(e)
//                        KeyEvent.VK_DOWN -> selectNextSuggestion(e)
                    }
//                    when (e.keyChar) {
//                        AT_CHAR -> showPopup(e)
//                        else -> {
//                            if (suggestionsPopupManager.isPopupVisible()) {
//                                updateSuggestions()
//                            }
//                        }
//                    }
                }
                return e.isConsumed
            }
        }
        return false
    }
}