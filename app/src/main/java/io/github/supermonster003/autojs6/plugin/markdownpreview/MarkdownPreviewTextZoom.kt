package io.github.supermonster003.autojs6.plugin.markdownpreview

internal object MarkdownPreviewTextZoom {

    const val MIN_PERCENT = 75
    const val MAX_PERCENT = 200
    const val STEP_PERCENT = 25
    const val DEFAULT_PERCENT = 100

    fun normalize(percent: Int): Int {
        val clamped = percent.coerceIn(MIN_PERCENT, MAX_PERCENT)
        val stepIndex = (clamped - MIN_PERCENT + STEP_PERCENT / 2) / STEP_PERCENT
        return (MIN_PERCENT + stepIndex * STEP_PERCENT).coerceAtMost(MAX_PERCENT)
    }
}
