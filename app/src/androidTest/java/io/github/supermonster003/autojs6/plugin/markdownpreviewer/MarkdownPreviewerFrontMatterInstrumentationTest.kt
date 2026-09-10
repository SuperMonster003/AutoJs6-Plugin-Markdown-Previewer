@file:Suppress("DEPRECATION")

package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

@RunWith(AndroidJUnit4::class)
class MarkdownPreviewerFrontMatterInstrumentationTest {

    @Test
    fun collapsedSanitizedYamlMetadataLoadsInJavaScriptFreeWebView() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        val metadataLabel = targetContext.getString(R.string.text_yaml_metadata)
        val renderedPreviewer = MarkdownPreviewerRenderer().render(
            markdown = """
                ---
                title: Device fixture
                payload: "</code><script id='$UNSAFE_SCRIPT_ID'>alert(1)</script>"
                format: html
                ---
                # $VISIBLE_BODY_MARKER

                Android WebView content.
            """.trimIndent(),
            stylesheetName = "github-light.css",
            customCss = null,
            frontMatterLabel = metadataLabel,
        ).replace(
            "<head>",
            "<head><title>$EXPECTED_TITLE</title>",
        )
        val document = Jsoup.parse(renderedPreviewer)
        val details = requireNotNull(document.selectFirst("details.markdown-front-matter"))
        val code = requireNotNull(details.selectFirst("pre > code.language-yaml.syntax-highlighted"))
        assertFalse(details.hasAttr("open"))
        assertEquals(metadataLabel, details.selectFirst("summary")?.text())
        assertTrue(code.wholeText().contains(UNSAFE_SCRIPT_ID))
        assertTrue(code.select(".syntax-property").isNotEmpty())
        assertTrue(document.select("script").isEmpty())
        assertEquals(VISIBLE_BODY_MARKER, document.selectFirst("h1")?.text())
        assertTrue(
            document.selectFirst("meta[http-equiv=Content-Security-Policy]")
                ?.attr("content")
                .orEmpty()
                .contains("script-src 'none'"),
        )
        val baseCss = targetContext.assets.open("markdown-previewer/base.css")
            .bufferedReader()
            .use { it.readText() }
        assertTrue(baseCss.contains(".markdown-front-matter"))

        val activity = AtomicReference<MarkdownPreviewerWebViewTestActivity?>()
        val controller = AtomicReference<MarkdownPreviewerWebController?>()
        val webView = AtomicReference<WebView?>()
        val observedTitle = AtomicReference<String?>()
        val observedUrl = AtomicReference<String?>()
        val pageFinished = CountDownLatch(1)

        try {
            activity.set(
                instrumentation.startActivitySync(
                    Intent(targetContext, MarkdownPreviewerWebViewTestActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                ) as MarkdownPreviewerWebViewTestActivity,
            )
            instrumentation.runOnMainSync {
                webView.set(requireNotNull(activity.get()).webView)
                controller.set(
                    MarkdownPreviewerWebController(
                        context = targetContext,
                        webView = requireNotNull(webView.get()),
                        resourceRoot = Uri.parse("content://markdown.previewer.test/root/document"),
                        onExternalLink = {},
                        onPageFinished = {
                            observedTitle.set(webView.get()?.title)
                            observedUrl.set(webView.get()?.url)
                            pageFinished.countDown()
                        },
                    ).also { previewerController ->
                        assertFalse(requireNotNull(webView.get()).settings.javaScriptEnabled)
                        previewerController.show(renderedPreviewer, "front-matter.qmd")
                    },
                )
            }

            assertTrue(
                "Timed out waiting for the front matter previewer",
                pageFinished.await(PAGE_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS),
            )
            instrumentation.waitForIdleSync()
            assertEquals(EXPECTED_TITLE, observedTitle.get())
            assertEquals(
                MarkdownPreviewerDocumentLinkPolicy.buildVirtualUrl("front-matter.qmd"),
                observedUrl.get(),
            )
        } finally {
            instrumentation.runOnMainSync {
                controller.getAndSet(null)?.destroy()
                webView.set(null)
                activity.getAndSet(null)?.finish()
            }
        }
    }

    companion object {
        private const val EXPECTED_TITLE = "Front matter instrumentation"
        private const val VISIBLE_BODY_MARKER = "Visible front matter body"
        private const val UNSAFE_SCRIPT_ID = "front-matter-script-payload"
        private const val PAGE_LOAD_TIMEOUT_SECONDS = 30L
    }
}
