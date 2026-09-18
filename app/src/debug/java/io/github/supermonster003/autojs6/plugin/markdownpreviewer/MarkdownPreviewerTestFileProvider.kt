package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.core.content.FileProvider

class MarkdownPreviewerTestFileProvider : FileProvider() {
    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor {
        beforeOpenFile?.invoke(uri)
        return requireNotNull(super.openFile(uri, mode))
    }

    companion object {
        @Volatile
        var beforeOpenFile: ((Uri) -> Unit)? = null
    }
}
