package io.github.supermonster003.autojs6.plugin.markdownpreview

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkdownPreviewPathPolicyTest {

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
            assertTrue(path, MarkdownPreviewPathPolicy.isSafeRelativePath(path))
            assertEquals(expectedMimeType, MarkdownPreviewPathPolicy.mimeType(path))
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
            assertFalse("$path should fail validation", MarkdownPreviewPathPolicy.isSafeRelativePath(path))
        }
    }

    @Test
    fun unsupportedMimeTypesAreRejected() {
        listOf("payload.js", "document.html", "notes.txt", "archive.zip").forEach { path ->
            assertNull(path, MarkdownPreviewPathPolicy.mimeType(path))
        }
    }

    @Test
    fun ordinaryNestedRelativePathIsAccepted() {
        assertTrue(MarkdownPreviewPathPolicy.isSafeRelativePath("images/diagrams/preview.svg"))
    }

    @Test
    fun onlyBundledPreviewStylesheetsCanLoad() {
        assertTrue(MarkdownPreviewAssetPolicy.isAllowed("base.css"))
        assertTrue(MarkdownPreviewAssetPolicy.isAllowed("github-dark.css"))
        assertFalse(MarkdownPreviewAssetPolicy.isAllowed("html-base.css"))
        assertFalse(MarkdownPreviewAssetPolicy.isAllowed("../base.css"))
        assertFalse(MarkdownPreviewAssetPolicy.isAllowed("custom.css"))
    }
}
