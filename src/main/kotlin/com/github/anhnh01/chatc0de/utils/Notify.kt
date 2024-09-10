package com.github.anhnh01.chatc0de.utils

import com.github.anhnh01.chatc0de.MyBundle
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications

fun notify(title: String, content: String, type: NotificationType = NotificationType.INFORMATION) {
    Notifications.Bus.notify(
        Notification(
            MyBundle.message("shuffle"),
            title,
            content,
            type,
        )
    )
}