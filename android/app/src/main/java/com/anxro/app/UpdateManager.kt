package com.anxro.app

import android.content.Context
import android.webkit.WebView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.net.URL
import java.util.zip.ZipInputStream

class UpdateManager(
    private val context: Context,
    private val webView: WebView
) {

    private val versionUrl =
        "https://jithinrajrk147-glitch.github.io/Anxro/app-version.json"

    fun checkUpdate() {

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val jsonText = URL(versionUrl).readText()

                val json = JSONObject(jsonText)

                val newVersion = json.getInt("version")

                val zipUrl = json.getString("zip_url")

                val prefs =
                    context.getSharedPreferences(
                        "app",
                        Context.MODE_PRIVATE
                    )

                val currentVersion =
                    prefs.getInt("version", 1)

                if (newVersion > currentVersion) {

                    downloadAndReplace(zipUrl)

                    prefs.edit()
                        .putInt("version", newVersion)
                        .apply()
                    NotificationHelper(context)
    .showNotification(
        "Anxro Updated",
        "New version installed"
    )
                    webView.post {
                        webView.reload()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun downloadAndReplace(zipUrl: String) {

        val tempZip =
            File(context.filesDir, "update.zip")

        URL(zipUrl).openStream().use { input ->

            tempZip.outputStream().use {
                input.copyTo(it)
            }
        }

        val wwwDir =
            File(context.filesDir, "www")

        wwwDir.deleteRecursively()

        wwwDir.mkdirs()

        val zipInput =
            ZipInputStream(tempZip.inputStream())

        var entry = zipInput.nextEntry

        while (entry != null) {

            val file = File(wwwDir, entry.name)

            if (entry.isDirectory) {

                file.mkdirs()

            } else {

                file.parentFile?.mkdirs()

                file.outputStream().use {
                    zipInput.copyTo(it)
                }
            }

            zipInput.closeEntry()

            entry = zipInput.nextEntry
        }

        zipInput.close()
    }
}
