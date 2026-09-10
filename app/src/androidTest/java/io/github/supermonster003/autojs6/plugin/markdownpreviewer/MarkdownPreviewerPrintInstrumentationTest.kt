package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.TestPrintCallbacks
import android.view.View
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

@RunWith(AndroidJUnit4::class)
class MarkdownPreviewerPrintInstrumentationTest {

    @Test
    fun sanitizedCurrentDocumentWritesPdfAndRestoresWebView() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        val renderedPreviewer = MarkdownPreviewerRenderer().render(
            markdown = buildString {
                appendLine("# Printable document")
                appendLine()
                appendLine(PRINT_SAFE_TEXT_MARKER)
                appendLine()
                appendLine("<script>$PRINT_UNSAFE_SCRIPT_MARKER</script>")
                appendLine()
                appendLine("[Safe Android link](https://developer.android.com/)")
                appendLine()
                appendLine("[Unsafe link](javascript:$PRINT_UNSAFE_URI_MARKER)")
                appendLine()
                appendLine("| Item | Value |")
                appendLine("| --- | ---: |")
                appendLine("| Rendering | 42 |")
                appendLine()
                appendLine("```kotlin")
                appendLine("fun printableAnswer(): Int = 42")
                appendLine("```")
                appendLine()
                appendLine("Printable footnote reference[^print-note].")
                repeat(PRINT_PARAGRAPH_COUNT) { index ->
                    appendLine()
                    appendLine("Printable paragraph ${index + 1}: consistent pagination verification text.")
                }
                appendLine()
                appendLine("[^print-note]: $PRINT_FOOTNOTE_TEXT_MARKER")
            },
            stylesheetName = "github-light.css",
            customCss = null,
        )
        val parsedDocument = Jsoup.parse(renderedPreviewer)
        assertFalse(renderedPreviewer.contains(PRINT_UNSAFE_SCRIPT_MARKER))
        assertFalse(renderedPreviewer.contains(PRINT_UNSAFE_URI_MARKER))
        assertFalse(
            requireNotNull(parsedDocument.select("a").firstOrNull { it.text() == "Unsafe link" })
                .hasAttr("href"),
        )
        assertEquals(1, parsedDocument.select("sup.footnote-ref > a[data-footnote-ref]").size)
        assertEquals(
            PRINT_FOOTNOTE_TEXT_MARKER,
            parsedDocument.selectFirst("section.footnotes > ol > li")?.text()?.substringBefore(" ↩"),
        )
        val baseCss = targetContext.assets.open("markdown-previewer/base.css")
            .bufferedReader()
            .use { it.readText() }
        assertTrue(baseCss.contains("@media print"))
        assertTrue(baseCss.contains("break-inside: avoid"))

