package io.github.supermonster003.autojs6.plugin.markdownpreview

import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.net.URI

class MarkdownPreviewDocumentLinkPolicyTest {

    @Test
    fun renderedRelativeMarkdownLinkSurvivesSanitizingAndResolvesToDocumentTarget() {
        val html = MarkdownPreviewRenderer().render(
            markdown = "[Open guide](guide/%E5%BC%80%E5%A7%8B%20%E9%98%85%E8%AF%BB.md#%E7%AC%AC%E4%B8%80%E6%AD%A5)",
            stylesheetName = "github-light.css",
            customCss = null,
        )
        val href = requireNotNull(Jsoup.parse(html).selectFirst("a")?.attr("href"))
        val resolvedUrl = URI(MarkdownPreviewWebOrigin.DOCUMENT_URL).resolve(href).toString()

        assertEquals(
            MarkdownPreviewDocumentLink(
                relativePath = "guide/开始 阅读.md",
                displayName = "开始 阅读.md",
                fragment = "第一步",
            ),
            MarkdownPreviewDocumentLinkPolicy.parseVirtualUrl(resolvedUrl),
        )
    }

    @Test
    fun markdownDocumentUrlRoundTripsUnicodeSpacesAndFragment() {
        val url = MarkdownPreviewDocumentLinkPolicy.buildVirtualUrl(
            relativePath = "指南/开始 阅读.qmd",
            fragment = "安装 步骤",
        )
        assertNotNull(url)

        assertEquals(
            MarkdownPreviewDocumentLink(
                relativePath = "指南/开始 阅读.qmd",
                displayName = "开始 阅读.qmd",
                fragment = "安装 步骤",
            ),
            MarkdownPreviewDocumentLinkPolicy.parseVirtualUrl(requireNotNull(url)),
        )
    }

    @Test
    fun allSupportedMarkdownExtensionsCanNavigate() {
        val extensions = listOf(
            "md", "markdown", "mdown", "mkd", "mkdn", "mdwn", "mdtext", "mdtxt", "rmd", "qmd",
        )

        extensions.forEach { extension ->
            val link = MarkdownPreviewDocumentLinkPolicy.parseVirtualUrl(
                "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}folder/guide.$extension",
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
                MarkdownPreviewDocumentLinkPolicy.parseVirtualUrl(
                    "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}$path",
                ),
            )
        }
    }

    @Test
    fun normalizedParentSegmentsWorkOnlyWhileTheyRemainInsideDocumentRoot() {
        val nestedDocumentUrl = requireNotNull(
            MarkdownPreviewDocumentLinkPolicy.buildVirtualUrl("guide/setup/current.md"),
        )
        val withinRoot = URI(nestedDocumentUrl).resolve("../previous.md#overview").toString()
        assertEquals(
            MarkdownPreviewDocumentLink(
                relativePath = "guide/previous.md",
                displayName = "previous.md",
                fragment = "overview",
            ),
            MarkdownPreviewDocumentLinkPolicy.parseVirtualUrl(withinRoot),
        )

        val rootDocumentUrl = requireNotNull(
            MarkdownPreviewDocumentLinkPolicy.buildVirtualUrl("README.md"),
        )
        val outsideRoot = URI(rootDocumentUrl).resolve("../outside.md").toString()
        assertNull(MarkdownPreviewDocumentLinkPolicy.parseVirtualUrl(outsideRoot))
    }

    @Test
    fun foreignOriginsQueriesIllegalCharactersAndUnsupportedFormatsAreRejected() {
        val unsafeUrls = listOf(
            "http://${MarkdownPreviewWebOrigin.DOMAIN}/document/guide.md",
            "https://user@${MarkdownPreviewWebOrigin.DOMAIN}/document/guide.md",
            "https://${MarkdownPreviewWebOrigin.DOMAIN}:443/document/guide.md",
            "https://${MarkdownPreviewWebOrigin.DOMAIN}./document/guide.md",
            "https://example.com/document/guide.md",
            "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}guide.md?download=true",
            "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}guide%3Aalternate.md",
            "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}guide%00.md",
            "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}guide.html",
            "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}guide.txt",
            "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}guide",
            "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}guide.md#%FF",
        )

        unsafeUrls.forEach { url ->
            assertNull(url, MarkdownPreviewDocumentLinkPolicy.parseVirtualUrl(url))
        }
    }

    @Test
    fun emptyMalformedAndOverlongPathsAreRejected() {
        val overlongSegment = "a".repeat(256)
        val tooManySegments = List(65) { "a" }.joinToString("/") + ".md"
        val urls = listOf(
            MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL,
            "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}folder//guide.md",
            "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}folder/",
            "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}guide%2.md",
            "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}$overlongSegment.md",
            "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}$tooManySegments",
            "${MarkdownPreviewWebOrigin.DOCUMENT_BASE_URL}${"a".repeat(2046)}.md",
        )

        urls.forEach { url ->
            assertNull(url, MarkdownPreviewDocumentLinkPolicy.parseVirtualUrl(url))
        }
    }
}
