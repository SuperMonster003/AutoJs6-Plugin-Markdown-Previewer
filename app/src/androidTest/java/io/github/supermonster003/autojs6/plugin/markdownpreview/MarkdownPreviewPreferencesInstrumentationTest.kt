package io.github.supermonster003.autojs6.plugin.markdownpreview

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MarkdownPreviewPreferencesInstrumentationTest {

    @Test
    fun textZoomPersistsAcrossPreferenceInstances() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val hadOriginalValue = sharedPreferences.contains(KEY_TEXT_ZOOM_PERCENT)
        val originalValue = sharedPreferences.getInt(
            KEY_TEXT_ZOOM_PERCENT,
            MarkdownPreviewTextZoom.DEFAULT_PERCENT,
        )

        try {
            MarkdownPreviewPreferences(context).textZoomPercent = 175
            assertEquals(175, MarkdownPreviewPreferences(context).textZoomPercent)

            MarkdownPreviewPreferences(context).textZoomPercent = Int.MAX_VALUE
            assertEquals(
                MarkdownPreviewTextZoom.MAX_PERCENT,
                MarkdownPreviewPreferences(context).textZoomPercent,
            )
        } finally {
            sharedPreferences.edit().apply {
                if (hadOriginalValue) {
                    putInt(KEY_TEXT_ZOOM_PERCENT, originalValue)
                } else {
                    remove(KEY_TEXT_ZOOM_PERCENT)
                }
            }.commit()
        }
    }

    companion object {
        private const val PREFERENCES_NAME = "markdown_preview"
        private const val KEY_TEXT_ZOOM_PERCENT = "text_zoom_percent"
    }
}
