@file:Suppress("DEPRECATION")

package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import android.content.ClipData
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.inspector.WindowInspector
import android.webkit.WebView
import android.widget.Button
import android.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.switchmaterial.SwitchMaterial
import org.autojs.plugin.explorer.api.ExplorerActionIntentExtras
import org.autojs.plugin.explorer.api.ExplorerActionIntentValues
import org.autojs.plugin.explorer.api.ExplorerActionPluginActions
import org.junit.Assert.*
import org.junit.Assume.assumeTrue
import org.junit.Test
import java.io.File

class PreviewerUiInstrumentationTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext

    @Test
    fun primaryAndOverflowIntentsBothOpenAndSettingsCanBeCancelled() {
        assumeTrue(Build.VERSION.SDK_INT >= 29)
        for (actionId in listOf(MarkdownPreviewerPlugin.PRIMARY_ACTION_ID, MarkdownPreviewerPlugin.ID)) {
            val request = request(actionId)
            assertNotNull(MarkdownPreviewerIntentPolicy.resolve(request))
            val activity = instrumentation.startActivitySync(request) as MarkdownPreviewerActivity
            try {
                await { activity.findViewById<WebView>(R.id.previewer_web_view)?.url?.startsWith("https://") == true }
                main {
                    val item = PopupMenu(activity, View(activity)).menu.add(0, R.id.action_markdown_previewer_settings, 0, "")
                    assertTrue(activity.onOptionsItemSelected(item))
                }
                await { dialogButton() != null }
                main {
                    val button = requireNotNull(dialogButton())
                    val dialog = button.rootView
                    val toggle = dialog.findViewById<SwitchMaterial>(R.id.start_in_fullscreen_mode)
                    assertNotNull(toggle)
                    assertFalse(toggle.showText)
                    assertTrue(toggle.isClickable)
                    assertNotNull(toggle.thumbDrawable)
                    assertNotNull(toggle.trackDrawable)
                    val before = toggle.isChecked
                    toggle.performClick()
                    assertEquals(!before, toggle.isChecked)
                    assertEquals(button.context.getColor(R.color.dialog_foreground), button.currentTextColor)
                    assertEquals(activity.getString(R.string.dialog_button_confirm), button.text.toString())
                    dialog.findViewById<Button>(android.R.id.button2).performClick()
                }
                await { dialogButton() == null }
                assertFalse(activity.isFinishing)
                main {
                    val item = PopupMenu(activity, View(activity)).menu.add(0, R.id.action_previewer_theme, 0, "")
                    activity.onOptionsItemSelected(item)
                }
                await { dialogButton() != null }
                main {
                    val button = requireNotNull(dialogButton())
                    assertEquals(button.context.getColor(R.color.dialog_foreground), button.currentTextColor)
                    val checked = android.util.TypedValue()
                    val unchecked = android.util.TypedValue()
                    button.context.theme.resolveAttribute(androidx.appcompat.R.attr.colorControlActivated, checked, true)
                    button.context.theme.resolveAttribute(androidx.appcompat.R.attr.colorControlNormal, unchecked, true)
                    assertEquals(checked.data, unchecked.data)
                    button.rootView.findViewById<Button>(android.R.id.button2).performClick()
                }
            } finally {
                main { activity.finish() }
                instrumentation.waitForIdleSync()
            }
        }
    }

    @Test
    fun customCssColorsAllThreeBarsAndKeepsIconsLegible() {
        val preferences = MarkdownPreviewerPreferences(context)
        val originalTheme = preferences.theme
        val originalCss = preferences.readCustomCss()
        try {
            preferences.importCustomCss("html, body, .markdown-body { background: #123456 !important; color: white !important; }".byteInputStream())
            preferences.theme = MarkdownPreviewerTheme.CUSTOM
            val activity = instrumentation.startActivitySync(request(MarkdownPreviewerPlugin.PRIMARY_ACTION_ID)) as MarkdownPreviewerActivity
            try {
                await {
                    (activity.findViewById<View>(R.id.toolbar).background as? android.graphics.drawable.ColorDrawable)?.color == 0xFF123456.toInt()
                }
                main {
                    // Android 15 enforces transparent system bars; the colored content root
                    // is then the visible system-bar background.
                    val contentRoot = activity.findViewById<android.view.ViewGroup>(android.R.id.content).getChildAt(0)
                    assertEquals(0xFF123456.toInt(), (contentRoot.background as android.graphics.drawable.ColorDrawable).color)
                    if (Build.VERSION.SDK_INT < 35) assertEquals(0xFF123456.toInt(), activity.window.statusBarColor)
                    if (Build.VERSION.SDK_INT < 35) assertEquals(0xFF123456.toInt(), activity.window.navigationBarColor)
                    assertFalse(androidx.core.view.WindowInsetsControllerCompat(activity.window, activity.window.decorView).isAppearanceLightStatusBars)
                }
            } finally {
                main { activity.finish() }
            }
        } finally {
            if (originalCss != null) preferences.importCustomCss(originalCss.byteInputStream()) else preferences.clearCustomCss()
            preferences.theme = originalTheme
        }
    }

    @Test
    fun dialogControlsUseBlackOrWhiteInBothNightModes() {
        for (night in listOf(false, true)) {
            val activity = instrumentation.startActivitySync(
                Intent(context, PreviewerAppearanceTestActivity::class.java)
                    .putExtra("dark_mode", night).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            ) as PreviewerAppearanceTestActivity
            try {
                main {
                    assertEquals(night, activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
                    val dialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(activity)
                        .setSingleChoiceItems(arrayOf("One", "Two"), 0, null)
                        .setPositiveButton(R.string.dialog_button_confirm, null)
                        .setNegativeButton(R.string.dialog_button_cancel, null)
                        .show()
                    try {
                        val color = if (night) 0xFFFFFFFF.toInt() else 0xFF202124.toInt()
                        assertEquals(color, dialog.getButton(android.content.DialogInterface.BUTTON_POSITIVE).currentTextColor)
                        assertEquals(color, dialog.getButton(android.content.DialogInterface.BUTTON_NEGATIVE).currentTextColor)
                        val checked = android.util.TypedValue()
                        val unchecked = android.util.TypedValue()
                        dialog.context.theme.resolveAttribute(androidx.appcompat.R.attr.colorControlActivated, checked, true)
                        dialog.context.theme.resolveAttribute(androidx.appcompat.R.attr.colorControlNormal, unchecked, true)
                        assertEquals(color, checked.data)
                        assertEquals(checked.data, unchecked.data)
                    } finally {
                        dialog.dismiss()
                    }
                }
            } finally {
                main { activity.finish() }
            }
        }
    }

    @Test
    fun hostConfigurationOverridesLanguageAndNightModeWithoutMutatingBaseResources() {
        val original = Configuration(context.resources.configuration)
        val snapshot = PreviewerHostAppearance("fr", true)
        val wrapped = snapshot.wrap(context)
        assertEquals("fr", wrapped.resources.configuration.locales[0].language)
        assertEquals(Configuration.UI_MODE_NIGHT_YES, wrapped.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK)
        assertEquals(original, context.resources.configuration)
        assertNull(PreviewerHostAppearance.fromBundle(Bundle()))
        assertNull(PreviewerHostAppearance.fromBundle(Bundle().apply { putInt("protocolVersion", 99) }))
    }

    @Test
    fun installedHostAppearanceIsAppliedToTheViewer() {
        val host = PreviewerHostAppearance.read(context)
        assumeTrue("Requires an official AutoJs6 host with the settings provider", host != null)
        val activity = instrumentation.startActivitySync(request(MarkdownPreviewerPlugin.PRIMARY_ACTION_ID)) as MarkdownPreviewerActivity
        try {
            assertEquals(host!!.languageTag, activity.resources.configuration.locales[0].toLanguageTag())
            assertEquals(host.darkMode, activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
        } finally {
            main { activity.finish() }
        }
    }

    private fun request(actionId: String): Intent {
        val directory = File(context.filesDir, "markdown-previewer-instrumentation/appearance").apply { mkdirs() }
        val file = File(directory, "appearance.md").apply { writeText("# Appearance\n\nSettings regression test.") }
        val documentUri = FileProvider.getUriForFile(context, context.packageName + ".test.files", file)
        val parentUri = FileProvider.getUriForFile(context, context.packageName + ".test.files", directory)
        return Intent(context, MarkdownPreviewerActivity::class.java)
            .setAction(ExplorerActionPluginActions.EXECUTE)
            .setDataAndType(documentUri, "text/markdown")
            .putExtra(ExplorerActionIntentExtras.ACTION_ID, actionId)
            .putExtra(ExplorerActionIntentExtras.PROTOCOL_VERSION, MarkdownPreviewerPlugin.PROTOCOL_VERSION)
            .putExtra(ExplorerActionIntentExtras.HOST_PACKAGE_NAME, "org.autojs.autojs6")
            .putExtra(ExplorerActionIntentExtras.HOST_VERSION_CODE, 5279L)
            .putExtra(ExplorerActionIntentExtras.SOURCE_SURFACE, ExplorerActionIntentValues.SOURCE_SURFACE_MAIN)
            .putExtra(ExplorerActionIntentExtras.PARENT_URI, parentUri)
            .putExtra(ExplorerActionIntentExtras.DISPLAY_NAME, "appearance.md")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
            .apply {
                clipData = ClipData.newRawUri("Document", documentUri).apply { addItem(ClipData.Item(parentUri)) }
            }
    }

    private fun dialogButton(): Button? = WindowInspector.getGlobalWindowViews()
        .firstNotNullOfOrNull { it.findViewById<Button>(android.R.id.button1)?.takeIf(View::isShown) }

    private fun await(condition: () -> Boolean) {
        val deadline = SystemClock.uptimeMillis() + 10000
        while (SystemClock.uptimeMillis() < deadline) {
            var ready = false
            main { ready = condition() }
            if (ready) return
            SystemClock.sleep(50)
        }
        fail("Timed out waiting for the viewer or settings dialog")
    }

    private fun main(action: () -> Unit) = instrumentation.runOnMainSync(action)
}
