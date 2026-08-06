package io.github.supermonster003.autojs6.plugin.markdownpreview

import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkdownPreviewIntentPolicyTest {

    @Test
    fun markdownCanBeResolvedFromExtensionOrMimeType() {
        assertTrue(MarkdownPreviewIntentPolicy.isMarkdown("application/octet-stream", "README.md"))
        assertTrue(MarkdownPreviewIntentPolicy.isMarkdown("text/x-markdown; charset=utf-8", "preview"))
        assertTrue(MarkdownPreviewIntentPolicy.isMarkdown(null, "report.QMD"))
    }

    @Test
    fun conflictingOrUnsupportedTypesAreRejected() {
        assertFalse(MarkdownPreviewIntentPolicy.isMarkdown("text/html", "README.md"))
        assertFalse(MarkdownPreviewIntentPolicy.isMarkdown("text/markdown", "index.html"))
        assertFalse(MarkdownPreviewIntentPolicy.isMarkdown("text/html", "preview"))
        assertFalse(MarkdownPreviewIntentPolicy.isMarkdown("text/plain", "notes.txt"))
    }

    @Test
    fun displayNameNeverRetainsAPathOrControlCharacters() {
        assertEquals(
            "README.md",
            MarkdownPreviewIntentPolicy.sanitizeDisplayName("C:\\private\\folder/README.md\u0000"),
        )
        assertNull(MarkdownPreviewIntentPolicy.sanitizeDisplayName("\u0000\n\t"))
    }
}
