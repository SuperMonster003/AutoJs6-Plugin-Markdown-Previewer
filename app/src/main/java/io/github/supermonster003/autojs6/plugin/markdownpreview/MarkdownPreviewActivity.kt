package io.github.supermonster003.autojs6.plugin.markdownpreview

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.format.Formatter
import android.text.style.LeadingMarginSpan
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.supermonster003.autojs6.plugin.markdownpreview.databinding.ActivityMarkdownPreviewBinding
import io.github.supermonster003.autojs6.plugin.markdownpreview.databinding.DialogMarkdownPreviewSettingsBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.ArrayDeque
import java.util.Locale
import kotlin.math.roundToInt

class MarkdownPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarkdownPreviewBinding
    private lateinit var rootPreviewRequest: MarkdownPreviewRequest
    private lateinit var currentDocument: PreviewDocument
    private lateinit var preferences: MarkdownPreviewPreferences
    private lateinit var webController: MarkdownPreviewWebController

    private val navigationHistory = ArrayDeque<PreviewDocument>()

    private val renderer = MarkdownPreviewRenderer()
    private var loadJob: Job? = null
    private var findJob: Job? = null
    private var loadGeneration = 0
    private var fullscreenMode = false
    private var documentReady = false
    private var printInProgress = false
    private var activePrintAdapter: MarkdownPreviewPrintDocumentAdapter? = null
    private var destroying = false
    private var findBarVisible = false
    private var documentOutline: List<MarkdownPreviewOutlineEntry> = emptyList()
    private var pendingDocumentOutline: List<MarkdownPreviewOutlineEntry> = emptyList()
    private var pendingDocumentAnchor: String? = null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (closeFindBar()) return
            if (fullscreenMode) {
                setFullscreenMode(false)
                return
            }
            if (navigateBack()) return
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

        rootPreviewRequest = MarkdownPreviewIntentPolicy.resolve(intent) ?: run {
            Toast.makeText(this, R.string.text_cannot_read_file, Toast.LENGTH_LONG).show()
            finish()
            return
        }
        val initialRelativePath = MarkdownPreviewPathPolicy.relativePath(
            rootPreviewRequest.parentUri,
            rootPreviewRequest.documentUri,
        ) ?: run {
            Toast.makeText(this, R.string.text_cannot_read_file, Toast.LENGTH_LONG).show()
            finish()
            return
        }
        val initialDocument = PreviewDocument(rootPreviewRequest, initialRelativePath)
        currentDocument = restoreNavigationState(savedInstanceState, initialDocument)
        preferences = MarkdownPreviewPreferences(this)
        fullscreenMode = savedInstanceState?.getBoolean(STATE_FULLSCREEN_MODE)
            ?: preferences.startInFullscreenMode

        binding = ActivityMarkdownPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.previewWebView.setBackgroundColor(getColor(R.color.window_background))
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = currentDocument.request.displayName
        }
        configureFindBar()

        setFullscreenMode(fullscreenMode, invalidateMenu = false)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        webController = MarkdownPreviewWebController(
            context = this,
            webView = binding.previewWebView,
            resourceRoot = rootPreviewRequest.parentUri,
            onExternalLink = ::openExternalLink,
            onDocumentLink = ::openDocumentLink,
            initialTextZoomPercent = preferences.textZoomPercent,
            onPageFinished = {
                binding.loadingIndicator.isVisible = false
                documentReady = true
                updateDocumentOutline(pendingDocumentOutline)
                pendingDocumentAnchor?.let(webController::scrollToAnchor)
                pendingDocumentAnchor = null
                invalidateOptionsMenu()
            },
            onFindResult = ::updateFindResult,
        )
        loadPreview()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_markdown_preview, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.action_find_in_page)?.isVisible = documentReady
        menu.findItem(R.id.action_document_outline)?.isVisible = documentOutline.isNotEmpty()
        menu.findItem(R.id.action_print_or_export_pdf)?.apply {
            isVisible = documentReady && supportsPrinting()
            isEnabled = !printInProgress
        }
        menu.findItem(R.id.action_clear_custom_css)?.isVisible = preferences.hasCustomCss
        menu.findItem(R.id.action_fullscreen_mode)?.isChecked = fullscreenMode
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> true.also {
            if (!navigateBack()) finish()
        }
        R.id.action_find_in_page -> true.also { showFindBar() }
        R.id.action_document_outline -> true.also { showDocumentOutline() }
        R.id.action_refresh -> true.also { loadPreview() }
        R.id.action_print_or_export_pdf -> true.also { printCurrentDocument() }
        R.id.action_preview_theme -> true.also { showThemeDialog() }
        R.id.action_import_custom_css -> true.also { openCustomCssPicker() }
        R.id.action_clear_custom_css -> true.also { clearCustomCss() }
        R.id.action_fullscreen_mode -> true.also { setFullscreenMode(!fullscreenMode) }
        R.id.action_markdown_preview_settings -> true.also { showSettingsDialog() }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_FULLSCREEN_MODE, fullscreenMode)
        if (::currentDocument.isInitialized) {
            outState.putString(STATE_CURRENT_DOCUMENT_PATH, currentDocument.relativePath)
            outState.putStringArrayList(
                STATE_NAVIGATION_HISTORY_PATHS,
                ArrayList(navigationHistory.map(PreviewDocument::relativePath)),
            )
        }
        super.onSaveInstanceState(outState)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && fullscreenMode && ::binding.isInitialized) {
            applyFullscreenMode()
        }
    }

    override fun onDestroy() {
        destroying = true
        loadGeneration++
        loadJob?.cancel()
        findJob?.cancel()
        activePrintAdapter?.abort()
        activePrintAdapter = null
        if (::webController.isInitialized) {
            webController.destroy()
        }
        super.onDestroy()
    }

    private fun loadPreview() {
        loadDocument(
            target = currentDocument,
            mode = DocumentLoadMode.CURRENT,
            fragment = null,
        )
    }

    private fun loadDocument(
        target: PreviewDocument,
        mode: DocumentLoadMode,
        fragment: String?,
    ) {
        val generation = ++loadGeneration
        loadJob?.cancel()
        findJob?.cancel()
        if (!closeFindBar()) {
            webController.clearFindMatches()
        }
        if (mode == DocumentLoadMode.CURRENT) {
            documentReady = false
            pendingDocumentOutline = emptyList()
            pendingDocumentAnchor = null
            updateDocumentOutline(emptyList())
        }
        binding.loadingIndicator.isVisible = true
        binding.errorText.isVisible = false

        val selectedTheme = preferences.theme
        val stylesheetName = selectedTheme.stylesheetName(isNightMode())
        val frontMatterLabel = getString(R.string.text_yaml_metadata)
        loadJob = lifecycleScope.launch {
            try {
                val input = withContext(Dispatchers.IO) {
                    validateDocumentTarget(target, requireMarkdownExtension = mode == DocumentLoadMode.FORWARD)
                    RenderInput(
                        source = MarkdownPreviewTextReader(contentResolver).read(
                            target.request.documentUri,
                            MAX_MARKDOWN_BYTES,
                        ),
                        customCss = if (selectedTheme == MarkdownPreviewTheme.CUSTOM) {
                            preferences.readCustomCss()
                        } else {
                            null
                        },
                    )
                }
                val renderResult = withContext(Dispatchers.Default) {
                    synchronized(renderer) {
                        renderer.renderWithOutline(
                            markdown = input.source,
                            stylesheetName = stylesheetName,
                            customCss = input.customCss,
                            frontMatterLabel = frontMatterLabel,
                        )
                    }
                }
                if (generation != loadGeneration) return@launch
                commitDocumentLoad(target, mode)
                pendingDocumentOutline = renderResult.outline
                pendingDocumentAnchor = fragment
                documentReady = false
                updateDocumentOutline(emptyList())
                supportActionBar?.title = target.request.displayName
                binding.previewWebView.isVisible = true
                webController.show(renderResult.html, target.relativePath)
            } catch (_: CancellationException) {
                // A newer load request superseded this one.
            } catch (error: Throwable) {
                if (generation == loadGeneration) {
                    if (mode == DocumentLoadMode.CURRENT) {
                        showLoadError(error)
                    } else {
                        binding.loadingIndicator.isVisible = false
                        Toast.makeText(
                            this@MarkdownPreviewActivity,
                            R.string.text_cannot_open_link,
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }
        }
    }

    private fun validateDocumentTarget(
        target: PreviewDocument,
        requireMarkdownExtension: Boolean,
    ) {
        val resolvedRelativePath = MarkdownPreviewPathPolicy.relativePath(
            rootPreviewRequest.parentUri,
            target.request.documentUri,
        )
        if (resolvedRelativePath != target.relativePath) {
            throw SecurityException("Preview document escaped its authorized parent URI")
        }
        if (requireMarkdownExtension) {
            val mimeType = runCatching {
                contentResolver.getType(target.request.documentUri)
            }.getOrNull()
            if (!MarkdownPreviewIntentPolicy.isMarkdown(mimeType, target.request.displayName)) {
                throw IOException("Relative link does not target a supported Markdown document")
            }
        }
    }

    private fun commitDocumentLoad(
        target: PreviewDocument,
        mode: DocumentLoadMode,
    ) {
        when (mode) {
            DocumentLoadMode.CURRENT -> Unit
            DocumentLoadMode.FORWARD -> {
                if (navigationHistory.size >= MAX_NAVIGATION_HISTORY_SIZE) {
                    navigationHistory.removeFirst()
                }
                navigationHistory.addLast(currentDocument)
                currentDocument = target
            }
            DocumentLoadMode.BACK -> {
                if (navigationHistory.peekLast() != target) {
                    throw IllegalStateException("Document history changed while loading")
                }
                navigationHistory.removeLast()
                currentDocument = target
            }
        }
    }

    private fun configureFindBar() {
        binding.findQuery.filters = binding.findQuery.filters +
            InputFilter.LengthFilter(MAX_FIND_QUERY_LENGTH)
        binding.findQuery.doAfterTextChanged { editable ->
            if (findBarVisible) scheduleFind(editable?.toString().orEmpty())
        }
        binding.findQuery.setOnEditorActionListener { _, actionId, event ->
            val isImeAction = actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_NEXT ||
                actionId == EditorInfo.IME_ACTION_GO
            val isEnterKey = event?.keyCode == KeyEvent.KEYCODE_ENTER &&
                event.action == KeyEvent.ACTION_DOWN
            if (isImeAction || isEnterKey) {
                submitOrAdvanceFind()
                true
            } else {
                false
            }
        }
        binding.findPrevious.setOnClickListener {
            webController.findNext(forward = false)
        }
        binding.findNext.setOnClickListener {
            webController.findNext(forward = true)
        }
        binding.findClose.setOnClickListener {
            closeFindBar()
        }
        resetFindResult()
    }

    private fun showFindBar() {
        if (!documentReady || findBarVisible) return
        findBarVisible = true
        binding.toolbar.isVisible = false
        binding.findBar.isVisible = true
        resetFindResult()
        binding.findQuery.requestFocus()
        binding.findQuery.post {
            WindowInsetsControllerCompat(window, binding.findQuery)
                .show(WindowInsetsCompat.Type.ime())
        }
    }

    private fun closeFindBar(): Boolean {
        if (!findBarVisible) return false
        findBarVisible = false
        findJob?.cancel()
        binding.findQuery.text?.clear()
        binding.findQuery.clearFocus()
        webController.clearFindMatches()
        resetFindResult()
        binding.findBar.isVisible = false
        binding.toolbar.isVisible = true
        WindowInsetsControllerCompat(window, binding.findQuery)
            .hide(WindowInsetsCompat.Type.ime())
        return true
    }

    private fun scheduleFind(query: String) {
        findJob?.cancel()
        if (query.isEmpty()) {
            webController.clearFindMatches()
            resetFindResult()
            return
        }

        showFindCounting()
        findJob = lifecycleScope.launch {
            delay(FIND_DEBOUNCE_MILLIS)
            if (findBarVisible && binding.findQuery.text?.toString() == query) {
                executeFind(query)
            }
        }
    }

    private fun submitOrAdvanceFind() {
        if (binding.findNext.isEnabled) {
            webController.findNext(forward = true)
            return
        }
        findJob?.cancel()
        executeFind(binding.findQuery.text?.toString().orEmpty())
    }

    private fun executeFind(query: String) {
        if (query.isEmpty()) {
            webController.clearFindMatches()
            resetFindResult()
            return
        }
        showFindCounting()
        if (!webController.findAll(query)) resetFindResult()
    }

    private fun updateFindResult(result: MarkdownPreviewFindResult) {
        if (!findBarVisible) return
        if (!result.isDoneCounting) {
            showFindCounting()
            return
        }
        binding.findResultCount.text = getString(
            R.string.text_find_match_count,
            result.activeMatchNumber,
            result.matchCount,
        )
        setFindNavigationEnabled(result.matchCount > 0)
    }

    private fun showFindCounting() {
        binding.findResultCount.setText(R.string.text_find_counting)
        setFindNavigationEnabled(false)
    }

    private fun resetFindResult() {
        binding.findResultCount.setText(R.string.text_find_match_count_initial)
        setFindNavigationEnabled(false)
    }

    private fun setFindNavigationEnabled(enabled: Boolean) {
        binding.findPrevious.isEnabled = enabled
        binding.findNext.isEnabled = enabled
        val alpha = if (enabled) 1f else DISABLED_CONTROL_ALPHA
        binding.findPrevious.alpha = alpha
        binding.findNext.alpha = alpha
    }

    private fun updateDocumentOutline(outline: List<MarkdownPreviewOutlineEntry>) {
        documentOutline = outline
        invalidateOptionsMenu()
    }

    private fun showDocumentOutline() {
        val outline = documentOutline
        if (outline.isEmpty()) return

        val indentPerLevel = (OUTLINE_INDENT_DP * resources.displayMetrics.density).roundToInt()
        val labels = outline.map { entry ->
            SpannableString(entry.title).apply {
                if (entry.level > 1) {
                    setSpan(
                        LeadingMarginSpan.Standard(indentPerLevel * (entry.level - 1)),
                        0,
                        length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                    )
                }
            }
        }.toTypedArray<CharSequence>()

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.text_document_outline)
            .setItems(labels) { _, which ->
                outline.getOrNull(which)?.let { entry ->
                    webController.scrollToAnchor(entry.anchorId)
                }
            }
            .setNegativeButton(R.string.dialog_button_cancel, null)
            .show()
    }

    private fun showLoadError(error: Throwable) {
        documentReady = false
        invalidateOptionsMenu()
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
        val currentTextZoomPercent = preferences.textZoomPercent
        val settingsBinding = DialogMarkdownPreviewSettingsBinding.inflate(layoutInflater).apply {
            startInFullscreenMode.isChecked = preferences.startInFullscreenMode
            fontSizeSlider.apply {
                valueFrom = MarkdownPreviewTextZoom.MIN_PERCENT.toFloat()
                valueTo = MarkdownPreviewTextZoom.MAX_PERCENT.toFloat()
                stepSize = MarkdownPreviewTextZoom.STEP_PERCENT.toFloat()
                value = currentTextZoomPercent.toFloat()
                setLabelFormatter { value ->
                    getString(R.string.text_font_size_percentage, value.roundToInt())
                }
                addOnChangeListener { _, value, _ ->
                    fontSizeValue.text = getString(
                        R.string.text_font_size_percentage,
                        value.roundToInt(),
                    )
                }
            }
            fontSizeValue.text = getString(
                R.string.text_font_size_percentage,
                currentTextZoomPercent,
            )
        }
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.text_settings)
            .setView(settingsBinding.root)
            .setNegativeButton(R.string.dialog_button_cancel, null)
            .setPositiveButton(R.string.dialog_button_confirm) { _, _ ->
                preferences.startInFullscreenMode = settingsBinding.startInFullscreenMode.isChecked
                val textZoomPercent = settingsBinding.fontSizeSlider.value.roundToInt()
                preferences.textZoomPercent = textZoomPercent
                webController.setTextZoom(textZoomPercent)
            }
            .show()
    }

    private fun setFullscreenMode(enabled: Boolean, invalidateMenu: Boolean = true) {
        if (enabled) closeFindBar()
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

    private fun printCurrentDocument() {
        if (!documentReady || printInProgress || !supportsPrinting()) return
        val printManager = getSystemService(PrintManager::class.java) ?: run {
            showPrintError()
            return
        }
        closeFindBar()
        val documentName = MarkdownPreviewPrintPolicy.documentName(currentDocument.request.displayName)
        printInProgress = true
        invalidateOptionsMenu()

        val printAdapter = try {
            webController.createPrintDocumentAdapter(documentName) {
                activePrintAdapter = null
                printInProgress = false
                if (!destroying && !isDestroyed) invalidateOptionsMenu()
            }
        } catch (_: RuntimeException) {
            printInProgress = false
            invalidateOptionsMenu()
            showPrintError()
            return
        }
        activePrintAdapter = printAdapter

        if (startPrintJob(printManager, documentName, printAdapter) == null) {
            printAdapter.abort()
            showPrintError()
        }
    }

    private fun startPrintJob(
        printManager: PrintManager,
        documentName: String,
        printAdapter: MarkdownPreviewPrintDocumentAdapter,
    ): PrintJob? = try {
        printManager.print(
            documentName,
            printAdapter,
            PrintAttributes.Builder().build(),
        )
    } catch (_: RuntimeException) {
        null
    }

    private fun supportsPrinting(): Boolean =
        packageManager.hasSystemFeature(PackageManager.FEATURE_PRINTING)

    private fun showPrintError() {
        Toast.makeText(this, R.string.text_cannot_print_document, Toast.LENGTH_SHORT).show()
    }

    internal fun openDocumentLink(link: MarkdownPreviewDocumentLink) {
        if (link.relativePath == currentDocument.relativePath) {
            link.fragment?.let(webController::scrollToAnchor)
            return
        }
        val target = previewDocumentForRelativePath(link.relativePath) ?: run {
            Toast.makeText(this, R.string.text_cannot_open_link, Toast.LENGTH_SHORT).show()
            return
        }
        loadDocument(
            target = target,
            mode = DocumentLoadMode.FORWARD,
            fragment = link.fragment,
        )
    }

    private fun navigateBack(): Boolean {
        val target = navigationHistory.peekLast() ?: return false
        loadDocument(
            target = target,
            mode = DocumentLoadMode.BACK,
            fragment = null,
        )
        return true
    }

    private fun restoreNavigationState(
        savedInstanceState: Bundle?,
        initialDocument: PreviewDocument,
    ): PreviewDocument {
        val currentPath = savedInstanceState?.getString(STATE_CURRENT_DOCUMENT_PATH)
            ?: return initialDocument
        val restoredCurrent = if (currentPath == initialDocument.relativePath) {
            initialDocument
        } else {
            previewDocumentForRelativePath(currentPath)
        } ?: return initialDocument

        savedInstanceState.getStringArrayList(STATE_NAVIGATION_HISTORY_PATHS)
            .orEmpty()
            .takeLast(MAX_NAVIGATION_HISTORY_SIZE)
            .mapNotNull { relativePath ->
                if (relativePath == initialDocument.relativePath) {
                    initialDocument
                } else {
                    previewDocumentForRelativePath(relativePath)
                }
            }
            .forEach(navigationHistory::addLast)
        return restoredCurrent
    }

    private fun previewDocumentForRelativePath(relativePath: String): PreviewDocument? {
        val documentUri = MarkdownPreviewPathPolicy.resolveDescendant(
            rootPreviewRequest.parentUri,
            relativePath,
        ) ?: return null
        val displayName = MarkdownPreviewIntentPolicy.sanitizeDisplayName(relativePath.substringAfterLast('/'))
            ?: return null
        if (!MarkdownPreviewIntentPolicy.isMarkdown(null, displayName)) return null
        return PreviewDocument(
            request = MarkdownPreviewRequest(
                documentUri = documentUri,
                parentUri = rootPreviewRequest.parentUri,
                displayName = displayName,
            ),
            relativePath = relativePath,
        )
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

    private data class PreviewDocument(
        val request: MarkdownPreviewRequest,
        val relativePath: String,
    )

    private enum class DocumentLoadMode {
        CURRENT,
        FORWARD,
        BACK,
    }

    companion object {
        const val MAX_MARKDOWN_BYTES = 8 * 1024 * 1024

        private const val FIND_DEBOUNCE_MILLIS = 150L
        private const val DISABLED_CONTROL_ALPHA = 0.38f
        private const val OUTLINE_INDENT_DP = 16
        private const val MAX_NAVIGATION_HISTORY_SIZE = 50
        private const val STATE_FULLSCREEN_MODE = "fullscreen_mode"
        private const val STATE_CURRENT_DOCUMENT_PATH = "current_document_path"
        private const val STATE_NAVIGATION_HISTORY_PATHS = "navigation_history_paths"
    }
}
