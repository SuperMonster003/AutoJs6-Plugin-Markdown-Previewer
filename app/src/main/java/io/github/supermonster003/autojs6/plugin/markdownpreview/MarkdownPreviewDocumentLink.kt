package io.github.supermonster003.autojs6.plugin.markdownpreview

import java.io.ByteArrayOutputStream
import java.net.URI
import java.nio.ByteBuffer
import java.nio.charset.CodingErrorAction

internal data class MarkdownPreviewDocumentLink(
    val relativePath: String,
    val displayName: String,
    val fragment: String?,
)

/**
 * Converts a WebView navigation on the private virtual origin back into a path below the
 * explorer-provided parent URI. The browser may normalize ordinary `.` and `..` segments before
 * this policy sees the request; any unresolved or percent-encoded traversal is rejected here.
 */
internal object MarkdownPreviewDocumentLinkPolicy {

    private const val MAX_ENCODED_PATH_LENGTH = 8 * 1024
    private const val MAX_FRAGMENT_LENGTH = 512

    fun parseVirtualUrl(value: String): MarkdownPreviewDocumentLink? {
        if (value.length > MAX_ENCODED_PATH_LENGTH + MAX_FRAGMENT_LENGTH + 128) return null
        val uri = runCatching { URI(value) }.getOrNull() ?: return null
        if (!uri.isAbsolute || uri.isOpaque) return null
        if (!uri.scheme.equals("https", ignoreCase = true)) return null
        if (!uri.rawAuthority.equals(MarkdownPreviewWebOrigin.DOMAIN, ignoreCase = true)) return null
        if (uri.rawUserInfo != null || uri.port != -1 || uri.rawQuery != null) return null

        val rawPath = uri.rawPath ?: return null
        if (!rawPath.startsWith(MarkdownPreviewWebOrigin.DOCUMENT_PATH_PREFIX)) return null
        val rawRelativePath = rawPath.removePrefix(MarkdownPreviewWebOrigin.DOCUMENT_PATH_PREFIX)
        val relativePath = decodeRelativePath(rawRelativePath) ?: return null
        val displayName = MarkdownPreviewIntentPolicy.sanitizeDisplayName(relativePath.substringAfterLast('/'))
            ?: return null
        if (!MarkdownPreviewIntentPolicy.isMarkdown(null, displayName)) return null

        val fragment = uri.rawFragment
            ?.let(::decodeUtf8Component)
            ?.takeIf(String::isNotEmpty)
        if (uri.rawFragment != null && fragment == null && uri.rawFragment!!.isNotEmpty()) return null
        if (!isSafeFragment(fragment)) return null

        return MarkdownPreviewDocumentLink(
            relativePath = relativePath,
            displayName = displayName,
            fragment = fragment,
        )
    }

    fun buildVirtualUrl(relativePath: String, fragment: String? = null): String? {
        if (!MarkdownPreviewPathPolicy.isSafeRelativePath(relativePath)) return null
        if (!isSafeFragment(fragment)) return null
        return runCatching {
            URI(
                "https",
                MarkdownPreviewWebOrigin.DOMAIN,
                "${MarkdownPreviewWebOrigin.DOCUMENT_PATH_PREFIX}$relativePath",
                null,
                fragment,
            ).toASCIIString()
        }.getOrNull()
    }

    fun isSafeFragment(fragment: String?): Boolean =
        fragment == null ||
            fragment.length <= MAX_FRAGMENT_LENGTH &&
            fragment.none(::isControlCharacter)

    private fun decodeRelativePath(rawPath: String): String? {
        if (
            rawPath.isEmpty() ||
            rawPath.length > MAX_ENCODED_PATH_LENGTH ||
            rawPath.startsWith('/') ||
            rawPath.endsWith('/') ||
            "//" in rawPath
        ) {
            return null
        }
        val decodedSegments = rawPath.split('/').map { rawSegment ->
            val segment = decodeUtf8Component(rawSegment) ?: return null
            if (
                segment.isEmpty() ||
                segment == "." ||
                segment == ".." ||
                '%' in segment ||
                '/' in segment ||
                '\\' in segment
            ) {
                return null
            }
            segment
        }
        val decodedPath = decodedSegments.joinToString("/")
        return decodedPath.takeIf(MarkdownPreviewPathPolicy::isSafeRelativePath)
    }

    private fun decodeUtf8Component(rawValue: String): String? {
        val bytes = ByteArrayOutputStream(rawValue.length)
        var index = 0
        while (index < rawValue.length) {
            if (rawValue[index] == '%') {
                if (index + 2 >= rawValue.length) return null
                val high = rawValue[index + 1].digitToIntOrNull(16) ?: return null
                val low = rawValue[index + 2].digitToIntOrNull(16) ?: return null
                bytes.write(high shl 4 or low)
                index += 3
            } else {
                val codePoint = rawValue.codePointAt(index)
                val encoded = String(Character.toChars(codePoint)).toByteArray(Charsets.UTF_8)
                bytes.write(encoded, 0, encoded.size)
                index += Character.charCount(codePoint)
            }
        }
        return runCatching {
            Charsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(bytes.toByteArray()))
                .toString()
        }.getOrNull()
    }

    private fun isControlCharacter(character: Char): Boolean =
        character == '\u0000' || character.code < 0x20 || character.code == 0x7f
}
