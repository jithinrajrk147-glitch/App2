package com.anxro.app

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.util.zip.ZipInputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val webView = WebView(this)

        setContentView(webView)

        try {

            val wwwDir = File(filesDir, "www")

            if (!wwwDir.exists()) {

                val inputStream = assets.open("www.zip")

                val zipInput = ZipInputStream(inputStream)

                var entry = zipInput.nextEntry

                while (entry != null) {

                    val file = File(filesDir, "www/${entry.name}")

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

            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.settings.allowFileAccess = true

            webView.webViewClient = WebViewClient()

            webView.loadUrl(
                "file://${filesDir.absolutePath}/www/index.html"
            )

        } catch (e: Exception) {

            e.printStackTrace()

            webView.loadData(
                "<h1>Crash</h1><pre>${e.message}</pre>",
                "text/html",
                "utf-8"
            )
        }
    }
}
