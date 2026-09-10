package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MarkdownPreviewerPreferencesInstrumentationTest {

    @Test
    fun textZoomPersistsAcrossPreferenceInstances() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val hadOriginalValue = sharedPreferences.contains(KEY_TEXT_ZOOM_PERCENT)
        val originalValue = sharedPreferences.getInt(
            KEY_TEXT_ZOOM_PERCENT,
            MarkdownPreviewerTextZoom.DEFAULT_PERCENT,
        )

        try {
            MarkdownPreviewerPreferences(context).textZoomPercent = 175
            assertEquals(175, MarkdownPreviewerPreferences(context).textZoomPercent)

            MarkdownPreviewerPreferences(context).textZoomPercent = Int.MAX_VALUE
            assertEquals(
                MarkdownPreviewerTextZoom.MAX_PERCENT,
                MarkdownPreviewerPreferences(context).textZoomPercent,
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
        private const val PREFERENCES_NAME = "markdown_previewer"
        private const val KEY_TEXT_ZOOM_PERCENT = "text_zoom_percent"
    }
}
