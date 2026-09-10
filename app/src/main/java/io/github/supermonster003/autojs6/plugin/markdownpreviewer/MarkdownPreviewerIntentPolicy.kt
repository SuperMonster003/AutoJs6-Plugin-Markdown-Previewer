package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import org.autojs.plugin.explorer.api.ExplorerActionIntentExtras
import org.autojs.plugin.explorer.api.ExplorerActionIntentValues
import org.autojs.plugin.explorer.api.ExplorerActionPluginActions
import java.util.Locale

private enum class PreviewerFormat {
    MARKDOWN,
    HTML,
}

internal data class MarkdownPreviewerRequest(
    val documentUri: Uri,
    val parentUri: Uri,
    val displayName: String,
)

/** Validates the complete, URI-only explorer action contract before any content is opened. */
internal object MarkdownPreviewerIntentPolicy {

    private const val MAX_DISPLAY_NAME_LENGTH = 255

    private val markdownExtensions = setOf(
        "md", "markdown", "mdown", "mkd", "mkdn", "mdwn", "mdtext", "mdtxt", "rmd", "qmd",
    )
    private val htmlExtensions = setOf("html", "htm", "shtm", "shtml", "xht", "xhtml")
    private val markdownMimeTypes = setOf("text/markdown", "text/x-markdown")
    private val htmlMimeTypes = setOf("text/html", "application/xhtml+xml")

    fun resolve(intent: Intent): MarkdownPreviewerRequest? {
        if (intent.action != ExplorerActionPluginActions.EXECUTE) return null
        if (intent.getStringExtra(ExplorerActionIntentExtras.ACTION_ID) !in
            setOf(MarkdownPreviewerPlugin.ID, MarkdownPreviewerPlugin.PRIMARY_ACTION_ID)) return null
        if (
            intent.getIntExtra(ExplorerActionIntentExtras.PROTOCOL_VERSION, Int.MIN_VALUE) !=
            MarkdownPreviewerPlugin.PROTOCOL_VERSION
        ) {
            return null
        }
        if (
            intent.getStringExtra(ExplorerActionIntentExtras.SOURCE_SURFACE) !=
            ExplorerActionIntentValues.SOURCE_SURFACE_MAIN
        ) {
            return null
        }

        val requiredFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
        if (intent.flags and requiredFlags != requiredFlags) return null

        val documentUri = intent.data?.takeIf(::isPlainContentUri) ?: return null
        val parentUri = intent.parcelableUriExtra(ExplorerActionIntentExtras.PARENT_URI)
            ?.takeIf(::isPlainContentUri)
            ?: return null
        if (!MarkdownPreviewerPathPolicy.isDescendant(parentUri, documentUri)) return null

        val clipData = intent.clipData ?: return null
        if (clipData.itemCount <= ExplorerActionIntentValues.CLIP_ITEM_PARENT_INDEX) return null
        if (clipData.getItemAt(ExplorerActionIntentValues.CLIP_ITEM_TARGET_INDEX).uri != documentUri) return null
        if (clipData.getItemAt(ExplorerActionIntentValues.CLIP_ITEM_PARENT_INDEX).uri != parentUri) return null

        val suppliedName = intent.getStringExtra(ExplorerActionIntentExtras.DISPLAY_NAME)
        val displayName = sanitizeDisplayName(suppliedName)
            ?: sanitizeDisplayName(documentUri.lastPathSegment)
            ?: return null
        if (!isMarkdown(intent.type, displayName)) return null

        val declaredSize = intent.getLongExtra(ExplorerActionIntentExtras.SIZE, -1L)
        if (declaredSize > MarkdownPreviewerActivity.MAX_MARKDOWN_BYTES) return null

        return MarkdownPreviewerRequest(documentUri, parentUri, displayName)
    }

    fun isMarkdown(mimeType: String?, displayName: String): Boolean {
        val extensionFormat = displayName.substringAfterLast('.', missingDelimiterValue = "")
            .lowercase(Locale.ROOT)
            .let { extension ->
                when (extension) {
                    in markdownExtensions -> PreviewerFormat.MARKDOWN
                    in htmlExtensions -> PreviewerFormat.HTML
                    else -> null
                }
            }
        val mimeFormat = mimeType
            ?.substringBefore(';')
            ?.trim()
            ?.lowercase(Locale.ROOT)
            .let { normalized ->
                when (normalized) {
                    in markdownMimeTypes -> PreviewerFormat.MARKDOWN
                    in htmlMimeTypes -> PreviewerFormat.HTML
                    else -> null
                }
            }
        if (extensionFormat != null && mimeFormat != null && extensionFormat != mimeFormat) return false
        return extensionFormat == PreviewerFormat.MARKDOWN || mimeFormat == PreviewerFormat.MARKDOWN
    }

    fun sanitizeDisplayName(value: String?): String? {
        val leaf = value
            ?.replace('\\', '/')
            ?.substringAfterLast('/')
            ?.filterNot { it == '\u0000' || it.code < 0x20 || it.code == 0x7f }
            ?.trim()
            ?.take(MAX_DISPLAY_NAME_LENGTH)
            .orEmpty()
        return leaf.takeIf(String::isNotEmpty)
    }

    private fun isPlainContentUri(uri: Uri): Boolean =
        uri.scheme.equals(ContentResolver.SCHEME_CONTENT, ignoreCase = true) &&
            !uri.authority.isNullOrBlank() &&
            uri.query == null &&
            uri.fragment == null

    @Suppress("DEPRECATION")
    private fun Intent.parcelableUriExtra(name: String): Uri? = getParcelableExtra(name)
}
