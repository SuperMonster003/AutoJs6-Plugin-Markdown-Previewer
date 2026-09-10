package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkdownPreviewerPathPolicyTest {

    @Test
    fun relativeResourcesUseOnlyWhitelistedMimeTypes() {
        val resources = mapOf(
            "styles/site.css" to "text/css",
            "images/picture.PNG" to "image/png",
            "fonts/body.woff2" to "font/woff2",
            "media/movie.mp4" to "video/mp4",
            "media/sound.ogg" to "audio/ogg",
        )

        resources.forEach { (path, expectedMimeType) ->
            assertTrue(path, MarkdownPreviewerPathPolicy.isSafeRelativePath(path))
            assertEquals(expectedMimeType, MarkdownPreviewerPathPolicy.mimeType(path))
        }
    }

    @Test
    fun traversalEncodedAndBackslashPathsAreRejected() {
        val unsafePaths = listOf(
            "../outside.png",
            "images/../picture.png",
            "images/%2e%2e/outside.png",
            "images%2Fpicture.png",
            "images\\picture.png",
            "/images/picture.png",
            "images//picture.png",
            "images/./picture.png",
            "images/picture.png?size=large",
            "images/picture.png#fragment",
            "images/picture.png:alternate",
        )

        unsafePaths.forEach { path ->
            assertFalse("$path should fail validation", MarkdownPreviewerPathPolicy.isSafeRelativePath(path))
        }
    }

    @Test
    fun unsupportedMimeTypesAreRejected() {
        listOf("payload.js", "document.html", "notes.txt", "archive.zip").forEach { path ->
            assertNull(path, MarkdownPreviewerPathPolicy.mimeType(path))
        }
    }

    @Test
    fun ordinaryNestedRelativePathIsAccepted() {
        assertTrue(MarkdownPreviewerPathPolicy.isSafeRelativePath("images/diagrams/previewer.svg"))
        assertTrue(MarkdownPreviewerPathPolicy.isSafeRelativePath("指南/开始 阅读.md"))
    }

    @Test
    fun overlongPathsSegmentsAndSegmentCountsAreRejected() {
        assertFalse(MarkdownPreviewerPathPolicy.isSafeRelativePath("a".repeat(2049)))
        assertFalse(MarkdownPreviewerPathPolicy.isSafeRelativePath("a".repeat(256) + ".md"))
        assertFalse(
            MarkdownPreviewerPathPolicy.isSafeRelativePath(
                List(65) { "segment" }.joinToString("/"),
            ),
        )
    }

    @Test
    fun onlyBundledPreviewerStylesheetsCanLoad() {
        assertTrue(MarkdownPreviewerAssetPolicy.isAllowed("base.css"))
        assertTrue(MarkdownPreviewerAssetPolicy.isAllowed("github-dark.css"))
        assertFalse(MarkdownPreviewerAssetPolicy.isAllowed("html-base.css"))
        assertFalse(MarkdownPreviewerAssetPolicy.isAllowed("../base.css"))
        assertFalse(MarkdownPreviewerAssetPolicy.isAllowed("custom.css"))
    }
}
