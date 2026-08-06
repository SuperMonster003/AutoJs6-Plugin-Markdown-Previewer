@file:Suppress("DEPRECATION")

package io.github.supermonster003.autojs6.plugin.markdownpreview

import android.net.Uri
import android.view.ContextThemeWrapper
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

@RunWith(AndroidJUnit4::class)
class MarkdownPreviewWebControllerInstrumentationTest {

    @Test
    fun generatedMarkdownLoadsFromVirtualHttpsOrigin() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        val resourceRoot = Uri.parse("content://markdown.preview.test/root/document")

        val controller = AtomicReference<MarkdownPreviewWebController?>()
        val observedTitle = AtomicReference<String?>()
        val observedUrl = AtomicReference<String?>()
        val pageFinished = CountDownLatch(1)

        try {
            instrumentation.runOnMainSync {
                val context = ContextThemeWrapper(targetContext, R.style.AppTheme)
                val webView = WebView(context)
                controller.set(
                    MarkdownPreviewWebController(
                        context = context,
                        webView = webView,
                        resourceRoot = resourceRoot,
                        onExternalLink = {},
                        onPageFinished = {
                            observedTitle.set(webView.title)
                            observedUrl.set(webView.url)
                            pageFinished.countDown()
                        },
                    ).also {
                        val renderedPreview = MarkdownPreviewRenderer().render(
                            markdown = """
                                # Document preview loaded

                                <div align="center">
                                  <details><summary>Preview details</summary>Safe content</details>
                                </div>

                                | Name | Value |
                                | --- | --- |
                                | One | Two |
                            """.trimIndent(),
                            stylesheetName = "github-light.css",
                            customCss = null,
                        ).replace(
                            "<head>",
                            "<head><title>$EXPECTED_MARKDOWN_TITLE</title>",
                        )
                        it.show(renderedPreview)
                    },
                )
            }

            assertTrue(
                "Timed out waiting for the generated preview document",
                pageFinished.await(PAGE_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS),
            )
            instrumentation.waitForIdleSync()
            assertEquals(EXPECTED_MARKDOWN_TITLE, observedTitle.get())
            assertEquals(MarkdownPreviewWebOrigin.DOCUMENT_URL, observedUrl.get())
        } finally {
            instrumentation.runOnMainSync {
                controller.getAndSet(null)?.destroy()
            }
        }
    }

    @Test
    fun untrustedDocumentCapabilitiesAreDisabled() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext

        instrumentation.runOnMainSync {
            val context = ContextThemeWrapper(targetContext, R.style.AppTheme)
            val webView = WebView(context)
            val controller = MarkdownPreviewWebController(
                context = context,
                webView = webView,
                resourceRoot = Uri.parse("content://markdown.preview.test/root/document"),
                onExternalLink = {},
            )
            try {
                webView.settings.apply {
                    assertFalse(javaScriptEnabled)
                    assertFalse(javaScriptCanOpenWindowsAutomatically)
                    assertFalse(domStorageEnabled)
                    assertFalse(databaseEnabled)
                    assertFalse(allowFileAccess)
                    assertFalse(allowContentAccess)
                    assertFalse(allowFileAccessFromFileURLs)
                    assertFalse(allowUniversalAccessFromFileURLs)
                    assertFalse(blockNetworkImage)
                    assertFalse(blockNetworkLoads)
                    assertEquals(WebSettings.MIXED_CONTENT_NEVER_ALLOW, mixedContentMode)
                }
            } finally {
                controller.destroy()
            }
        }
    }

    companion object {
        private const val EXPECTED_MARKDOWN_TITLE = "markdown-preview-load-ok"
        private const val PAGE_LOAD_TIMEOUT_SECONDS = 20L
    }
}
