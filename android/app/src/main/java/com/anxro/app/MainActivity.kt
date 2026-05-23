package com.anxro.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.util.zip.ZipInputStream

class MainActivity : AppCompatActivity() {

    lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)

        setContentView(webView)

        val wwwDir = File(filesDir, "www")

        if (!wwwDir.exists()) {
            unzipAssets()
        }

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.settings.mediaPlaybackRequiresUserGesture = false

        webView.webViewClient = WebViewClient()

        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                request?.grant(request.resources)
            }
        }

        val path =
            "file://${filesDir.absolutePath}/www/index.html"

        webView.loadUrl(path)

        UpdateManager(this, webView).checkUpdate()
    }

    private fun unzipAssets() {

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
}
