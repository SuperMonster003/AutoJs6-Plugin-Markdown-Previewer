package io.github.supermonster003.autojs6.plugin.markdownpreview

import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter

/**
 * Keeps the visible WebView from drawing while Chromium converts it to PDF and guarantees that
 * viewer state is restored exactly once when printing finishes or fails to start.
 */
internal class MarkdownPreviewPrintDocumentAdapter(
    private val delegate: PrintDocumentAdapter,
    private val onPrintingStarted: () -> Unit,
    private val onPrintingFinished: () -> Unit,
) : PrintDocumentAdapter() {

    private var delegateStarted = false
    private var finished = false

    override fun onStart() {
        if (finished || delegateStarted) return
        try {
            onPrintingStarted()
            delegateStarted = true
            delegate.onStart()
        } catch (error: Throwable) {
            try {
                if (delegateStarted) delegate.onFinish()
            } finally {
                delegateStarted = false
                complete()
            }
            throw error
        }
    }

    override fun onLayout(
        oldAttributes: PrintAttributes,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal,
        callback: LayoutResultCallback,
        extras: Bundle,
    ) {
        if (finished) {
            callback.onLayoutCancelled()
            return
        }
        delegate.onLayout(oldAttributes, newAttributes, cancellationSignal, callback, extras)
    }

    override fun onWrite(
        pages: Array<out PageRange>,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal,
        callback: WriteResultCallback,
    ) {
        if (finished) {
            callback.onWriteCancelled()
            return
        }
        delegate.onWrite(pages, destination, cancellationSignal, callback)
    }

    override fun onFinish() {
        if (finished) return
        try {
            if (delegateStarted) delegate.onFinish()
        } finally {
            complete()
        }
    }

    /** Releases viewer state when PrintManager could not create a job. */
    fun abort() {
        onFinish()
    }

    private fun complete() {
        if (finished) return
        finished = true
        runCatching(onPrintingFinished)
    }
}

internal object MarkdownPreviewPrintPolicy {

    private const val FALLBACK_DOCUMENT_NAME = "Markdown"

    fun documentName(displayName: String?): String =
        MarkdownPreviewIntentPolicy.sanitizeDisplayName(displayName)
            ?: FALLBACK_DOCUMENT_NAME
}
