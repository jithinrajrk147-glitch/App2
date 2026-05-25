package com.anxro.app

import android.content.Context
import android.webkit.WebView
import org.json.JSONObject
import java.io.File
import java.net.URL
import java.util.zip.ZipInputStream
import kotlin.concurrent.thread

class UpdateManager(
    private val context: Context,
    private val webView: WebView
) {

    private val versionUrl =
        "https://jithinrajrk147-glitch.github.io/Anxro/app-version.json"

    fun checkUpdate() {

        thread {

            try {

                val jsonText =
                    URL(versionUrl)
                        .readText()

                val json =
                    JSONObject(jsonText)

                val newVersion =
                    json.getInt("version")

                val zipUrl =
                    json.getString("zip_url")

                val prefs =
                    context.getSharedPreferences(
                        "anxro_update",
                        Context.MODE_PRIVATE
                    )

                val currentVersion =
                    prefs.getInt("version", 0)

                if (newVersion > currentVersion) {

                    val zipFile =
                        File(
                            context.cacheDir,
                            "update.zip"
                        )

                    URL(zipUrl).openStream().use { input ->

                        zipFile.outputStream().use {
                            input.copyTo(it)
                        }
                    }

                    val wwwDir =
                        File(
                            context.filesDir,
                            "www"
                        )

                    if (wwwDir.exists()) {

                        wwwDir.deleteRecursively()
                    }

                    wwwDir.mkdirs()

                    val zipInput =
                        ZipInputStream(
                            zipFile.inputStream()
                        )

                    var entry =
                        zipInput.nextEntry

                    while (entry != null) {

                        var fileName =
                            entry.name

                        if (fileName.contains("/")) {

                            val split =
                                fileName.split("/")

                            if (split.size > 1) {

                                fileName =
                                    split.drop(1)
                                        .joinToString("/")
                            }
                        }

                        if (fileName.isNotEmpty()) {

                            val outFile =
                                File(
                                    wwwDir,
                                    fileName
                                )

                            if (entry.isDirectory) {

                                outFile.mkdirs()

                            } else {

                                outFile.parentFile?.mkdirs()

                                outFile.outputStream().use {
                                    zipInput.copyTo(it)
                                }
                            }
                        }

                        zipInput.closeEntry()

                        entry =
                            zipInput.nextEntry
                    }

                    zipInput.close()

                    prefs.edit()
                        .putInt(
                            "version",
                            newVersion
                        )
                        .apply()

                    NotificationHelper(context)
                        .showNotification(
                            "Anxro Updated",
                            "New version installed"
                        )

                    webView.post {

                        webView.clearCache(true)

                        webView.loadUrl(
                            "file://${context.filesDir.absolutePath}/www/index.html?update=${System.currentTimeMillis()}"
                        )
                    }
                }

            } catch (e: Exception) {

                e.printStackTrace()

                NotificationHelper(context)
                    .showNotification(
                        "Update Failed",
                        e.toString()
                    )
            }
        }
    }
}
