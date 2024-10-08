package com.github.anhnh01.chatc0de.services

import com.github.anhnh01.chatc0de.ui.ChatPanel
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

@Service(Service.Level.PROJECT)
class MyProjectService(val project: Project) {
    private val httpClient = OkHttpClient()
    val ui = ChatPanel(this)

    private fun getCatInfo(): String {
        val request = Request.Builder().url("https://catfact.ninja/fact").build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Request failed $response")

            val respBody = response.body!!.string()

            return respBody

        }
    }


    fun getBotResponseMessage(msg: String) {
        ApplicationManager.getApplication().executeOnPooledThread {

            val result = try {
                getCatInfo()
            } catch (e: Exception) {
                thisLogger().debug(e)
                "Something wrong happened :( Try again?"
            }

            ApplicationManager.getApplication().invokeLater {
                ui.addMessage(result)
            }
        }
    }
}
