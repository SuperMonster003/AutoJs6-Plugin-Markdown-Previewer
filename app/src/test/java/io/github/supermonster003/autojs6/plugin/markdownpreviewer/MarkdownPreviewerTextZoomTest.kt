package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import org.junit.Assert.assertEquals
import org.junit.Test

class MarkdownPreviewerTextZoomTest {

    @Test
    fun defaultPercentageIsAValidStep() {
        assertEquals(
            MarkdownPreviewerTextZoom.DEFAULT_PERCENT,
            MarkdownPreviewerTextZoom.normalize(MarkdownPreviewerTextZoom.DEFAULT_PERCENT),
        )
    }

    @Test
    fun outOfRangePercentagesAreClamped() {
        assertEquals(
            MarkdownPreviewerTextZoom.MIN_PERCENT,
            MarkdownPreviewerTextZoom.normalize(Int.MIN_VALUE),
        )
        assertEquals(
            MarkdownPreviewerTextZoom.MAX_PERCENT,
            MarkdownPreviewerTextZoom.normalize(Int.MAX_VALUE),
        )
    }

    @Test
    fun percentagesAreSnappedToTheNearestStep() {
        assertEquals(75, MarkdownPreviewerTextZoom.normalize(87))
        assertEquals(100, MarkdownPreviewerTextZoom.normalize(88))
        assertEquals(150, MarkdownPreviewerTextZoom.normalize(162))
        assertEquals(175, MarkdownPreviewerTextZoom.normalize(163))
        assertEquals(175, MarkdownPreviewerTextZoom.normalize(187))
        assertEquals(200, MarkdownPreviewerTextZoom.normalize(188))
    }
}
