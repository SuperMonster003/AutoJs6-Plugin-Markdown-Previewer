package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.net.URI

class MarkdownPreviewerDocumentLinkPolicyTest {

    @Test
    fun renderedRelativeMarkdownLinkSurvivesSanitizingAndResolvesToDocumentTarget() {
        val html = MarkdownPreviewerRenderer().render(
            markdown = "[Open guide](guide/%E5%BC%80%E5%A7%8B%20%E9%98%85%E8%AF%BB.md#%E7%AC%AC%E4%B8%80%E6%AD%A5)",
            stylesheetName = "github-light.css",
            customCss = null,
        )
        val href = requireNotNull(Jsoup.parse(html).selectFirst("a")?.attr("href"))
        val resolvedUrl = URI(MarkdownPreviewerWebOrigin.DOCUMENT_URL).resolve(href).toString()

        assertEquals(
            MarkdownPreviewerDocumentLink(
                relativePath = "guide/开始 阅读.md",
                displayName = "开始 阅读.md",
                fragment = "第一步",
            ),
            MarkdownPreviewerDocumentLinkPolicy.parseVirtualUrl(resolvedUrl),
        )
    }

    @Test
    fun markdownDocumentUrlRoundTripsUnicodeSpacesAndFragment() {
        val url = MarkdownPreviewerDocumentLinkPolicy.buildVirtualUrl(
            relativePath = "指南/开始 阅读.qmd",
            fragment = "安装 步骤",
        )
        assertNotNull(url)

        assertEquals(
            MarkdownPreviewerDocumentLink(
                relativePath = "指南/开始 阅读.qmd",
                displayName = "开始 阅读.qmd",
                fragment = "安装 步骤",
            ),
            MarkdownPreviewerDocumentLinkPolicy.parseVirtualUrl(requireNotNull(url)),
        )
    }

    @Test
    fun allSupportedMarkdownExtensionsCanNavigate() {
        val extensions = listOf(
            "md", "markdown", "mdown", "mkd", "mkdn", "mdwn", "mdtext", "mdtxt", "rmd", "qmd",
        )

        extensions.forEach { extension ->
            val link = MarkdownPreviewerDocumentLinkPolicy.parseVirtualUrl(
                "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}folder/guide.$extension",
            )
            assertNotNull(extension, link)
        }
    }

    @Test
    fun traversalEncodedSeparatorsAndDoubleEncodingAreRejected() {
        val unsafePaths = listOf(
            "../outside.md",
            "folder/../outside.md",
            "%2e%2e/outside.md",
            "folder/%2E%2E/outside.md",
            "%252e%252e/outside.md",
            "folder%2Foutside.md",
            "folder%5Coutside.md",
            "folder\\outside.md",
            "/outside.md",
        )

        unsafePaths.forEach { path ->
            assertNull(
                path,
                MarkdownPreviewerDocumentLinkPolicy.parseVirtualUrl(
                    "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}$path",
                ),
            )
        }
    }

    @Test
    fun normalizedParentSegmentsWorkOnlyWhileTheyRemainInsideDocumentRoot() {
        val nestedDocumentUrl = requireNotNull(
            MarkdownPreviewerDocumentLinkPolicy.buildVirtualUrl("guide/setup/current.md"),
        )
        val withinRoot = URI(nestedDocumentUrl).resolve("../previous.md#overview").toString()
        assertEquals(
            MarkdownPreviewerDocumentLink(
                relativePath = "guide/previous.md",
                displayName = "previous.md",
                fragment = "overview",
            ),
            MarkdownPreviewerDocumentLinkPolicy.parseVirtualUrl(withinRoot),
        )

        val rootDocumentUrl = requireNotNull(
            MarkdownPreviewerDocumentLinkPolicy.buildVirtualUrl("README.md"),
        )
        val outsideRoot = URI(rootDocumentUrl).resolve("../outside.md").toString()
        assertNull(MarkdownPreviewerDocumentLinkPolicy.parseVirtualUrl(outsideRoot))
    }

    @Test
    fun foreignOriginsQueriesIllegalCharactersAndUnsupportedFormatsAreRejected() {
        val unsafeUrls = listOf(
            "http://${MarkdownPreviewerWebOrigin.DOMAIN}/document/guide.md",
            "https://user@${MarkdownPreviewerWebOrigin.DOMAIN}/document/guide.md",
            "https://${MarkdownPreviewerWebOrigin.DOMAIN}:443/document/guide.md",
            "https://${MarkdownPreviewerWebOrigin.DOMAIN}./document/guide.md",
            "https://example.com/document/guide.md",
            "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}guide.md?download=true",
            "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}guide%3Aalternate.md",
            "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}guide%00.md",
            "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}guide.html",
            "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}guide.txt",
            "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}guide",
            "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}guide.md#%FF",
        )

        unsafeUrls.forEach { url ->
            assertNull(url, MarkdownPreviewerDocumentLinkPolicy.parseVirtualUrl(url))
        }
    }

    @Test
    fun emptyMalformedAndOverlongPathsAreRejected() {
        val overlongSegment = "a".repeat(256)
        val tooManySegments = List(65) { "a" }.joinToString("/") + ".md"
        val urls = listOf(
            MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL,
            "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}folder//guide.md",
            "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}folder/",
            "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}guide%2.md",
            "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}$overlongSegment.md",
            "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}$tooManySegments",
            "${MarkdownPreviewerWebOrigin.DOCUMENT_BASE_URL}${"a".repeat(2046)}.md",
        )

        urls.forEach { url ->
            assertNull(url, MarkdownPreviewerDocumentLinkPolicy.parseVirtualUrl(url))
        }
    }
}
