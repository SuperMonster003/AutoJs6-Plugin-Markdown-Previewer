package io.github.supermonster003.autojs6.plugin.markdownpreview

import org.junit.Assert.assertEquals
import org.junit.Test

class MarkdownPreviewPrintPolicyTest {

    @Test
    fun documentNameUsesOnlyTheSanitizedLeafName() {
        assertEquals(
            "Guide.md",
            MarkdownPreviewPrintPolicy.documentName("folder\\nested/Guide.md"),
        )
    }

    @Test
    fun blankOrControlOnlyNameUsesStableFallback() {
        assertEquals("Markdown", MarkdownPreviewPrintPolicy.documentName(null))
        assertEquals("Markdown", MarkdownPreviewPrintPolicy.documentName(" \u0000\u001f "))
    }

    @Test
    fun documentNameRetainsIntentPolicyLengthLimit() {
        assertEquals(255, MarkdownPreviewPrintPolicy.documentName("a".repeat(300)).length)
    }
}
