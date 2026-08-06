package io.github.supermonster003.autojs6.plugin.markdownpreview

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.text.format.Formatter
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.supermonster003.autojs6.plugin.markdownpreview.databinding.ActivityMarkdownPreviewBinding
import io.github.supermonster003.autojs6.plugin.markdownpreview.databinding.DialogMarkdownPreviewSettingsBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class MarkdownPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarkdownPreviewBinding
    private lateinit var previewRequest: MarkdownPreviewRequest
    private lateinit var preferences: MarkdownPreviewPreferences
    private lateinit var webController: MarkdownPreviewWebController

    private val renderer = MarkdownPreviewRenderer()
    private var loadJob: Job? = null
    private var loadGeneration = 0
    private var fullscreenMode = false

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (fullscreenMode) {
                setFullscreenMode(false)
                return
            }
            isEnabled = false
            onBackPressedDispatcher.onBackPressed()
            isEnabled = true
        }
    }

    private val customCssPicker = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let(::importCustomCss)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        previewRequest = MarkdownPreviewIntentPolicy.resolve(intent) ?: run {
            Toast.makeText(this, R.string.text_cannot_read_file, Toast.LENGTH_LONG).show()
            finish()
            return
        }
        preferences = MarkdownPreviewPreferences(this)
        fullscreenMode = savedInstanceState?.getBoolean(STATE_FULLSCREEN_MODE)
            ?: preferences.startInFullscreenMode

        binding = ActivityMarkdownPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.previewWebView.setBackgroundColor(getColor(R.color.window_background))
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = previewRequest.displayName
        }

        setFullscreenMode(fullscreenMode, invalidateMenu = false)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        webController = MarkdownPreviewWebController(
            context = this,
            webView = binding.previewWebView,
            resourceRoot = previewRequest.parentUri,
            onExternalLink = ::openExternalLink,
            onPageFinished = {
                binding.loadingIndicator.isVisible = false
            },
        )
        loadPreview()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_markdown_preview, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.action_clear_custom_css)?.isVisible = preferences.hasCustomCss
        menu.findItem(R.id.action_fullscreen_mode)?.isChecked = fullscreenMode
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> true.also { finish() }
        R.id.action_refresh -> true.also { loadPreview() }
        R.id.action_preview_theme -> true.also { showThemeDialog() }
        R.id.action_import_custom_css -> true.also { openCustomCssPicker() }
        R.id.action_clear_custom_css -> true.also { clearCustomCss() }
        R.id.action_fullscreen_mode -> true.also { setFullscreenMode(!fullscreenMode) }
        R.id.action_markdown_preview_settings -> true.also { showSettingsDialog() }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_FULLSCREEN_MODE, fullscreenMode)
        super.onSaveInstanceState(outState)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && fullscreenMode && ::binding.isInitialized) {
            applyFullscreenMode()
        }
    }

    override fun onDestroy() {
        loadGeneration++
        loadJob?.cancel()
        if (::webController.isInitialized) {
            webController.destroy()
        }
        super.onDestroy()
    }

    private fun loadPreview() {
        val generation = ++loadGeneration
        loadJob?.cancel()
        binding.loadingIndicator.isVisible = true
        binding.errorText.isVisible = false

        val selectedTheme = preferences.theme
        val stylesheetName = selectedTheme.stylesheetName(isNightMode())
        loadJob = lifecycleScope.launch {
            try {
                val input = withContext(Dispatchers.IO) {
                    RenderInput(
                        source = MarkdownPreviewTextReader(contentResolver).read(
                            previewRequest.documentUri,
                            MAX_MARKDOWN_BYTES,
                        ),
                        customCss = if (selectedTheme == MarkdownPreviewTheme.CUSTOM) {
                            preferences.readCustomCss()
                        } else {
                            null
                        },
                    )
                }
                val html = withContext(Dispatchers.Default) {
                    synchronized(renderer) {
                        renderer.render(
                            markdown = input.source,
                            stylesheetName = stylesheetName,
                            customCss = input.customCss,
                        )
                    }
                }
                if (generation != loadGeneration) return@launch
                binding.previewWebView.isVisible = true
                webController.show(html)
            } catch (_: CancellationException) {
                // A newer load request superseded this one.
            } catch (error: Throwable) {
                if (generation == loadGeneration) {
                    showLoadError(error)
                }
            }
        }
    }

    private fun showLoadError(error: Throwable) {
        binding.loadingIndicator.isVisible = false
        binding.previewWebView.isVisible = false
        binding.errorText.isVisible = true
        binding.errorText.text = when (error) {
            is MarkdownTooLargeException -> getString(
                R.string.error_markdown_file_too_large,
                Formatter.formatShortFileSize(this, error.limitBytes.toLong()),
            )
            else -> getString(R.string.text_cannot_read_file)
        }
    }

    private fun showThemeDialog() {
        val themes = buildList {
            addAll(MarkdownPreviewTheme.BUILT_IN)
            if (preferences.hasCustomCss) add(MarkdownPreviewTheme.CUSTOM)
        }
        var pendingIndex = themes.indexOf(preferences.theme).coerceAtLeast(0)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.text_preview_theme)
            .setSingleChoiceItems(
                themes.map { getString(it.labelRes) }.toTypedArray(),
                pendingIndex,
            ) { _, which ->
                pendingIndex = which
            }
            .setNegativeButton(R.string.dialog_button_cancel, null)
            .setPositiveButton(R.string.dialog_button_confirm) { _, _ ->
                preferences.theme = themes[pendingIndex]
                loadPreview()
            }
            .show()
    }

    private fun showSettingsDialog() {
        val settingsBinding = DialogMarkdownPreviewSettingsBinding.inflate(layoutInflater).apply {
            startInFullscreenMode.isChecked = preferences.startInFullscreenMode
        }
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.text_settings)
            .setView(settingsBinding.root)
            .setNegativeButton(R.string.dialog_button_cancel, null)
            .setPositiveButton(R.string.dialog_button_confirm) { _, _ ->
                preferences.startInFullscreenMode = settingsBinding.startInFullscreenMode.isChecked
            }
            .show()
    }

    private fun setFullscreenMode(enabled: Boolean, invalidateMenu: Boolean = true) {
        val changed = fullscreenMode != enabled
        fullscreenMode = enabled
        binding.appBar.isVisible = !enabled
        applyFullscreenMode()
        if (changed && invalidateMenu) {
            invalidateOptionsMenu()
        }
    }

    private fun applyFullscreenMode() {
        WindowInsetsControllerCompat(window, window.decorView).apply {
            if (fullscreenMode) {
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                hide(WindowInsetsCompat.Type.systemBars())
            } else {
                show(WindowInsetsCompat.Type.systemBars())
            }
        }
        ViewCompat.requestApplyInsets(binding.root)
    }

    private fun openCustomCssPicker() {
        customCssPicker.launch(arrayOf("text/css", "text/plain"))
    }

    private fun importCustomCss(uri: Uri) {
        lifecycleScope.launch {
            val result = runCatching {
                withContext(Dispatchers.IO) {
                    val input = contentResolver.openInputStream(uri)
                        ?: throw IOException("Cannot open the selected CSS file")
                    input.use(preferences::importCustomCss)
                }
            }
            result.onSuccess {
                preferences.theme = MarkdownPreviewTheme.CUSTOM
                Toast.makeText(
                    this@MarkdownPreviewActivity,
                    R.string.text_custom_css_imported,
                    Toast.LENGTH_SHORT,
                ).show()
                invalidateOptionsMenu()
                loadPreview()
            }.onFailure { error ->
                val message = when (error) {
                    is CustomCssTooLargeException -> getString(
                        R.string.error_custom_css_file_too_large,
                        Formatter.formatShortFileSize(this@MarkdownPreviewActivity, error.limitBytes.toLong()),
                    )
                    else -> getString(R.string.text_failed_to_import)
                }
                Toast.makeText(this@MarkdownPreviewActivity, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun clearCustomCss() {
        preferences.clearCustomCss()
        Toast.makeText(this, R.string.text_custom_css_cleared, Toast.LENGTH_SHORT).show()
        invalidateOptionsMenu()
        loadPreview()
    }

    private fun openExternalLink(uri: Uri) {
        if (uri.scheme?.lowercase(Locale.ROOT) !in setOf("http", "https")) return
        try {
            startActivity(Intent(Intent.ACTION_VIEW, uri).addCategory(Intent.CATEGORY_BROWSABLE))
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(this, R.string.text_cannot_open_link, Toast.LENGTH_SHORT).show()
        } catch (_: SecurityException) {
            Toast.makeText(this, R.string.text_cannot_open_link, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNightMode(): Boolean =
        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
            Configuration.UI_MODE_NIGHT_YES

    private data class RenderInput(
        val source: String,
        val customCss: String?,
    )

    companion object {
        const val MAX_MARKDOWN_BYTES = 8 * 1024 * 1024

        private const val STATE_FULLSCREEN_MODE = "fullscreen_mode"
    }
}
