package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkdownPreviewerIntentPolicyTest {

    @Test
    fun markdownCanBeResolvedFromExtensionOrMimeType() {
        assertTrue(MarkdownPreviewerIntentPolicy.isMarkdown("application/octet-stream", "README.md"))
        assertTrue(MarkdownPreviewerIntentPolicy.isMarkdown("text/x-markdown; charset=utf-8", "previewer"))
        assertTrue(MarkdownPreviewerIntentPolicy.isMarkdown(null, "report.QMD"))
    }

    @Test
    fun conflictingOrUnsupportedTypesAreRejected() {
        assertFalse(MarkdownPreviewerIntentPolicy.isMarkdown("text/html", "README.md"))
        assertFalse(MarkdownPreviewerIntentPolicy.isMarkdown("text/markdown", "index.html"))
        assertFalse(MarkdownPreviewerIntentPolicy.isMarkdown("text/html", "previewer"))
        assertFalse(MarkdownPreviewerIntentPolicy.isMarkdown("text/plain", "notes.txt"))
    }

    @Test
    fun displayNameNeverRetainsAPathOrControlCharacters() {
        assertEquals(
            "README.md",
            MarkdownPreviewerIntentPolicy.sanitizeDisplayName("C:\\private\\folder/README.md\u0000"),
        )
        assertNull(MarkdownPreviewerIntentPolicy.sanitizeDisplayName("\u0000\n\t"))
    }
}
