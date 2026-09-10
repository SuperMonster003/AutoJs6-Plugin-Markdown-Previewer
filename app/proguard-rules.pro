-keep class io.github.supermonster003.autojs6.plugin.markdownpreviewer.** { *; }
-keep class org.autojs.plugin.common.api.PluginInfo { *; }
-keep class org.autojs.plugin.explorer.api.** { *; }

# PluginInfo is already parcelized in the bundled API AAR. Its source annotation
# is not required by this application at runtime.
-dontwarn kotlinx.parcelize.Parcelize
