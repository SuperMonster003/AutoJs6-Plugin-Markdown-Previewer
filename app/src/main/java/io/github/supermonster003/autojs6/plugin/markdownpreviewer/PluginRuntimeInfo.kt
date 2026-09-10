package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import android.content.Context
import android.os.Build
import android.os.Bundle
import org.autojs.plugin.common.api.PluginCapabilityKeys
import org.autojs.plugin.common.api.PluginInfo
import org.autojs.plugin.explorer.api.ExplorerActionCapabilityKeys
import org.autojs.plugin.explorer.api.ExplorerActionCatalogKeys
import org.autojs.plugin.explorer.api.ExplorerActionPluginIds
import org.autojs.plugin.explorer.api.ExplorerActionProtocol
import org.autojs.plugin.explorer.api.ExplorerActionValues

internal object MarkdownPreviewerPlugin {
    const val ID = "markdown-previewer"
    const val VARIANT = "default"
    const val REQUIRED_HOST_VERSION = 5268L
    const val LABEL_RESOURCE_NAME = "action_markdown_previewer"
    const val LABEL_FALLBACK = "Markdown Previewer"
    const val ACTIVITY_CLASS_NAME =
        "io.github.supermonster003.autojs6.plugin.markdownpreviewer.MarkdownPreviewerActivity"

    val MIME_TYPES = arrayOf("text/markdown", "text/x-markdown")
    val EXTENSIONS = arrayOf(
        "md",
        "markdown",
        "mdown",
        "mkd",
        "mkdn",
        "mdwn",
        "mdtext",
        "mdtxt",
        "rmd",
        "qmd",
    )
}

internal fun Context.markdownPreviewerPluginInfo(): PluginInfo {
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    return PluginInfo().apply {
        name = getString(R.string.app_name)
        description = getString(R.string.plugin_description)
        instruction = resources.openRawResource(R.raw.plugin_instruction)
            .bufferedReader()
            .use { it.readText() }
        author = getString(R.string.plugin_author)
        collaborators = null
        versionName = packageInfo.versionName.orEmpty()
        versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }
        versionDate = getString(R.string.plugin_version_date)
        id = MarkdownPreviewerPlugin.ID
        engine = ExplorerActionPluginIds.ENGINE
        variant = MarkdownPreviewerPlugin.VARIANT
        supportedAbis = emptyArray()
        capabilities = Bundle().apply {
            putLong(PluginCapabilityKeys.REQUIRES_HOST_VERSION, MarkdownPreviewerPlugin.REQUIRED_HOST_VERSION)
            putInt(ExplorerActionCapabilityKeys.PROTOCOL_VERSION, ExplorerActionProtocol.VERSION)
        }
    }
}

internal fun markdownPreviewerActionCatalog(): Bundle {
    val action = Bundle().apply {
        putString(ExplorerActionCatalogKeys.ID, MarkdownPreviewerPlugin.ID)
        putString(ExplorerActionCatalogKeys.LABEL_RESOURCE_NAME, MarkdownPreviewerPlugin.LABEL_RESOURCE_NAME)
        putString(ExplorerActionCatalogKeys.LABEL_FALLBACK, MarkdownPreviewerPlugin.LABEL_FALLBACK)
        putString(ExplorerActionCatalogKeys.ACTIVITY_CLASS_NAME, MarkdownPreviewerPlugin.ACTIVITY_CLASS_NAME)
        putInt(ExplorerActionCatalogKeys.PRIORITY, 100)
        putInt(ExplorerActionCatalogKeys.TARGET_KIND, ExplorerActionValues.TARGET_FILE)
        putInt(ExplorerActionCatalogKeys.ACCESS_MODE, ExplorerActionValues.ACCESS_READ_ONLY)
        putInt(ExplorerActionCatalogKeys.PLACEMENT, ExplorerActionValues.PLACEMENT_OVERFLOW)
        putStringArrayList(
            ExplorerActionCatalogKeys.MIME_TYPES,
            ArrayList(MarkdownPreviewerPlugin.MIME_TYPES.asList()),
        )
        putStringArrayList(
            ExplorerActionCatalogKeys.EXTENSIONS,
            ArrayList(MarkdownPreviewerPlugin.EXTENSIONS.asList()),
        )
    }
    return Bundle().apply {
        putInt(ExplorerActionCatalogKeys.PROTOCOL_VERSION, ExplorerActionProtocol.VERSION)
        putParcelableArrayList(ExplorerActionCatalogKeys.ACTIONS, arrayListOf(action))
    }
}
