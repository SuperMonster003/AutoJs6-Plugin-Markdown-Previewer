package io.github.supermonster003.autojs6.plugin.markdownpreview

import org.junit.Assert.assertSame
import org.junit.Test

class MarkdownPreviewThemeTest {

    @Test
    fun invalidOrMissingThemeIdFallsBackToGithubAuto() {
        assertSame(MarkdownPreviewTheme.GITHUB_AUTO, MarkdownPreviewTheme.fromId(null))
        assertSame(MarkdownPreviewTheme.GITHUB_AUTO, MarkdownPreviewTheme.fromId(""))
        assertSame(MarkdownPreviewTheme.GITHUB_AUTO, MarkdownPreviewTheme.fromId("not-a-theme"))
        assertSame(MarkdownPreviewTheme.GITHUB_AUTO, MarkdownPreviewTheme.fromId("GITHUB_DARK"))
    }

    @Test
    fun knownThemeIdsRoundTrip() {
        MarkdownPreviewTheme.entries.forEach { theme ->
            assertSame(theme, MarkdownPreviewTheme.fromId(theme.id))
        }
    }
}
