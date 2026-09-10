package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import org.junit.Assert.assertEquals
import org.junit.Test

class MarkdownPreviewerPrintPolicyTest {

    @Test
    fun documentNameUsesOnlyTheSanitizedLeafName() {
        assertEquals(
            "Guide.md",
            MarkdownPreviewerPrintPolicy.documentName("folder\\nested/Guide.md"),
        )
    }

    @Test
    fun blankOrControlOnlyNameUsesStableFallback() {
        assertEquals("Markdown", MarkdownPreviewerPrintPolicy.documentName(null))
        assertEquals("Markdown", MarkdownPreviewerPrintPolicy.documentName(" \u0000\u001f "))
    }

    @Test
    fun documentNameRetainsIntentPolicyLengthLimit() {
        assertEquals(255, MarkdownPreviewerPrintPolicy.documentName("a".repeat(300)).length)
    }
}
