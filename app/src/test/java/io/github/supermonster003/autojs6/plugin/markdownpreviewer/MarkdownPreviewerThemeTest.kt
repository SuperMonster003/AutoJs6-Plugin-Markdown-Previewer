package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import org.junit.Assert.assertSame
import org.junit.Test

class MarkdownPreviewerThemeTest {

    @Test
    fun invalidOrMissingThemeIdFallsBackToGithubAuto() {
        assertSame(MarkdownPreviewerTheme.GITHUB_AUTO, MarkdownPreviewerTheme.fromId(null))
        assertSame(MarkdownPreviewerTheme.GITHUB_AUTO, MarkdownPreviewerTheme.fromId(""))
        assertSame(MarkdownPreviewerTheme.GITHUB_AUTO, MarkdownPreviewerTheme.fromId("not-a-theme"))
        assertSame(MarkdownPreviewerTheme.GITHUB_AUTO, MarkdownPreviewerTheme.fromId("GITHUB_DARK"))
    }

    @Test
    fun knownThemeIdsRoundTrip() {
        MarkdownPreviewerTheme.entries.forEach { theme ->
            assertSame(theme, MarkdownPreviewerTheme.fromId(theme.id))
        }
    }
}
