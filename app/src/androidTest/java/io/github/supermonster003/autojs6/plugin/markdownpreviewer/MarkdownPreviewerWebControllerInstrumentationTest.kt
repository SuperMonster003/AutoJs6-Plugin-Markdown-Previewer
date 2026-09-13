@file:Suppress("DEPRECATION")

package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import android.content.Intent
import android.net.Uri
import android.view.ContextThemeWrapper
import android.webkit.WebSettings
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
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

@RunWith(AndroidJUnit4::class)
class MarkdownPreviewerWebControllerInstrumentationTest {

    @Test
    fun generatedMarkdownLoadsFromVirtualHttpsOrigin() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        val resourceRoot = Uri.parse("content://markdown.previewer.test/root/document")

        val controller = AtomicReference<MarkdownPreviewerWebController?>()
        val observedTitle = AtomicReference<String?>()
        val observedUrl = AtomicReference<String?>()
        val pageFinished = CountDownLatch(1)

        try {
            instrumentation.runOnMainSync {
                val context = ContextThemeWrapper(targetContext, R.style.AppTheme)
                val webView = WebView(context)
                controller.set(
                    MarkdownPreviewerWebController(
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
                        val renderedPreviewer = MarkdownPreviewerRenderer().render(
                            markdown = """
                                # Document previewer loaded

                                <div align="center">
                                  <details><summary>Previewer details</summary>Safe content</details>
                                </div>

                                | Name | Value |
                                | --- | --- |
                                | One | Two |

                                Device-side footnote[^device-note] and inline note^[Rendered without JavaScript].

                                [^device-note]: CommonMark 0.30 footnote definition.
                            """.trimIndent(),
                            stylesheetName = "github-light.css",
                            customCss = null,
                        ).replace(
                            "<head>",
                            "<head><title>$EXPECTED_MARKDOWN_TITLE</title>",
                        )
                        val parsedPreviewer = Jsoup.parse(renderedPreviewer)
                        assertEquals(2, parsedPreviewer.select("sup.footnote-ref > a[data-footnote-ref]").size)
                        assertEquals(2, parsedPreviewer.select("section.footnotes > ol > li").size)
                        assertTrue(parsedPreviewer.select("script").isEmpty())
                        it.show(renderedPreviewer)
                    },
                )
            }

            assertTrue(
                "Timed out waiting for the generated previewer document",
                pageFinished.await(PAGE_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS),
            )
            instrumentation.waitForIdleSync()
            assertEquals(EXPECTED_MARKDOWN_TITLE, observedTitle.get())
            assertEquals(MarkdownPreviewerWebOrigin.DOCUMENT_URL, observedUrl.get())
        } finally {
            instrumentation.runOnMainSync {
                controller.getAndSet(null)?.destroy()
            }
        }
    }

    @Test
    fun nestedMarkdownDocumentLoadsFromItsOwnEncodedVirtualPath() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        val resourceRoot = Uri.parse("content://markdown.previewer.test/root/document")
        val relativePath = "指南/开始 阅读.md"
        val expectedUrl = requireNotNull(MarkdownPreviewerDocumentLinkPolicy.buildVirtualUrl(relativePath))

        val controller = AtomicReference<MarkdownPreviewerWebController?>()
        val observedTitle = AtomicReference<String?>()
        val observedUrl = AtomicReference<String?>()
        val pageFinished = CountDownLatch(1)

        try {
            instrumentation.runOnMainSync {
                val context = ContextThemeWrapper(targetContext, R.style.AppTheme)
                val webView = WebView(context)
                controller.set(
                    MarkdownPreviewerWebController(
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
                        it.show(
                            html = "<html><head><title>$EXPECTED_NESTED_TITLE</title></head><body>OK</body></html>",
                            relativePath = relativePath,
                        )
                    },
                )
            }

            assertTrue(
                "Timed out waiting for the nested previewer document",
                pageFinished.await(PAGE_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS),
            )
            instrumentation.waitForIdleSync()
            assertEquals(EXPECTED_NESTED_TITLE, observedTitle.get())
            assertEquals(expectedUrl, observedUrl.get())
        } finally {
            instrumentation.runOnMainSync {
                controller.getAndSet(null)?.destroy()
            }
        }
    }

    @Test
    fun relativeMarkdownNavigationRequiresAnExplicitMainFrameGesture() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        val observedDocumentLink = AtomicReference<MarkdownPreviewerDocumentLink?>()
        val observedExternalLink = AtomicReference<Uri?>()

        instrumentation.runOnMainSync {
            val context = ContextThemeWrapper(targetContext, R.style.AppTheme)
            val webView = WebView(context)
            val controller = MarkdownPreviewerWebController(
                context = context,
                webView = webView,
                resourceRoot = Uri.parse("content://markdown.previewer.test/root/document"),
                onExternalLink = observedExternalLink::set,
                onDocumentLink = observedDocumentLink::set,
            )
            try {
                val documentUri = Uri.parse(
                    "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}guide/next.md#destination",
                )
                assertTrue(controller.handleNavigationRequest(documentUri, isForMainFrame = true, hasGesture = false))
                assertEquals(null, observedDocumentLink.get())

                assertTrue(controller.handleNavigationRequest(documentUri, isForMainFrame = true, hasGesture = true))
                assertEquals(
                    MarkdownPreviewerDocumentLink(
                        relativePath = "guide/next.md",
                        displayName = "next.md",
                        fragment = "destination",
                    ),
                    observedDocumentLink.get(),
                )

                observedDocumentLink.set(null)
                assertTrue(
                    controller.handleNavigationRequest(
                        Uri.parse("${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}%2e%2e/outside.md"),
                        isForMainFrame = true,
                        hasGesture = true,
                    ),
                )
                assertEquals(null, observedDocumentLink.get())

                val externalUri = Uri.parse("https://example.com/guide")
                assertTrue(controller.handleNavigationRequest(externalUri, isForMainFrame = true, hasGesture = true))
                assertEquals(externalUri, observedExternalLink.get())

                assertFalse(
                    controller.handleNavigationRequest(
                        Uri.parse("${MarkdownPreviewerWebOrigin.DOCUMENT_URL}#local-heading"),
                        isForMainFrame = true,
                        hasGesture = true,
                    ),
                )
            } finally {
                controller.destroy()
            }
        }
    }

    @Test
    fun syntaxHighlightedDocumentLoadsWithJavaScriptDisabled() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        val resourceRoot = Uri.parse("content://markdown.previewer.test/root/document")
        val renderedPreviewer = MarkdownPreviewerRenderer().render(
            markdown = """
                # Syntax fixture

                ```kotlin
                // rendered without JavaScript
                fun answer(): Int = 42
                ```
            """.trimIndent(),
            stylesheetName = "github-dark.css",
            customCss = null,
        ).replace(
            "<head>",
            "<head><title>$EXPECTED_SYNTAX_TITLE</title>",
        )
        val document = Jsoup.parse(renderedPreviewer)
        assertTrue(document.select("pre > code.syntax-highlighted span.syntax-token").isNotEmpty())
        assertTrue(document.select("script").isEmpty())
        ANDROID_SYNTAX_FIXTURES.forEach { (language, source) ->
            val tokens = MarkdownPreviewerSyntaxHighlighter.tokenize(language, source)
            assertTrue("$language must be supported on Android", !tokens.isNullOrEmpty())
        }
        assertTrue(
            document.selectFirst("meta[http-equiv=Content-Security-Policy]")
                ?.attr("content")
                .orEmpty()
                .contains("script-src 'none'"),
        )
        val baseCss = targetContext.assets.open("markdown-previewer/base.css")
            .bufferedReader()
            .use { it.readText() }
        assertTrue(baseCss.contains(".syntax-keyword"))
        SYNTAX_COLOR_VARIABLES.forEach { variable ->
            assertTrue("base.css must use $variable", baseCss.contains("var($variable)"))
        }
        listOf("github-light.css", "github-dark.css", "paper.css", "sepia.css").forEach { stylesheet ->
            val css = targetContext.assets.open("markdown-previewer/$stylesheet")
                .bufferedReader()
                .use { it.readText() }
            SYNTAX_COLOR_VARIABLES.forEach { variable ->
                assertTrue("$stylesheet must define $variable", css.contains("$variable:"))
            }
        }

        val activity = AtomicReference<MarkdownPreviewerWebViewTestActivity?>()
        val controller = AtomicReference<MarkdownPreviewerWebController?>()
        val observedTitle = AtomicReference<String?>()
        val pageFinished = CountDownLatch(1)

        try {
            activity.set(
                instrumentation.startActivitySync(
                    Intent(targetContext, MarkdownPreviewerWebViewTestActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                ) as MarkdownPreviewerWebViewTestActivity,
            )
            instrumentation.runOnMainSync {
                val webView = requireNotNull(activity.get()).webView
                controller.set(
                    MarkdownPreviewerWebController(
                        context = targetContext,
                        webView = webView,
                        resourceRoot = resourceRoot,
                        onExternalLink = {},
                        onPageFinished = {
                            observedTitle.set(webView.title)
                            pageFinished.countDown()
                        },
                    ).also {
                        assertFalse(webView.settings.javaScriptEnabled)
                        it.show(renderedPreviewer)
                    },
                )
            }

            assertTrue(
                "Timed out waiting for the syntax-highlighted document",
                pageFinished.await(PAGE_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS),
            )
            instrumentation.waitForIdleSync()
            assertEquals(EXPECTED_SYNTAX_TITLE, observedTitle.get())
        } finally {
            instrumentation.runOnMainSync {
                controller.getAndSet(null)?.destroy()
                activity.getAndSet(null)?.finish()
            }
        }
    }

    @Test
    fun documentOutlineAnchorNavigationTargetsGeneratedHeading() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        val resourceRoot = Uri.parse("content://markdown.previewer.test/root/document")
        val renderedPreviewer = MarkdownPreviewerRenderer().renderWithOutline(
            markdown = buildString {
                appendLine("# Document start")
                repeat(250) { index ->
                    appendLine()
                    appendLine("Paragraph $index keeps the destination below the visible viewport.")
                }
                appendLine()
                appendLine("## Outline destination")
                appendLine()
                appendLine("Target content")
            },
            stylesheetName = "github-light.css",
            customCss = null,
        )
        val destination = renderedPreviewer.outline.last()

        val activity = AtomicReference<MarkdownPreviewerWebViewTestActivity?>()
        val controller = AtomicReference<MarkdownPreviewerWebController?>()
        val webViewReference = AtomicReference<WebView?>()
        val anchorPoller = AtomicReference<Runnable?>()
        val pageFinished = CountDownLatch(1)
        val anchorReached = CountDownLatch(1)

        try {
            activity.set(
                instrumentation.startActivitySync(
                    Intent(targetContext, MarkdownPreviewerWebViewTestActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                ) as MarkdownPreviewerWebViewTestActivity,
            )
            instrumentation.runOnMainSync {
                val webView = requireNotNull(activity.get()).webView
                webViewReference.set(webView)
                controller.set(
                    MarkdownPreviewerWebController(
                        context = targetContext,
                        webView = webView,
                        resourceRoot = resourceRoot,
                        onExternalLink = {},
                        onPageFinished = pageFinished::countDown,
                    ).also { it.show(renderedPreviewer.html) },
                )
            }
            assertTrue(
                "Timed out waiting for the outline test document",
                pageFinished.await(PAGE_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS),
            )

            instrumentation.runOnMainSync {
                val webView = requireNotNull(webViewReference.get())
                assertTrue(requireNotNull(controller.get()).scrollToAnchor(destination.anchorId))
                val poller = object : Runnable {
                    override fun run() {
                        val currentFragment = webView.url?.let(Uri::parse)?.fragment
                        if (currentFragment == destination.anchorId && webView.scrollY > 0) {
                            anchorReached.countDown()
                        } else {
                            webView.postDelayed(this, ANCHOR_POLL_INTERVAL_MILLIS)
                        }
                    }
                }
                anchorPoller.set(poller)
                webView.post(poller)
            }

            assertTrue(
                "Timed out waiting for the document outline anchor jump",
                anchorReached.await(PAGE_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS),
            )
            instrumentation.runOnMainSync {
                val webView = requireNotNull(webViewReference.get())
                assertEquals(destination.anchorId, webView.url?.let(Uri::parse)?.fragment)
                assertTrue(webView.scrollY > 0)
            }
        } finally {
            instrumentation.runOnMainSync {
                val webView = webViewReference.getAndSet(null)
                anchorPoller.getAndSet(null)?.let { poller -> webView?.removeCallbacks(poller) }
                controller.getAndSet(null)?.destroy()
                activity.getAndSet(null)?.finish()
            }
        }
    }

    @Test
    fun findInPageCountsMatchesAndNavigatesBothDirections() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        val resourceRoot = Uri.parse("content://markdown.previewer.test/root/document")
        val renderedPreviewer = MarkdownPreviewerRenderer().render(
            markdown = """
                # Search fixture

                First needle occurrence.

                Second **needle** occurrence.

                Third `needle` occurrence.
            """.trimIndent(),
            stylesheetName = "github-light.css",
            customCss = null,
        )

        val activity = AtomicReference<MarkdownPreviewerWebViewTestActivity?>()
        val controller = AtomicReference<MarkdownPreviewerWebController?>()
        val phase = AtomicInteger(FIND_PHASE_COUNT)
        val observedResult = AtomicReference<MarkdownPreviewerFindResult?>()
        val pageFinished = CountDownLatch(1)
        val countFinished = CountDownLatch(1)
        val nextFinished = CountDownLatch(1)
        val previousFinished = CountDownLatch(1)

        try {
            activity.set(
                instrumentation.startActivitySync(
                    Intent(targetContext, MarkdownPreviewerWebViewTestActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                ) as MarkdownPreviewerWebViewTestActivity,
            )
            instrumentation.runOnMainSync {
                val webView = requireNotNull(activity.get()).webView
                controller.set(
                    MarkdownPreviewerWebController(
                        context = targetContext,
                        webView = webView,
                        resourceRoot = resourceRoot,
                        onExternalLink = {},
                        onPageFinished = pageFinished::countDown,
                        onFindResult = { result ->
                            observedResult.set(result)
                            if (result.isDoneCounting && result.matchCount == EXPECTED_FIND_MATCH_COUNT) {
                                when (phase.get()) {
                                    FIND_PHASE_COUNT -> if (result.activeMatchOrdinal == 0) {
                                        countFinished.countDown()
                                    }
                                    FIND_PHASE_NEXT -> if (result.activeMatchOrdinal == 1) {
                                        nextFinished.countDown()
                                    }
                                    FIND_PHASE_PREVIOUS -> if (result.activeMatchOrdinal == 0) {
                                        previousFinished.countDown()
                                    }
                                }
                            }
                        },
                    ).also { it.show(renderedPreviewer) },
                )
            }
            assertTrue(
                "Timed out waiting for the find-in-page test document",
                pageFinished.await(PAGE_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS),
            )

            instrumentation.runOnMainSync {
                assertTrue(requireNotNull(controller.get()).findAll(FIND_QUERY))
            }
            assertTrue(
                "Timed out waiting for the find-in-page match count",
                countFinished.await(PAGE_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS),
            )
            assertEquals(EXPECTED_FIND_MATCH_COUNT, observedResult.get()?.matchCount)
            assertEquals(1, observedResult.get()?.activeMatchNumber)

            phase.set(FIND_PHASE_NEXT)
            instrumentation.runOnMainSync {
                assertTrue(requireNotNull(controller.get()).findNext(forward = true))
            }
            assertTrue(
                "Timed out waiting for the next find-in-page match",
                nextFinished.await(PAGE_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS),
            )
            assertEquals(2, observedResult.get()?.activeMatchNumber)

            phase.set(FIND_PHASE_PREVIOUS)
            instrumentation.runOnMainSync {
                assertTrue(requireNotNull(controller.get()).findNext(forward = false))
            }
            assertTrue(
                "Timed out waiting for the previous find-in-page match",
                previousFinished.await(PAGE_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS),
            )
            assertEquals(1, observedResult.get()?.activeMatchNumber)

            instrumentation.runOnMainSync {
                requireNotNull(controller.get()).apply {
                    clearFindMatches()
                    assertFalse(findNext(forward = true))
                    assertFalse(findAll(""))
                    assertFalse(findAll("x".repeat(MAX_FIND_QUERY_LENGTH + 1)))
                }
            }
        } finally {
            instrumentation.runOnMainSync {
                controller.getAndSet(null)?.destroy()
                activity.getAndSet(null)?.finish()
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
            val controller = MarkdownPreviewerWebController(
                context = context,
                webView = webView,
                resourceRoot = Uri.parse("content://markdown.previewer.test/root/document"),
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
                    assertTrue(blockNetworkLoads)
                    assertEquals(WebSettings.MIXED_CONTENT_NEVER_ALLOW, mixedContentMode)
                }
            } finally {
                controller.destroy()
            }
        }
    }

    @Test
    fun textZoomAppliesWithoutDisablingPinchZoom() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext

        instrumentation.runOnMainSync {
            val context = ContextThemeWrapper(targetContext, R.style.AppTheme)
            val webView = WebView(context)
            val controller = MarkdownPreviewerWebController(
                context = context,
                webView = webView,
                resourceRoot = Uri.parse("content://markdown.previewer.test/root/document"),
                onExternalLink = {},
                initialTextZoomPercent = 125,
            )
            try {
                webView.settings.apply {
                    assertEquals(125, textZoom)
                    assertTrue(supportZoom())
                    assertTrue(builtInZoomControls)
                    assertFalse(displayZoomControls)
                }

                assertEquals(175, controller.setTextZoom(174))
                webView.settings.apply {
                    assertEquals(175, textZoom)
                    assertTrue(supportZoom())
                    assertTrue(builtInZoomControls)
                    assertFalse(displayZoomControls)
                }
            } finally {
                controller.destroy()
            }
        }
    }

    companion object {
        private const val EXPECTED_MARKDOWN_TITLE = "markdown-previewer-load-ok"
        private const val EXPECTED_NESTED_TITLE = "markdown-previewer-nested-load-ok"
        private const val EXPECTED_SYNTAX_TITLE = "markdown-previewer-syntax-ok"
        private const val FIND_QUERY = "needle"
        private const val EXPECTED_FIND_MATCH_COUNT = 3
        private const val FIND_PHASE_COUNT = 0
        private const val FIND_PHASE_NEXT = 1
        private const val FIND_PHASE_PREVIOUS = 2
        private const val PAGE_LOAD_TIMEOUT_SECONDS = 20L
        private const val ANCHOR_POLL_INTERVAL_MILLIS = 50L

        private val SYNTAX_COLOR_VARIABLES = listOf(
            "--previewer-syntax-attribute",
            "--previewer-syntax-comment",
            "--previewer-syntax-function",
            "--previewer-syntax-keyword",
            "--previewer-syntax-meta",
            "--previewer-syntax-number",
            "--previewer-syntax-string",
            "--previewer-syntax-tag",
            "--previewer-syntax-type",
            "--previewer-syntax-variable",
        )

        private val ANDROID_SYNTAX_FIXTURES = listOf(
            "kotlin" to "val answer: Int = 42",
            "java" to "public record Answer(int value) {}",
            "javascript" to "const answer = () => 42;",
            "typescript" to "interface Answer { value: number }",
            "c" to "static int answer = 42;",
            "cpp" to "constexpr int answer = 42;",
            "csharp" to "public record Answer(int Value);",
            "go" to "func answer() int { return 42 }",
            "rust" to "fn answer() -> i32 { 42 }",
            "swift" to "func answer() -> Int { 42 }",
            "dart" to "final int answer = 42;",
            "python" to "def answer(): return 42",
            "json" to "{\"answer\": 42}",
            "bash" to "echo \$HOME",
            "sql" to "SELECT answer FROM results;",
            "html" to "<p class=\"answer\">42</p>",
            "css" to ".answer { color: #fff; }",
            "yaml" to "answer: 42",
            "markdown" to "# Answer",
        )
    }
}
