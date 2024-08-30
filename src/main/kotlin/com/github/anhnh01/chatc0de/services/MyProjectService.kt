package com.github.anhnh01.chatc0de.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.github.anhnh01.chatc0de.MyBundle
import com.github.anhnh01.chatc0de.toolWindow.MyToolWindowFactory
import com.intellij.openapi.application.ApplicationManager
import kotlinx.serialization.json.JsonObject
import okhttp3.*
import okio.IOException
import org.jetbrains.io.response

@Service(Service.Level.PROJECT)
class MyProjectService(project: Project) {

    private val httpClient = OkHttpClient()
    init {
        thisLogger().info(MyBundle.message("projectService", project.name))
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }


    private fun getCatInfo(): String {
        val request = Request.Builder().url("https://catfact.ninja/fact").build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Request failed $response")

            val respBody = response.body!!.string()

            return respBody

        }
    }


    fun performActionThreaded(msg: String, ui: MyToolWindowFactory.MyToolWindow) {
        ApplicationManager.getApplication().executeOnPooledThread {
            val rsp = getCatInfo()
            ApplicationManager.getApplication().invokeLater {
                ui.addMessage(rsp)
                ui.toggleSendBtn()
            }
        }
    }
    fun getRandomNumber() = (1..100).random()

    fun performAction(msg: String): String {
        thisLogger().info("msg received $msg")
        val rs = getCatInfo()
        return rs
    }
}
