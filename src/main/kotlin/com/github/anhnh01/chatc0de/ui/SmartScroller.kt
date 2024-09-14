package com.github.anhnh01.chatc0de.ui

import java.awt.event.AdjustmentEvent
import java.awt.event.AdjustmentListener
import javax.swing.JScrollBar
import javax.swing.JScrollPane
import javax.swing.SwingUtilities
import javax.swing.text.DefaultCaret
import javax.swing.text.JTextComponent

/**
 * SmartScroller taken from http://tips4java.wordpress.com/2013/03/03/smart-scrolling/
 *
 *
 * The SmartScroller will attempt to keep the viewport positioned based on the users interaction
 * with the scrollbar. The normal behaviour is to keep the viewport positioned to see new data as it
 * is dynamically added.
 *
 *
 * Assuming vertical scrolling and data is added to the bottom:
 *
 *
 * - when the viewport is at the bottom and new data is added, then automatically scroll the
 * viewport to the bottom
 * - when the viewport is not at the bottom and new data is added, then do nothing with the viewport
 *
 *
 * Assuming vertical scrolling and data is added to the top:
 *
 *
 * - when the viewport is at the top and new data is added, then do nothing with the viewport
 * - when the viewport is not at the top and new data is added, then adjust the viewport to the
 * relative position it was at before the data was added
 *
 *
 * Similar logic would apply for horizontal scrolling.
 */
class SmartScroller(
    scrollPane: JScrollPane,
    scrollDirection: Int = VERTICAL,
    viewportPosition: Int = END
) :
    AdjustmentListener {
    private val viewportPosition: Int

    private var scrollBar: JScrollBar
    private var adjustScrollBar = true

    private var previousValue = -1
    private var previousMaximum = -1

    /**
     * Convenience constructor. Scroll direction is VERTICAL.
     *
     * @param scrollPane       the scroll pane to monitor
     * @param viewportPosition valid values are START and END
     */
    constructor(scrollPane: JScrollPane, viewportPosition: Int) : this(scrollPane, VERTICAL, viewportPosition)

    /**
     * Specify how the SmartScroller will function.
     *
     * @param scrollPane       the scroll pane to monitor
     * @param scrollDirection  indicates which JScrollBar to monitor. Valid values are HORIZONTAL and
     * VERTICAL.
     * @param viewportPosition indicates where the viewport will normally be positioned as data is
     * added. Valid values are START and END
     */
    /**
     * Convenience constructor. Scroll direction is VERTICAL and viewport position is at the END.
     *
     * @param scrollPane the scroll pane to monitor
     */
    init {
        require(
            !(scrollDirection != HORIZONTAL
                    && scrollDirection != VERTICAL)
        ) { "invalid scroll direction specified" }

        require(
            !(viewportPosition != START
                    && viewportPosition != END)
        ) { "invalid viewport position specified" }

        this.viewportPosition = viewportPosition

        scrollBar = if (scrollDirection == HORIZONTAL) {
            scrollPane.horizontalScrollBar
        } else {
            scrollPane.verticalScrollBar
        }

        scrollBar.addAdjustmentListener(this)

        //  Turn off automatic scrolling for text components
        val view = scrollPane.viewport.view

        if (view is JTextComponent) {
            val caret = view.caret as DefaultCaret
            caret.updatePolicy = DefaultCaret.NEVER_UPDATE
        }
    }

    override fun adjustmentValueChanged(e: AdjustmentEvent) {
        SwingUtilities.invokeLater { checkScrollBar(e) }
    }

    /*
   *  Analyze every adjustment event to determine when the viewport
   *  needs to be repositioned.
   */
    private fun checkScrollBar(e: AdjustmentEvent) {
        //  The scroll bar listModel contains information needed to determine
        //  whether the viewport should be repositioned or not.

        val scrollBar = e.source as JScrollBar
        val listModel = scrollBar.model
        var value = listModel.value
        val extent = listModel.extent
        val maximum = listModel.maximum

        val valueChanged = previousValue != value
        val maximumChanged = previousMaximum != maximum

        //  Check if the user has manually repositioned the scrollbar
        if (valueChanged && !maximumChanged) {
            adjustScrollBar = if (viewportPosition == START) {
                value != 0
            } else {
                value + extent >= maximum
            }
        }

        //  Reset the "value" so we can reposition the viewport and
        //  distinguish between a user scroll and a program scroll.
        //  (i.e. valueChanged will be false on a program scroll)
        if (adjustScrollBar && viewportPosition == END) {
            //  Scroll the viewport to the end.
            scrollBar.removeAdjustmentListener(this)
            value = maximum - extent
            scrollBar.value = value
            scrollBar.addAdjustmentListener(this)
        }

        if (adjustScrollBar && viewportPosition == START) {
            //  Keep the viewport at the same relative viewportPosition
            scrollBar.removeAdjustmentListener(this)
            value = value + maximum - previousMaximum
            scrollBar.value = value
            scrollBar.addAdjustmentListener(this)
        }

        previousValue = value
        previousMaximum = maximum
    }

    companion object {
        const val HORIZONTAL: Int = 0
        const val VERTICAL: Int = 1

        const val START: Int = 0
        const val END: Int = 1
    }
}