@file:Suppress("DEPRECATION")

package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.net.Uri
import androidx.test.runner.AndroidJUnit4
import org.autojs.plugin.explorer.api.ExplorerActionIntentExtras
import org.autojs.plugin.explorer.api.ExplorerActionIntentValues
import org.autojs.plugin.explorer.api.ExplorerActionPluginActions
import org.autojs.plugin.explorer.api.ExplorerActionProtocol
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MarkdownPreviewerIntentPolicyInstrumentationTest {

    private val parentUri = Uri.parse("content://org.autojs.test.fileprovider/root/documents")
    private val documentUri = Uri.parse("content://org.autojs.test.fileprovider/root/documents/README.md")

    @Test
    fun completeUriOnlyExplorerContractIsAccepted() {
        val resolved = MarkdownPreviewerIntentPolicy.resolve(validIntent())

        assertNotNull(resolved)
        assertEquals(documentUri, resolved?.documentUri)
        assertEquals(parentUri, resolved?.parentUri)
        assertEquals("README.md", resolved?.displayName)
    }

    @Test
    fun actionProtocolAndGrantFlagsAreMandatory() {
        assertNull(MarkdownPreviewerIntentPolicy.resolve(Intent(validIntent()).setAction(Intent.ACTION_VIEW)))
        assertNull(
            MarkdownPreviewerIntentPolicy.resolve(
                Intent(validIntent()).putExtra(ExplorerActionIntentExtras.PROTOCOL_VERSION, 2),
            ),
        )
        assertNull(
            MarkdownPreviewerIntentPolicy.resolve(
                Intent(validIntent()).apply {
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                },
            ),
        )
    }

    @Test
    fun targetParentAndClipDataMustDescribeTheSameContentTree() {
        assertNull(
            MarkdownPreviewerIntentPolicy.resolve(
                Intent(validIntent()).setData(Uri.parse("file:///sdcard/README.md")),
            ),
        )
        assertNull(
            MarkdownPreviewerIntentPolicy.resolve(
                Intent(validIntent()).putExtra(
                    ExplorerActionIntentExtras.PARENT_URI,
                    Uri.parse("content://org.autojs.test.fileprovider/root/other"),
                ),
            ),
        )
        assertNull(
            MarkdownPreviewerIntentPolicy.resolve(
                Intent(validIntent()).apply {
                    clipData = ClipData(
                        ClipDescription("Previewer target", arrayOf("text/markdown")),
                        ClipData.Item(documentUri),
                    )
                },
            ),
        )
    }

    @Test
    fun htmlIdentityIsRejectedEvenWhenMimeClaimsMarkdown() {
        val htmlUri = Uri.parse("content://org.autojs.test.fileprovider/root/documents/index.html")
        val intent = validIntent().apply {
            data = htmlUri
            type = "text/markdown"
            putExtra(ExplorerActionIntentExtras.DISPLAY_NAME, "index.html")
            clipData = ClipData(
                ClipDescription("Previewer target", arrayOf("text/markdown")),
                ClipData.Item(htmlUri),
            ).apply {
                addItem(ClipData.Item(parentUri))
            }
        }

        assertNull(MarkdownPreviewerIntentPolicy.resolve(intent))
    }

    @Test
    fun documentPathsRoundTripOnlyBelowTheAuthorizedParent() {
        val relativePath = "指南/开始 阅读.md"
        val resolved = MarkdownPreviewerPathPolicy.resolveDescendant(parentUri, relativePath)

        assertNotNull(resolved)
        assertTrue(MarkdownPreviewerPathPolicy.isDescendant(parentUri, requireNotNull(resolved)))
        assertEquals(relativePath, MarkdownPreviewerPathPolicy.relativePath(parentUri, resolved))
        assertNull(MarkdownPreviewerPathPolicy.resolveDescendant(parentUri, "../outside.md"))
        assertNull(MarkdownPreviewerPathPolicy.resolveDescendant(parentUri, "%2e%2e/outside.md"))
        assertFalse(
            MarkdownPreviewerPathPolicy.isDescendant(
                parentUri,
                Uri.parse("content://org.autojs.test.fileprovider/root/outside.md"),
            ),
        )
    }

    private fun validIntent(): Intent {
        val clipData = ClipData(
            ClipDescription("Previewer target", arrayOf("text/markdown")),
            ClipData.Item(documentUri),
        ).apply {
            addItem(ClipData.Item(parentUri))
        }
        return Intent(ExplorerActionPluginActions.EXECUTE)
            .setDataAndType(documentUri, "text/markdown")
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
            .putExtra(ExplorerActionIntentExtras.ACTION_ID, MarkdownPreviewerPlugin.ID)
            .putExtra(ExplorerActionIntentExtras.PROTOCOL_VERSION, ExplorerActionProtocol.VERSION)
            .putExtra(ExplorerActionIntentExtras.DISPLAY_NAME, "README.md")
            .putExtra(ExplorerActionIntentExtras.SIZE, 1024L)
            .putExtra(ExplorerActionIntentExtras.PARENT_URI, parentUri)
            .putExtra(
                ExplorerActionIntentExtras.SOURCE_SURFACE,
                ExplorerActionIntentValues.SOURCE_SURFACE_MAIN,
            )
            .apply { this.clipData = clipData }
    }
}
