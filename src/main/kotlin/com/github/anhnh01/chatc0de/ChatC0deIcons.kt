package com.github.anhnh01.chatc0de

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

class ChatC0deIcons {
    companion object {
        val SEND: Icon = IconLoader.getIcon("/icons/send.svg", Companion::class.java)
        val USER: Icon = IconLoader.getIcon("/icons/user.svg", Companion::class.java)
        val CHATBOT: Icon = IconLoader.getIcon("/icons/chatbot.svg", Companion::class.java)
        val COPY : Icon = IconLoader.getIcon("/icons/copy.svg", Companion::class.java)
        val THUMBS_UP: Icon = IconLoader.getIcon("/icons/thumbsup.svg", Companion::class.java)
        val THUMBS_DOWN: Icon = IconLoader.getIcon("/icons/thumbsdown.svg", Companion::class.java)
    }
}