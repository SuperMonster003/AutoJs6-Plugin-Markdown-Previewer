@file:Suppress("DEPRECATION")

package io.github.supermonster003.autojs6.plugin.markdownpreview

import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.WebView
import androidx.core.content.FileProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.autojs.plugin.explorer.api.ExplorerActionIntentExtras
import org.autojs.plugin.explorer.api.ExplorerActionIntentValues
import org.autojs.plugin.explorer.api.ExplorerActionPluginActions
import org.autojs.plugin.explorer.api.ExplorerActionProtocol
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

@RunWith(AndroidJUnit4::class)
class MarkdownPreviewDocumentNavigationInstrumentationTest {

    @Test
    fun linkedDocumentOpensInsideViewerAndBackReturnsToInitialDocument() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val targetContext = instrumentation.targetContext
        val fixtureDirectory = File(
            targetContext.filesDir,
            "markdown-preview-instrumentation/navigation",
        ).apply { mkdirs() }
        val initialFile = File(fixtureDirectory, INITIAL_FILE_NAME).apply {
            writeText(
                """
                    # Initial document

                    [Open second](second.md#second-heading)
                """.trimIndent(),
            )
        }
        val linkedFile = File(fixtureDirectory, LINKED_FILE_NAME).apply {
            writeText(
                """
                    # Second heading

                    Linked document content.
                """.trimIndent(),
            )
        }
        val authority = "${targetContext.packageName}.test.files"
        val initialUri = FileProvider.getUriForFile(targetContext, authority, initialFile)
        val parentUri = requireNotNull(initialUri.encodedPath)
            .substringBeforeLast('/')
            .let { parentPath -> initialUri.buildUpon().encodedPath(parentPath).build() }
        val preferences = MarkdownPreviewPreferences(targetContext)
        val originalFullscreenPreference = preferences.startInFullscreenMode
        preferences.startInFullscreenMode = false

        var activity: MarkdownPreviewActivity? = null
        try {
            activity = instrumentation.startActivitySync(
                validPreviewIntent(
                    targetContext.packageName,
                    initialUri,
                    parentUri,
                    initialFile.length(),
                ),
            ) as MarkdownPreviewActivity
            val webView = activity.findViewById<WebView>(R.id.preview_web_view)
            val initialUrl = requireNotNull(
                MarkdownPreviewDocumentLinkPolicy.buildVirtualUrl(INITIAL_FILE_NAME),
            )
            assertTrue(
                "Initial Markdown preview did not finish loading",
                waitUntil(instrumentation) {
                    webView.visibility == View.VISIBLE &&
                        webView.url == initialUrl &&
                        activity.supportActionBar?.title == INITIAL_FILE_NAME
                },
            )

            instrumentation.runOnMainSync {
                activity.openDocumentLink(
                    MarkdownPreviewDocumentLink(
                        relativePath = LINKED_FILE_NAME,
                        displayName = LINKED_FILE_NAME,
                        fragment = LINKED_FRAGMENT,
                    ),
                )
            }
            val linkedUrl = requireNotNull(
                MarkdownPreviewDocumentLinkPolicy.buildVirtualUrl(LINKED_FILE_NAME, LINKED_FRAGMENT),
            )
            assertTrue(
                "Linked Markdown preview did not finish loading",
                waitUntil(instrumentation) {
                    webView.url == linkedUrl &&
                        activity.supportActionBar?.title == LINKED_FILE_NAME
                },
            )

            instrumentation.runOnMainSync {
                activity.onBackPressedDispatcher.onBackPressed()
            }
            assertTrue(
                "Viewer back did not restore the initial Markdown document",
                waitUntil(instrumentation) {
                    webView.url == initialUrl &&
                        activity.supportActionBar?.title == INITIAL_FILE_NAME
                },
            )
            assertEquals(INITIAL_FILE_NAME, activity.supportActionBar?.title)
        } finally {
            instrumentation.runOnMainSync {
                activity?.finish()
            }
            preferences.startInFullscreenMode = originalFullscreenPreference
            initialFile.delete()
            linkedFile.delete()
            fixtureDirectory.delete()
        }
    }

    private fun validPreviewIntent(
        packageName: String,
        documentUri: Uri,
        parentUri: Uri,
        size: Long,
    ): Intent {
        val clipData = ClipData(
            ClipDescription("Markdown navigation fixture", arrayOf("text/markdown")),
            ClipData.Item(documentUri),
        ).apply {
            addItem(ClipData.Item(parentUri))
        }
        return Intent(ExplorerActionPluginActions.EXECUTE)
            .setClassName(packageName, MarkdownPreviewActivity::class.java.name)
            .setDataAndType(documentUri, "text/markdown")
            .addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_PREFIX_URI_PERMISSION,
            )
            .putExtra(ExplorerActionIntentExtras.ACTION_ID, MarkdownPreviewPlugin.ID)
            .putExtra(ExplorerActionIntentExtras.PROTOCOL_VERSION, ExplorerActionProtocol.VERSION)
            .putExtra(ExplorerActionIntentExtras.DISPLAY_NAME, INITIAL_FILE_NAME)
            .putExtra(ExplorerActionIntentExtras.SIZE, size)
            .putExtra(ExplorerActionIntentExtras.PARENT_URI, parentUri)
            .putExtra(
                ExplorerActionIntentExtras.SOURCE_SURFACE,
                ExplorerActionIntentValues.SOURCE_SURFACE_MAIN,
            )
            .apply { this.clipData = clipData }
    }

    private fun waitUntil(
        instrumentation: android.app.Instrumentation,
        condition: () -> Boolean,
    ): Boolean {
        repeat(WAIT_ATTEMPTS) {
            val matched = AtomicBoolean(false)
            instrumentation.runOnMainSync {
                matched.set(condition())
            }
            if (matched.get()) return true
            Thread.sleep(WAIT_INTERVAL_MILLIS)
        }
        return false
    }

    companion object {
        private const val INITIAL_FILE_NAME = "README.md"
        private const val LINKED_FILE_NAME = "second.md"
        private const val LINKED_FRAGMENT = "second-heading"
        private const val WAIT_ATTEMPTS = 200
        private const val WAIT_INTERVAL_MILLIS = 50L
    }
}
