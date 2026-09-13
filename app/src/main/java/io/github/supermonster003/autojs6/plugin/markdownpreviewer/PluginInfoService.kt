package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.autojs.plugin.common.api.IPluginInfoProvider

class PluginInfoService : Service() {
    private val binder = object : IPluginInfoProvider.Stub() {
        override fun getInfo() = markdownPreviewerPluginInfo().apply { supportedAbis = emptyArray() }
    }

    override fun onBind(intent: Intent?): IBinder = binder
}
