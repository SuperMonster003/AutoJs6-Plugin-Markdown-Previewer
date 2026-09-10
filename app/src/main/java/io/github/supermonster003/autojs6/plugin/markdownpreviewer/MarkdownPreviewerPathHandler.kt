package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import android.content.ContentResolver
import android.content.res.AssetManager
import android.net.Uri
import android.webkit.WebResourceResponse
import androidx.webkit.WebViewAssetLoader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.Locale
import java.util.concurrent.atomic.AtomicReference

class MarkdownPreviewerDocumentPathHandler(
    private val contentResolver: ContentResolver,
    rootUri: Uri,
) : WebViewAssetLoader.PathHandler {

    private val resourceRoot = MarkdownPreviewerPathPolicy.normalizeRoot(rootUri)
        ?: throw IllegalArgumentException("Previewer resource root must be a content URI without a query or fragment")
    private val document = AtomicReference<InMemoryDocument?>(null)

    fun updateDocument(
        html: String,
        relativePath: String = MarkdownPreviewerWebOrigin.DOCUMENT_FILE_NAME,
    ) {
        require(MarkdownPreviewerPathPolicy.isSafeRelativePath(relativePath)) {
            "Previewer document path must stay below the virtual document root"
        }
        document.set(
            InMemoryDocument(
                relativePath = relativePath,
                bytes = html.toByteArray(Charsets.UTF_8),
            ),
        )
    }

    fun clearDocument() {
        document.set(null)
    }

    override fun handle(path: String): WebResourceResponse {
        val activeDocument = document.get()
        if (activeDocument != null && path == activeDocument.relativePath) {
            return WebResourceResponse(
                "text/html",
                Charsets.UTF_8.name(),
                200,
                "OK",
                DOCUMENT_RESPONSE_HEADERS,
                ByteArrayInputStream(activeDocument.bytes),
            )
        }

        val resource = MarkdownPreviewerPathPolicy.resolve(resourceRoot, path) ?: return notFound()
        val input = try {
            contentResolver.openInputStream(resource.uri) ?: return notFound()
        } catch (_: IOException) {
            return notFound()
        } catch (_: SecurityException) {
            return notFound()
        } catch (_: RuntimeException) {
            return notFound()
        }
        return WebResourceResponse(
            resource.mimeType,
            null,
            200,
            "OK",
            RESPONSE_HEADERS,
            input,
        )
    }

    private data class InMemoryDocument(
        val relativePath: String,
        val bytes: ByteArray,
    )
}

class MarkdownPreviewerAssetPathHandler(
    private val assetManager: AssetManager,
) : WebViewAssetLoader.PathHandler {

    override fun handle(path: String): WebResourceResponse {
        val assetPath = path.takeIf(MarkdownPreviewerAssetPolicy::isAllowed) ?: return notFound()
        val input = try {
            assetManager.open("$ASSET_ROOT/$assetPath", AssetManager.ACCESS_STREAMING)
        } catch (_: IOException) {
            return notFound()
        } catch (_: SecurityException) {
            return notFound()
        }
        return WebResourceResponse(
            "text/css",
            Charsets.UTF_8.name(),
            200,
            "OK",
            RESPONSE_HEADERS,
            input,
        )
    }

    companion object {
        private const val ASSET_ROOT = "markdown-previewer"
    }
}

internal object MarkdownPreviewerAssetPolicy {
    private val allowedAssets = setOf(
        "base.css",
        "github-dark.css",
        "github-light.css",
        "paper.css",
        "sepia.css",
    )

    fun isAllowed(path: String): Boolean = path in allowedAssets
}

internal object MarkdownPreviewerPathPolicy {

    data class Resource(
        val uri: Uri,
        val mimeType: String,
    )

    private const val MAX_PATH_LENGTH = 2048
    private const val MAX_PATH_SEGMENTS = 64
    private const val MAX_PATH_SEGMENT_LENGTH = 255

    fun normalizeRoot(rootUri: Uri): Uri? {
        if (!rootUri.scheme.equals(ContentResolver.SCHEME_CONTENT, ignoreCase = true)) return null
        if (rootUri.authority.isNullOrBlank() || rootUri.query != null || rootUri.fragment != null) return null
        return rootUri.buildUpon().clearQuery().fragment(null).build()
    }

    fun resolve(rootUri: Uri, path: String): Resource? {
        val mimeType = mimeType(path) ?: return null
        val uri = resolveDescendant(rootUri, path) ?: return null
        return Resource(uri, mimeType)
    }