        val activity = AtomicReference<MarkdownPreviewerWebViewTestActivity?>()
        val controller = AtomicReference<MarkdownPreviewerWebController?>()
        val printAdapter = AtomicReference<MarkdownPreviewerPrintDocumentAdapter?>()
        val webViewReady = CountDownLatch(1)
        val printFinished = CountDownLatch(1)
        val layoutCallback = TestPrintCallbacks.Layout()
        val writeCallback = TestPrintCallbacks.Write()
        val outputDirectory = requireNotNull(targetContext.getExternalFilesDir(null))
        val outputFile = File(outputDirectory, PRINT_VERIFICATION_FILE_NAME)
        outputFile.delete()

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
                        resourceRoot = Uri.parse("content://markdown.previewer.test/root/document"),
                        onExternalLink = {},
                        onPageFinished = webViewReady::countDown,
                    ).also { it.show(renderedPreviewer, "print-verification.md") },
                )
            }
            assertTrue(
                "Timed out waiting for the printable WebView",
                webViewReady.await(PRINT_TIMEOUT_SECONDS, TimeUnit.SECONDS),
            )

            instrumentation.runOnMainSync {
                val webView = requireNotNull(activity.get()).webView
                assertEquals(View.VISIBLE, webView.visibility)
                printAdapter.set(
                    requireNotNull(controller.get()).createPrintDocumentAdapter("print-verification.md") {
                        printFinished.countDown()
                    }.also { adapter ->
                        assertTrue(
                            runCatching {
                                requireNotNull(controller.get())
                                    .createPrintDocumentAdapter("duplicate.md") {}
                            }.exceptionOrNull() is IllegalStateException,
                        )
                        adapter.onStart()
                        assertEquals(View.INVISIBLE, webView.visibility)
                        adapter.onLayout(
                            PrintAttributes.Builder().build(),
                            PRINT_ATTRIBUTES,
                            CancellationSignal(),
                            layoutCallback,
                            Bundle().apply {
                                putBoolean(PrintDocumentAdapter.EXTRA_PRINT_PREVIEW, true)
                            },
                        )
                    },
                )
            }
            assertTrue(
                "Timed out laying out the printable WebView",
                layoutCallback.finished.await(PRINT_TIMEOUT_SECONDS, TimeUnit.SECONDS),
            )
            assertFalse(layoutCallback.cancelled)
            assertNull(layoutCallback.error)
            assertNotNull(layoutCallback.info)

            ParcelFileDescriptor.open(
                outputFile,
                ParcelFileDescriptor.MODE_CREATE or
                    ParcelFileDescriptor.MODE_TRUNCATE or
                    ParcelFileDescriptor.MODE_READ_WRITE,
            ).use { destination ->
                instrumentation.runOnMainSync {
                    requireNotNull(printAdapter.get()).onWrite(
                        arrayOf(PageRange.ALL_PAGES),
                        destination,
                        CancellationSignal(),
                        writeCallback,
                    )
                }
                assertTrue(
                    "Timed out writing the printable WebView PDF",
                    writeCallback.finished.await(PRINT_TIMEOUT_SECONDS, TimeUnit.SECONDS),
                )
            }
            assertFalse(writeCallback.cancelled)
            assertNull(writeCallback.error)
            assertNotNull(writeCallback.pages)

            instrumentation.runOnMainSync {
                requireNotNull(printAdapter.get()).onFinish()
                assertEquals(View.VISIBLE, requireNotNull(activity.get()).webView.visibility)
            }
            assertTrue(printFinished.await(PRINT_TIMEOUT_SECONDS, TimeUnit.SECONDS))
            assertTrue(outputFile.length() > MINIMUM_PDF_SIZE_BYTES)
            assertEquals("%PDF-", outputFile.inputStream().use { input ->
                ByteArray(PDF_HEADER_LENGTH).also { header ->
                    assertEquals(PDF_HEADER_LENGTH, input.read(header))
                }.toString(Charsets.US_ASCII)
            })
            assertRenderedPdfHasVisibleContent(outputFile)
        } finally {
            instrumentation.runOnMainSync {
                printAdapter.getAndSet(null)?.abort()
                controller.getAndSet(null)?.destroy()
                activity.getAndSet(null)?.finish()
            }
        }
    }

    private fun assertRenderedPdfHasVisibleContent(pdfFile: File) {
        val fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
        PdfRenderer(fileDescriptor).use { renderer ->
            assertTrue(renderer.pageCount > 1)
            renderer.openPage(0).use { page ->
                val bitmap = Bitmap.createBitmap(RENDER_WIDTH, RENDER_HEIGHT, Bitmap.Config.ARGB_8888)
                bitmap.eraseColor(Color.WHITE)
                val matrix = Matrix().apply {
                    setScale(
                        RENDER_WIDTH.toFloat() / page.width,
                        RENDER_HEIGHT.toFloat() / page.height,
                    )
                }
                page.render(bitmap, null, matrix, PdfRenderer.Page.RENDER_MODE_FOR_PRINT)
                var nonWhitePixels = 0
                var y = 0
                while (y < bitmap.height) {
                    var x = 0
                    while (x < bitmap.width) {
                        val color = bitmap.getPixel(x, y)
                        if (Color.red(color) < WHITE_THRESHOLD ||
                            Color.green(color) < WHITE_THRESHOLD ||
                            Color.blue(color) < WHITE_THRESHOLD
                        ) {
                            nonWhitePixels++
                        }
                        x += PIXEL_SAMPLE_STEP
                    }
                    y += PIXEL_SAMPLE_STEP
                }
                bitmap.recycle()
                assertTrue(nonWhitePixels > MINIMUM_NON_WHITE_PIXEL_SAMPLES)
            }
        }
    }

    companion object {
        const val PRINT_VERIFICATION_FILE_NAME = "markdown-previewer-print-verification.pdf"

        private const val PRINT_SAFE_TEXT_MARKER = "PRINT_SAFE_TEXT_MARKER_42"
        private const val PRINT_UNSAFE_SCRIPT_MARKER = "PRINT_UNSAFE_SCRIPT_MARKER_91"
        private const val PRINT_UNSAFE_URI_MARKER = "PRINT_UNSAFE_URI_MARKER_73"
        private const val PRINT_FOOTNOTE_TEXT_MARKER = "PRINT_FOOTNOTE_TEXT_MARKER_58"
        private const val PRINT_PARAGRAPH_COUNT = 120
        private const val PRINT_TIMEOUT_SECONDS = 30L
        private const val MINIMUM_PDF_SIZE_BYTES = 4 * 1024L
        private const val PDF_HEADER_LENGTH = 5
        private const val RENDER_WIDTH = 620
        private const val RENDER_HEIGHT = 877
        private const val PIXEL_SAMPLE_STEP = 4
        private const val WHITE_THRESHOLD = 245
        private const val MINIMUM_NON_WHITE_PIXEL_SAMPLES = 100

        private val PRINT_ATTRIBUTES = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            .setResolution(PrintAttributes.Resolution("pdf", "PDF", 300, 300))
            .setMinMargins(PrintAttributes.Margins(500, 500, 500, 500))
            .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
            .build()
    }
}
