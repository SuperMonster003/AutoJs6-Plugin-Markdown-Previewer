package io.github.supermonster003.autojs6.plugin.markdownpreview

import org.junit.Assert.assertEquals
import org.junit.Test

class MarkdownPreviewTextZoomTest {

    @Test
    fun defaultPercentageIsAValidStep() {
        assertEquals(
            MarkdownPreviewTextZoom.DEFAULT_PERCENT,
            MarkdownPreviewTextZoom.normalize(MarkdownPreviewTextZoom.DEFAULT_PERCENT),
        )
    }

    @Test
    fun outOfRangePercentagesAreClamped() {
        assertEquals(
            MarkdownPreviewTextZoom.MIN_PERCENT,
            MarkdownPreviewTextZoom.normalize(Int.MIN_VALUE),
        )
        assertEquals(
            MarkdownPreviewTextZoom.MAX_PERCENT,
            MarkdownPreviewTextZoom.normalize(Int.MAX_VALUE),
        )
    }

    @Test
    fun percentagesAreSnappedToTheNearestStep() {
        assertEquals(75, MarkdownPreviewTextZoom.normalize(87))
        assertEquals(100, MarkdownPreviewTextZoom.normalize(88))
        assertEquals(150, MarkdownPreviewTextZoom.normalize(162))
        assertEquals(175, MarkdownPreviewTextZoom.normalize(163))
        assertEquals(175, MarkdownPreviewTextZoom.normalize(187))
        assertEquals(200, MarkdownPreviewTextZoom.normalize(188))
    }
}
