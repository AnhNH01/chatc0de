package com.github.anhnh01.chatc0de.ui

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.roots.ui.componentsList.components.ScrollablePanel
import com.intellij.ui.JBColor
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.components.JBTextArea
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UI
import java.awt.CardLayout
import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.net.URISyntaxException
import java.util.*
import javax.swing.*
import javax.swing.event.HyperlinkEvent
import javax.swing.event.HyperlinkListener
import javax.swing.text.DefaultCaret


object UIUtil {
    @JvmOverloads
    fun createTextPane(
        text: String?,
        opaque: Boolean = true,
        listener: HyperlinkListener
    ): JTextPane {
        val textPane = JTextPane()
        textPane.putClientProperty(JTextPane.HONOR_DISPLAY_PROPERTIES, true)
        textPane.addHyperlinkListener(listener)
        textPane.contentType = "text/html"
        textPane.isEditable = false
        textPane.text = text
        textPane.isOpaque = opaque
        (textPane.caret as DefaultCaret).updatePolicy = DefaultCaret.NEVER_UPDATE
        return textPane
    }

    fun createTextArea(initialValue: String?): JBTextArea {
        val textArea = JBTextArea(initialValue)
        textArea.rows = 3
        textArea.border = JBUI.Borders.compound(
            JBUI.Borders.customLine(JBColor.border()),
            JBUI.Borders.empty(4)
        )
        textArea.lineWrap = true
        return textArea
    }

    fun createIconButton(icon: Icon): JButton {
        val button = JButton(icon)
        button.border = BorderFactory.createEmptyBorder()
        button.isContentAreaFilled = false
        button.preferredSize = Dimension(icon.iconWidth, icon.iconHeight)
        return button
    }

    fun createScrollPaneWithSmartScroller(scrollablePanel: ScrollablePanel?): JScrollPane {
        val scrollPane = ScrollPaneFactory.createScrollPane(scrollablePanel, true)
        SmartScroller(scrollPane)
        return scrollPane
    }

    fun setEqualLabelWidths(firstPanel: JPanel, secondPanel: JPanel) {
        val firstLabel = firstPanel.components[0]
        val secondLabel = secondPanel.components[0]
        if (firstLabel is JLabel && secondLabel is JLabel) {
            firstLabel.setPreferredSize(secondLabel.getPreferredSize())
        }
    }

    fun createPanel(component: JComponent?, label: String, resizeX: Boolean): JPanel {
        return UI.PanelFactory.panel(component)
            .withLabel(label)
            .resizeX(resizeX)
            .createPanel()
    }

    fun handleHyperlinkClicked(event: HyperlinkEvent) {
        val url = event.url
        if (HyperlinkEvent.EventType.ACTIVATED == event.eventType && url != null) {
            try {
                BrowserUtil.browse(url.toURI())
            } catch (e: URISyntaxException) {
                throw RuntimeException(e)
            }
        }
    }

    fun addShiftEnterInputMap(textArea: JTextArea, onSubmit: AbstractAction?) {
        textArea.inputMap.put(KeyStroke.getKeyStroke("shift ENTER"), "insert-break")
        textArea.inputMap.put(KeyStroke.getKeyStroke("ENTER"), "text-submit")
        textArea.actionMap.put("text-submit", onSubmit)
    }


    fun createRadioButtonsPanel(radioButtons: List<JBRadioButton?>): JPanel {
        val buttonGroup = ButtonGroup()
        val radioPanel = JPanel()
        radioPanel.layout = BoxLayout(radioPanel, BoxLayout.PAGE_AXIS)
        for (i in radioButtons.indices) {
            val radioButton = radioButtons[i]
            buttonGroup.add(radioButton)
            radioPanel.add(radioButton)
            radioPanel.add(Box.createVerticalStrut(if (i == radioButtons.size - 1) 8 else 4))
        }
        return withEmptyLeftBorder<JPanel>(radioPanel)
    }

    fun <T : JComponent?> withEmptyLeftBorder(component: T): T {
        component!!.border = JBUI.Borders.emptyLeft(16)
        return component
    }


    /**
     * Creates RadioButton group to toggle between different layouts.
     *
     * @param layouts       Map from layout name to RadioButton + Layout to be shown
     * @param initialLayout Key of `layouts` entry to be initially shown
     * @return Panel with the RadioButton group
     */
    fun createRadioButtonGroupLayouts(
        layouts: Map<String, RadioButtonWithLayout>,
        initialLayout: String?
    ): JPanel {
        val cardLayout: CardLayout = object : CardLayout() {
            override fun show(parent: Container, name: String) {
                super.show(parent, name)
                // Set height to selected components height instead of consistent height
                Arrays.stream(parent.components)
                    .filter { component: Component -> name == component.name }
                    .findFirst()
                    .map { component: Component -> component.preferredSize.getHeight().toInt() }
                    .map { height: Int? ->
                        Dimension(
                            parent.preferredSize.width,
                            height!!
                        )
                    }
                    .ifPresent { preferredSize: Dimension? ->
                        parent.preferredSize =
                            preferredSize
                    }
            }
        }

        val formPanelCards = JPanel(cardLayout)
        for ((key, value) in layouts) {
            val component = value.component
            component.name = key
            formPanelCards.add(component, key)
            value.radioButton.addActionListener { e: ActionEvent? -> cardLayout.show(formPanelCards, key) }
        }

        cardLayout.show(formPanelCards, initialLayout)
        return formPanelCards
    }

    class RadioButtonWithLayout(val radioButton: JBRadioButton, val component: Component)
}