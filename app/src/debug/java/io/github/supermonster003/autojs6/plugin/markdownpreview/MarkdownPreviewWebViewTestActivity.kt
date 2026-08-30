package io.github.supermonster003.autojs6.plugin.markdownpreview

import android.app.Activity
import android.os.Bundle
import android.webkit.WebView

class MarkdownPreviewWebViewTestActivity : Activity() {

    lateinit var webView: WebView
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        setContentView(webView)
    }
}