    fun resolveDescendant(rootUri: Uri, relativePath: String): Uri? {
        val root = normalizeRoot(rootUri) ?: return null
        if (!isSafeRelativePath(relativePath)) return null
        val builder = root.buildUpon()
        relativePath.split('/').forEach(builder::appendPath)
        return builder.build().takeIf { candidate -> isDescendant(root, candidate) }
    }

    fun relativePath(rootUri: Uri, candidateUri: Uri): String? {
        val root = normalizeRoot(rootUri) ?: return null
        if (!isDescendant(root, candidateUri)) return null
        val relativePath = candidateUri.pathSegments
            .drop(root.pathSegments.size)
            .joinToString("/")
        return relativePath.takeIf(::isSafeRelativePath)
    }

    fun isDescendant(rootUri: Uri, candidateUri: Uri): Boolean {
        val root = normalizeRoot(rootUri) ?: return false
        if (!candidateUri.scheme.equals(root.scheme, ignoreCase = true)) return false
        if (!candidateUri.authority.equals(root.authority, ignoreCase = true)) return false
        if (candidateUri.query != null || candidateUri.fragment != null) return false
        val rootSegments = root.pathSegments
        val candidateSegments = candidateUri.pathSegments
        return candidateSegments.size > rootSegments.size &&
            candidateSegments.take(rootSegments.size) == rootSegments &&
            candidateSegments.drop(rootSegments.size).all(::isSafeUriSegment)
    }

    fun isSafeRelativePath(path: String): Boolean {
        if (path.isEmpty() || path.length > MAX_PATH_LENGTH || path.startsWith('/')) return false
        if (path.any { it == '\\' || it == '\u0000' || it == '%' || it == '?' || it == '#' || it == ':' }) {
            return false
        }
        if (path.any { it.code < 0x20 || it.code == 0x7f }) return false
        val segments = path.split('/')
        return segments.size <= MAX_PATH_SEGMENTS && segments.all(::isSafeUriSegment)
    }

    fun mimeType(path: String): String? = when (path.substringAfterLast('.').lowercase(Locale.ROOT)) {
        "css" -> "text/css"
        "png" -> "image/png"
        "jpg", "jpeg" -> "image/jpeg"
        "gif" -> "image/gif"
        "webp" -> "image/webp"
        "avif" -> "image/avif"
        "bmp" -> "image/bmp"
        "ico" -> "image/x-icon"
        "svg" -> "image/svg+xml"
        "heic" -> "image/heic"
        "heif" -> "image/heif"
        "woff" -> "font/woff"
        "woff2" -> "font/woff2"
        "ttf" -> "font/ttf"
        "otf" -> "font/otf"
        "mp4", "m4v" -> "video/mp4"
        "webm" -> "video/webm"
        "ogv" -> "video/ogg"
        "mp3" -> "audio/mpeg"
        "m4a" -> "audio/mp4"
        "ogg", "oga" -> "audio/ogg"
        "wav" -> "audio/wav"
        else -> null
    }

    private fun isSafeUriSegment(segment: String): Boolean =
        segment.isNotEmpty() &&
            segment.length <= MAX_PATH_SEGMENT_LENGTH &&
            segment != "." &&
            segment != ".." &&
            segment.none {
                it == '/' ||
                    it == '\\' ||
                    it == '\u0000' ||
                    it == '%' ||
                    it == '?' ||
                    it == '#' ||
                    it == ':' ||
                    it.code < 0x20 ||
                    it.code == 0x7f
            }
}

private val RESPONSE_HEADERS = mapOf(
    "Cache-Control" to "no-store, max-age=0",
    "X-Content-Type-Options" to "nosniff",
)

private val DOCUMENT_RESPONSE_HEADERS = RESPONSE_HEADERS + mapOf(
    "Content-Security-Policy" to MarkdownPreviewerSecurityPolicy.CONTENT_SECURITY_POLICY,
    "Referrer-Policy" to MarkdownPreviewerSecurityPolicy.REFERRER_POLICY,
)

private fun notFound(): WebResourceResponse = WebResourceResponse(
    "text/plain",
    Charsets.UTF_8.name(),
    404,
    "Not Found",
    RESPONSE_HEADERS,
    ByteArrayInputStream("Not Found".toByteArray(Charsets.UTF_8)),
)
