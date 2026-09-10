@file:Suppress("DEPRECATION")

package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.net.toUri
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import java.io.ByteArrayInputStream
import java.util.Locale

internal const val MAX_FIND_QUERY_LENGTH = 256

internal data class MarkdownPreviewerFindResult(
    val activeMatchOrdinal: Int,
    val matchCount: Int,
    val isDoneCounting: Boolean,
) {
    val activeMatchNumber: Int
        get() = if (matchCount == 0) 0 else (activeMatchOrdinal + 1).coerceIn(1, matchCount)
}

internal class MarkdownPreviewerWebController(
    context: Context,
    private val webView: WebView,
    resourceRoot: Uri,
    private val onExternalLink: (Uri) -> Unit,
    private val onDocumentLink: (MarkdownPreviewerDocumentLink) -> Unit = {},
    initialTextZoomPercent: Int = MarkdownPreviewerTextZoom.DEFAULT_PERCENT,
    private val onPageFinished: () -> Unit = {},
    private val onFindResult: (MarkdownPreviewerFindResult) -> Unit = {},
) {

    private var activeFindQuery: String? = null
    private var findMatchCount = 0
    private var isFindCountingDone = false
    private var activeDocumentRelativePath = MarkdownPreviewerWebOrigin.DOCUMENT_FILE_NAME
    private var activeDocumentUrl = MarkdownPreviewerWebOrigin.DOCUMENT_URL
    private var printAdapterActive = false
    private var destroyed = false

    private val documentPathHandler = MarkdownPreviewerDocumentPathHandler(context.contentResolver, resourceRoot)

    private val assetLoader = WebViewAssetLoader.Builder()
        .setDomain(MarkdownPreviewerWebOrigin.DOMAIN)
        .addPathHandler(
            MarkdownPreviewerWebOrigin.PREVIEWER_ASSET_PATH_PREFIX,
            MarkdownPreviewerAssetPathHandler(context.assets),
        )
        .addPathHandler(
            MarkdownPreviewerWebOrigin.DOCUMENT_PATH_PREFIX,
            documentPathHandler,
        )
        .build()

    init {
        configureSettings(initialTextZoomPercent)
        configureClient()
        configureFindListener()
    }

    private fun configureSettings(initialTextZoomPercent: Int) {
        webView.settings.apply {
            javaScriptEnabled = false
            javaScriptCanOpenWindowsAutomatically = false
            domStorageEnabled = false
            databaseEnabled = false
            setGeolocationEnabled(false)

            allowFileAccess = false
            allowContentAccess = false
            allowFileAccessFromFileURLs = false
            allowUniversalAccessFromFileURLs = false
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            blockNetworkImage = false
            blockNetworkLoads = false

            setSupportMultipleWindows(false)
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            textZoom = MarkdownPreviewerTextZoom.normalize(initialTextZoomPercent)
            mediaPlaybackRequiresUserGesture = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            saveFormData = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                safeBrowsingEnabled = true
            }
        }
        CookieManager.getInstance().setAcceptCookie(false)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, false)
        webView.isVerticalScrollBarEnabled = true
        webView.isHorizontalScrollBarEnabled = false
    }

    private fun configureClient() {
        webView.webViewClient = object : WebViewClientCompat() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest,
            ): WebResourceResponse? {
                val uri = request.url
                if (
                    MarkdownPreviewerRequestPolicy.shouldLetWebViewLoadDataResource(
                        uri.toString(),
                        request.isForMainFrame,
                    )
                ) {
                    return null
                }
                if (uri.scheme.equals("https", ignoreCase = true) && MarkdownPreviewerWebOrigin.isDomain(uri.host)) {
                    return assetLoader.shouldInterceptRequest(uri) ?: forbidden()
                }
                if (
                    MarkdownPreviewerRequestPolicy.shouldLetWebViewLoadHttpsSubresource(
                        uri.toString(),
                        request.isForMainFrame,
                    )
                ) {
                    return null
                }
                return forbidden()
            }

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest,
            ): Boolean = handleNavigationRequest(
                uri = request.url,
                isForMainFrame = request.isForMainFrame,
                hasGesture = request.hasGesture(),
            )

            override fun onPageFinished(view: WebView, url: String) {
                if (isActiveDocumentUrl(url.toUri())) {
                    onPageFinished()
                }
            }
        }
    }

    private fun configureFindListener() {
        webView.setFindListener { activeMatchOrdinal, numberOfMatches, isDoneCounting ->
            if (activeFindQuery == null) return@setFindListener

            val safeMatchCount = numberOfMatches.coerceAtLeast(0)
            val safeActiveOrdinal = if (safeMatchCount == 0) {
                0
            } else {
                activeMatchOrdinal.coerceIn(0, safeMatchCount - 1)
            }
            findMatchCount = safeMatchCount
            isFindCountingDone = isDoneCounting
            onFindResult(
                MarkdownPreviewerFindResult(
                    activeMatchOrdinal = safeActiveOrdinal,
                    matchCount = safeMatchCount,
                    isDoneCounting = isDoneCounting,
                ),
            )
        }
    }

    fun show(
        html: String,
        relativePath: String = MarkdownPreviewerWebOrigin.DOCUMENT_FILE_NAME,
    ) {
        val documentUrl = MarkdownPreviewerDocumentLinkPolicy.buildVirtualUrl(relativePath)
            ?: throw IllegalArgumentException("Unsafe previewer document path")
        activeDocumentRelativePath = relativePath
        activeDocumentUrl = documentUrl
        documentPathHandler.updateDocument(html, relativePath)
        webView.loadUrl(documentUrl)
    }

    fun createPrintDocumentAdapter(
        documentName: String,
        onFinished: () -> Unit,
    ): MarkdownPreviewerPrintDocumentAdapter {
        check(!destroyed) { "Cannot print from a destroyed WebView" }
        check(!printAdapterActive) { "This WebView is already being printed" }
        val safeDocumentName = MarkdownPreviewerPrintPolicy.documentName(documentName)
        printAdapterActive = true
        var visibilityBeforePrinting = webView.visibility
        val delegate = try {
            webView.createPrintDocumentAdapter(safeDocumentName)
        } catch (error: Throwable) {
            printAdapterActive = false
            throw error
        }
        return MarkdownPreviewerPrintDocumentAdapter(
            delegate = delegate,
            onPrintingStarted = {
                visibilityBeforePrinting = webView.visibility
                if (!destroyed) webView.visibility = View.INVISIBLE
            },
            onPrintingFinished = {
                printAdapterActive = false
                if (!destroyed) webView.visibility = visibilityBeforePrinting
                onFinished()
            },
        )
    }

    fun scrollToAnchor(anchorId: String): Boolean {
        if (
            anchorId.isBlank() ||
            !MarkdownPreviewerDocumentLinkPolicy.isSafeFragment(anchorId)
        ) {
            return false
        }
        val anchorUrl = MarkdownPreviewerDocumentLinkPolicy.buildVirtualUrl(
            relativePath = activeDocumentRelativePath,
            fragment = anchorId,
        ) ?: return false
        webView.loadUrl(anchorUrl)
        return true
    }

    internal fun handleNavigationRequest(
        uri: Uri,
        isForMainFrame: Boolean,
        hasGesture: Boolean,
    ): Boolean {
        if (!isForMainFrame) return true
        if (isLocalAnchor(uri)) return false
        val documentLink = MarkdownPreviewerDocumentLinkPolicy.parseVirtualUrl(uri.toString())
        if (documentLink != null) {
            if (hasGesture) onDocumentLink(documentLink)
            return true
        }
        if (MarkdownPreviewerWebOrigin.isDomain(uri.host)) return true
        if (hasGesture && uri.scheme?.lowercase(Locale.ROOT) in EXTERNAL_LINK_SCHEMES) {
            onExternalLink(uri)
        }
        return true
    }

    fun findAll(query: String): Boolean {
        if (query.isEmpty() || query.length > MAX_FIND_QUERY_LENGTH) {
            clearFindMatches()
            return false
        }
        activeFindQuery = query
        findMatchCount = 0
        isFindCountingDone = false
        webView.findAllAsync(query)
        return true
    }

    fun findNext(forward: Boolean): Boolean {
        if (activeFindQuery == null || findMatchCount == 0 || !isFindCountingDone) return false
        webView.findNext(forward)
        return true
    }

    fun clearFindMatches() {
        activeFindQuery = null
        findMatchCount = 0
        isFindCountingDone = false
        webView.clearMatches()
    }

    fun setTextZoom(percent: Int): Int {
        val normalized = MarkdownPreviewerTextZoom.normalize(percent)
        webView.settings.textZoom = normalized
        return normalized
    }

    fun destroy() {
        if (destroyed) return
        destroyed = true
        documentPathHandler.clearDocument()
        activeFindQuery = null
        findMatchCount = 0
        isFindCountingDone = false
        printAdapterActive = false
        activeDocumentRelativePath = MarkdownPreviewerWebOrigin.DOCUMENT_FILE_NAME
        activeDocumentUrl = MarkdownPreviewerWebOrigin.DOCUMENT_URL
        webView.setFindListener(null)
        webView.clearMatches()
        webView.stopLoading()
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = null
        webView.loadUrl("about:blank")
        webView.clearHistory()
        webView.clearFormData()
        webView.removeAllViews()
        webView.destroy()
    }

    private fun isLocalAnchor(uri: Uri): Boolean {
        return uri.fragment != null && isActiveDocumentUrl(uri)
    }

    private fun isActiveDocumentUrl(uri: Uri): Boolean =
        uri.scheme.equals("https", ignoreCase = true) &&
            MarkdownPreviewerWebOrigin.isDomain(uri.host) &&
            uri.port == -1 &&
            uri.userInfo == null &&
            uri.query == null &&
            uri.path == activeDocumentUrl.toUri().path

    companion object {
        private val EXTERNAL_LINK_SCHEMES = setOf("http", "https")

        private val FORBIDDEN_HEADERS = mapOf(
            "Cache-Control" to "no-store, max-age=0",
            "X-Content-Type-Options" to "nosniff",
        )

        private fun forbidden() = WebResourceResponse(
            "text/plain",
            Charsets.UTF_8.name(),
            403,
            "Forbidden",
            FORBIDDEN_HEADERS,
            ByteArrayInputStream(ByteArray(0)),
        )
    }
}

internal object MarkdownPreviewerRequestPolicy {

    fun shouldLetWebViewLoadDataResource(
        url: String,
        isForMainFrame: Boolean,
    ): Boolean =
        !isForMainFrame &&
            url.startsWith("data:", ignoreCase = true) &&
            MarkdownPreviewerUrlPolicy.isSafeResource(url)

    fun shouldLetWebViewLoadHttpsSubresource(
        url: String,
        isForMainFrame: Boolean,
    ): Boolean {
        if (isForMainFrame) return false
        val host = MarkdownPreviewerUrlPolicy.safeRemoteHttpsHost(url) ?: return false
        return !MarkdownPreviewerWebOrigin.isDomain(host)
    }
}
