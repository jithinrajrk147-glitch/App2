package com.anxro.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.File
import java.util.zip.ZipInputStream

class MainActivity : Activity() {

    private var filePathCallback:
            ValueCallback<Array<Uri>>? = null

    private val FILE_CHOOSER_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
    fun animateLoader() {

    val animator =
        android.animation.ValueAnimator
            .ofFloat(25f, 36f)

    animator.duration = 800

    animator.repeatCount =
        android.animation.ValueAnimator.INFINITE

    animator.repeatMode =
        android.animation.ValueAnimator.REVERSE

    animator.addUpdateListener {

        val value =
            it.animatedValue as Float

        leftLogo.translationX =
            value

        rightLogo.translationX =
            -value
    }

    animator.start()
}

animateLoader()
        super.onCreate(savedInstanceState)
     requestPermissions(
        arrayOf(
          android.Manifest.permission.RECORD_AUDIO,
          android.Manifest.permission.POST_NOTIFICATIONS,
          android.Manifest.permission.READ_EXTERNAL_STORAGE
         ),
        1
       )
        val webView = WebView(this)
        val loadingView =
    layoutInflater.inflate(
        R.layout.loading_view,
        null
    )

val leftLogo =
    loadingView.findViewById<
        android.widget.ImageView
    >(R.id.leftLogo)

val rightLogo =
    loadingView.findViewById<
        android.widget.ImageView
    >(R.id.rightLogo)
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

            webView.settings.databaseEnabled = true

            webView.settings.allowFileAccess = true

            webView.settings.allowContentAccess = true

            webView.settings.allowFileAccessFromFileURLs = true

            webView.settings.allowUniversalAccessFromFileURLs = true

            webView.settings.loadsImagesAutomatically = true

            webView.settings.mediaPlaybackRequiresUserGesture = false

            webView.settings.mixedContentMode =
                WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            webView.webViewClient = WebViewClient()

            webView.webChromeClient =
                object : WebChromeClient() {

                    override fun onShowFileChooser(
                        webView: WebView?,
                        filePathCallback:
                        ValueCallback<Array<Uri>>?,
                        fileChooserParams:
                        FileChooserParams?
                    ): Boolean {

                        this@MainActivity.filePathCallback =
                            filePathCallback

                        val intent = Intent(
                            Intent.ACTION_GET_CONTENT
                        )

                        intent.type = "*/*"

                        startActivityForResult(
                            Intent.createChooser(
                                intent,
                                "Select File"
                            ),
                            FILE_CHOOSER_REQUEST
                        )

                        return true
                    }
                }

            val page =
    intent.getStringExtra("page")
        ?: "index.html"

webView.loadUrl(
    "file://${filesDir.absolutePath}/www/$page"
)

            UpdateManager(this, webView).checkUpdate()

        } catch (e: Exception) {

            val errorWebView = WebView(this)

            setContentView(errorWebView)

            errorWebView.loadData(
                "<h1>ERROR</h1><pre>${e}</pre>",
                "text/html",
                "utf-8"
            )
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == FILE_CHOOSER_REQUEST) {

            if (resultCode == Activity.RESULT_OK) {

                val result =
                    data?.data

                if (result != null) {

                    filePathCallback?.onReceiveValue(
                        arrayOf(result)
                    )
                }

            } else {

                filePathCallback?.onReceiveValue(null)
            }

            filePathCallback = null
        }
    }
}
