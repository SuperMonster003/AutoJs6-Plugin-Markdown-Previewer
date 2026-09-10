package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkdownPreviewerRendererTest {

    private val renderer = MarkdownPreviewerRenderer()

    @Test
    fun yamlFrontMatterBecomesCollapsedSanitizedAndHighlightedMetadata() {
        val yaml = """
            title: "Safe </code><script id='front-matter-payload'>alert(1)</script>"
            author:
              - Ada Lovelace
            format: html
            url: https://example.com/
        """.trimIndent()
        val result = renderer.renderWithOutline(
            markdown = "---\n$yaml\n---\n# Rendered body\n\nVisible content.",
            stylesheetName = "github-light.css",
            customCss = null,
            frontMatterLabel = "YAML <metadata>",
        )
        val document = Jsoup.parse(result.html)
        val details = requireNotNull(document.selectFirst("article > details.markdown-front-matter"))
        val code = requireNotNull(details.selectFirst("pre > code.language-yaml.syntax-highlighted"))

        assertFalse(details.hasAttr("open"))
        assertEquals("YAML <metadata>", details.selectFirst("summary")?.text())
        assertEquals(yaml, code.wholeText())
        assertTrue(code.select("span.syntax-property").any { it.text() == "title" })
        assertTrue(code.select("span.syntax-string").isNotEmpty())
        assertTrue(document.select("script, summary img, details.markdown-front-matter a").isEmpty())
        assertTrue(document.select("article > hr").isEmpty())
        assertEquals("Rendered body", document.selectFirst("h1")?.text())
        assertEquals(listOf("Rendered body"), result.outline.map { it.title })
    }

    @Test
    fun horizontalRuleDocumentWithoutYamlMappingKeepsNormalMarkdownMeaning() {
        val document = Jsoup.parse(
            renderer.render(
                markdown = "---\nOrdinary paragraph\n---\n\n# Body",
                stylesheetName = "github-light.css",
                customCss = null,
            ),
        )

        assertTrue(document.select("details.markdown-front-matter").isEmpty())
        assertNotNull(document.selectFirst("hr"))
        assertTrue(document.body().text().contains("Ordinary paragraph"))
    }

    @Test
    fun documentOutlineCollectsHeadingHierarchyAndTargetsSanitizedAnchors() {
        val result = renderer.renderWithOutline(
            markdown = """
                # Top *heading*

                ### Deep `code`

                <h2 id="kept-anchor">Raw <span>heading</span></h2>

                <div id="duplicate-anchor"></div>
                <h4 id="duplicate-anchor">Duplicate anchor heading</h4>

                <h5><img alt="Image heading" src="images/heading.png"></h5>
            """.trimIndent(),
            stylesheetName = "github-light.css",
            customCss = null,
        )
        val document = Jsoup.parse(result.html)

        assertEquals(listOf(1, 3, 2, 4, 5), result.outline.map { it.level })
        assertEquals(
            listOf(
                "Top heading",
                "Deep code",
                "Raw heading",
                "Duplicate anchor heading",
                "Image heading",
            ),
            result.outline.map { it.title },
        )
        assertEquals("kept-anchor", result.outline[2].anchorId)
        assertFalse(result.outline[3].anchorId == "duplicate-anchor")
        result.outline.forEach { entry ->
            val target = requireNotNull(document.getElementById(entry.anchorId))
            assertEquals("h${entry.level}", target.normalName())
        }
    }

    @Test
    fun documentOutlineIsEmptyWhenRenderedDocumentHasNoHeadings() {
        val result = renderer.renderWithOutline(
            markdown = "A paragraph with **no headings**.",
            stylesheetName = "github-light.css",
            customCss = null,
        )

        assertTrue(result.outline.isEmpty())
        assertTrue(Jsoup.parse(result.html).select("h1, h2, h3, h4, h5, h6").isEmpty())
    }

    @Test
    fun markdownIsRenderedWithExtensionsAndUnsafeInputIsNeutralized() {
        val markdown = """
            # Previewer Heading

            <script id="raw-markdown">alert(1)</script>

            [unsafe](javascript:alert(1))
            [safe](https://example.com/docs)

            | Name | Value |
            | --- | --- |
            | One | Two |

            ~~removed~~

            - [x] completed
            - [ ] pending
        """.trimIndent()

        val output = renderer.render(
            markdown = markdown,
            stylesheetName = "github-light.css",
            customCss = null,
        )
        val document = Jsoup.parse(output)
        val unsafeLink = requireNotNull(document.select("a").firstOrNull { it.text() == "unsafe" })
        val safeLink = requireNotNull(document.select("a").firstOrNull { it.text() == "safe" })

        assertNull(document.selectFirst("script#raw-markdown"))
        assertFalse(output.contains("alert(1)"))
        assertFalse(unsafeLink.hasAttr("href"))
        assertEquals("https://example.com/docs", safeLink.attr("href"))
        assertNotNull(document.selectFirst("h1#previewer-heading"))
        assertNotNull(document.selectFirst("table"))
        assertTrue(document.selectFirst("table")?.parent()?.hasClass("markdown-table-wrapper") == true)
        assertEquals("removed", document.selectFirst("del")?.text())
        assertEquals(2, document.select("li.task-list-item").size)
        assertEquals(2, document.select("ul.task-list input[type=checkbox][disabled]").size)
        assertEquals(1, document.select("input[type=checkbox][checked]").size)

        val csp = document.selectFirst("meta[http-equiv=Content-Security-Policy]")?.attr("content").orEmpty()
        assertTrue(csp.contains("script-src 'none'"))
        assertTrue(csp.contains("connect-src 'none'"))
        assertTrue(csp.contains("object-src 'none'"))
        assertTrue(csp.contains("base-uri 'none'"))
        assertTrue(csp.contains("form-action 'none'"))
        assertTrue(csp.contains("img-src 'self' data: https:"))
        assertEquals(
            MarkdownPreviewerSecurityPolicy.REFERRER_POLICY,
            document.selectFirst("meta[name=referrer]")?.attr("content"),
        )
    }

    @Test
    fun namedRepeatedAndInlineFootnotesRenderWithBidirectionalAnchors() {
        val document = Jsoup.parse(
            renderer.render(
                markdown = """
                    Text with a named footnote[^guide], the same footnote again[^guide],
                    and an inline note^[Inline *emphasis*].

                    [^guide]: Named **definition** with a [safe link](https://example.com/footnote).
                """.trimIndent(),
                stylesheetName = "github-light.css",
                customCss = null,
            ),
        )

        val footnotes = requireNotNull(document.selectFirst("article > section.footnotes[data-footnotes]"))
        val definitions = footnotes.select("ol > li")
        val references = document.select("sup.footnote-ref > a[data-footnote-ref]")
        val backReferences = footnotes.select("a.footnote-backref[data-footnote-backref]")

        assertEquals(2, definitions.size)
        assertEquals(3, references.size)
        assertEquals(3, backReferences.size)
        assertTrue(definitions.any { it.text().contains("Named definition") })
        assertTrue(definitions.any { it.select("em").text() == "emphasis" })
        assertEquals(
            "https://example.com/footnote",
            footnotes.selectFirst("a[href^=https://example.com/footnote]")?.attr("href"),
        )

        references.forEach { reference ->
            val href = reference.attr("href")
            assertTrue(href.startsWith("#"))
            assertNotNull(document.getElementById(href.removePrefix("#")))
            assertEquals("noopener noreferrer", reference.attr("rel"))
            assertEquals("", reference.attr("data-footnote-ref"))
        }
        backReferences.forEach { backReference ->
            val href = backReference.attr("href")
            assertTrue(href.startsWith("#"))
            assertNotNull(document.getElementById(href.removePrefix("#")))
            assertEquals(setOf("footnote-backref"), backReference.classNames())
            assertTrue(backReference.attr("data-footnote-backref-idx").matches(Regex("""\d+(?:-\d+)?""")))
        }
    }

    @Test
    fun footnoteMarkupUsesAClosedAttributeAndClassAllowlist() {
        val document = Jsoup.parse(
            renderer.render(
                markdown = """
                    Unsafe definition[^unsafe].

                    [^unsafe]: <script id="footnote-script">footnote payload</script>
                        [unsafe link](javascript:alert(1))

                    <sup id="raw-footnote-ref" class="footnote-ref forged" onclick="alert(2)">raw ref</sup>
                    <section id="raw-footnotes" class="footnotes forged" data-footnotes="forged" onclick="alert(3)">
                      <a id="raw-footnote-backref" class="footnote-backref forged"
                         data-footnote-backref="forged" data-footnote-backref-idx="1-2-3"
                         href="javascript:alert(4)">raw backref</a>
                    </section>
                    <div id="wrong-footnote-tag" class="footnotes" data-footnotes>wrong tag</div>
                """.trimIndent(),
                stylesheetName = "github-light.css",
                customCss = null,
            ),
        )

        assertNull(document.selectFirst("script#footnote-script"))
        assertFalse(document.body().text().contains("footnote payload"))
        val unsafeLink = requireNotNull(document.select("a").firstOrNull { it.text() == "unsafe link" })
        assertFalse(unsafeLink.hasAttr("href"))

        val rawReference = requireNotNull(document.selectFirst("sup#raw-footnote-ref"))
        assertEquals(setOf("footnote-ref"), rawReference.classNames())
        assertFalse(rawReference.hasAttr("onclick"))

        val rawSection = requireNotNull(document.selectFirst("section#raw-footnotes"))
        assertEquals(setOf("footnotes"), rawSection.classNames())
        assertEquals("", rawSection.attr("data-footnotes"))
        assertFalse(rawSection.hasAttr("onclick"))

        val rawBackReference = requireNotNull(document.selectFirst("a#raw-footnote-backref"))
        assertEquals(setOf("footnote-backref"), rawBackReference.classNames())
        assertEquals("", rawBackReference.attr("data-footnote-backref"))
        assertFalse(rawBackReference.hasAttr("data-footnote-backref-idx"))
        assertFalse(rawBackReference.hasAttr("href"))

        val wrongTag = requireNotNull(document.selectFirst("div#wrong-footnote-tag"))
        assertFalse(wrongTag.hasAttr("class"))
        assertFalse(wrongTag.hasAttr("data-footnotes"))
    }

    @Test
    fun markdownSupportsSanitizedGitHubHtmlWithoutShowingMarkup() {
        val markdown = """
            <!--suppress HtmlDeprecatedAttribute -->

            <div align="center">
              <p>
                <a href="https://github.com/example/project">
                  <img alt="Banner" src="https://raw.githubusercontent.com/example/project/main/banner.png" height="224"/>
                </a>
                <img id="badge-with-space" alt="Badge" src="https://img.shields.io/badge/auto.js->= 6.2.0-67a91b"/>
              </p>
              <p>Centered description</p>
            </div>

            <details open><summary>查看更多功能</summary><br>

            - 自动收取好友能量

            </details>

            [comment]: <> (Version history only shows last 3 versions)
        """.trimIndent()

        val output = renderer.render(
            markdown = markdown,
            stylesheetName = "github-light.css",
            customCss = null,
        )
        val document = Jsoup.parse(output)
        val image = requireNotNull(document.selectFirst("div[align=center] a > img"))

        assertEquals(
            "https://raw.githubusercontent.com/example/project/main/banner.png",
            image.attr("src"),
        )
        assertEquals("224", image.attr("height"))
        assertEquals("lazy", image.attr("loading"))
        assertEquals("async", image.attr("decoding"))
        assertEquals("no-referrer", image.attr("referrerpolicy"))
        assertEquals(
            "https://img.shields.io/badge/auto.js->= 6.2.0-67a91b",
            document.selectFirst("#badge-with-space")?.attr("src"),
        )
        assertNotNull(document.selectFirst("details[open] > summary"))
        assertEquals("自动收取好友能量", document.selectFirst("details li")?.text())
        assertFalse(document.body().text().contains("<!--suppress"))
        assertFalse(document.body().text().contains("<div"))
        assertFalse(document.body().text().contains("<img"))
        assertFalse(document.body().text().contains("Version history only shows"))
    }

    @Test
    fun markdownRawHtmlSanitizerKeepsOnlySafeElementsAttributesAndImageUrls() {
        val markdown = """
            <div id="safe" align="center" style="position:fixed" onclick="alert(1)">
              <script id="script">script payload</script>
              <style id="style">body { display: none; }</style>
              <iframe id="frame" src="https://example.com/"></iframe>
              <svg id="svg"><script>svg payload</script></svg>
              <form id="form"><input value="unsafe">form payload</form>
              <marquee><strong id="kept">Kept text</strong></marquee>
              <a id="link" href="https://example.com/docs" target="_blank" ping="https://tracker.example/">Docs</a>
              <img id="https-image" src="https://example.com/image.png" height="100" onerror="alert(1)" srcset="https://example.com/2x.png 2x" style="display:none">
              <img id="relative-image" src="images/picture.png">
              <img id="data-image" src="data:image/png;base64,iVBORw0KGgo=">
              <img id="http-image" src="http://example.com/image.png">
              <img id="protocol-relative-image" src="//example.com/image.png">
              <img id="script-image" src="javascript:alert(1)">
              <img id="text-data-image" src="data:text/html;base64,PGgxPkJhZDwvaDE+">
            </div>
        """.trimIndent()

        val document = Jsoup.parse(
            renderer.render(
                markdown = markdown,
                stylesheetName = "github-light.css",
                customCss = null,
            ),
        )

        assertTrue(document.select("script, style#style, iframe, svg, form, input").isEmpty())
        assertFalse(document.body().text().contains("script payload"))
        assertFalse(document.body().text().contains("svg payload"))
        assertFalse(document.body().text().contains("form payload"))
        assertNotNull(document.selectFirst("strong#kept"))
        assertNull(document.selectFirst("marquee"))

        val safe = requireNotNull(document.selectFirst("div#safe"))
        assertEquals("center", safe.attr("align"))
        assertFalse(safe.hasAttr("style"))
        assertFalse(safe.hasAttr("onclick"))

        val link = requireNotNull(document.selectFirst("a#link"))
        assertEquals("https://example.com/docs", link.attr("href"))
        assertEquals("noopener noreferrer", link.attr("rel"))
        assertFalse(link.hasAttr("target"))
        assertFalse(link.hasAttr("ping"))

        val httpsImage = requireNotNull(document.selectFirst("img#https-image"))
        assertEquals("https://example.com/image.png", httpsImage.attr("src"))
        assertEquals("100", httpsImage.attr("height"))
        assertFalse(httpsImage.hasAttr("onerror"))
        assertFalse(httpsImage.hasAttr("srcset"))
        assertFalse(httpsImage.hasAttr("style"))
        assertEquals("images/picture.png", document.selectFirst("#relative-image")?.attr("src"))
        assertTrue(document.selectFirst("#data-image")?.hasAttr("src") == true)
        listOf(
            "#http-image",
            "#protocol-relative-image",
            "#script-image",
            "#text-data-image",
        ).forEach { selector ->
            assertFalse(selector, document.selectFirst(selector)!!.hasAttr("src"))
        }
    }

    @Test
    fun markdownTablesScrollAndLegacyCommentsStayVisibleOnlyInsideCodeBlocks() {
        val markdown = """
            [comment]: <> (hidden paragraph)

            ```text
            [comment]: <> (visible code)
            ```

            | Left | Center | Right |
            | :--- | :----: | ----: |
            | One | Two | Three |

            | A | B |
            | --- | --- |
            | C | D |
        """.trimIndent()

        val document = Jsoup.parse(
            renderer.render(
                markdown = markdown,
                stylesheetName = "github-light.css",
                customCss = null,
            ),
        )

        assertTrue(
            document.select("p").none {
                it.text().contains("[comment]: <> (hidden paragraph)")
            },
        )
        assertEquals("[comment]: <> (visible code)", document.selectFirst("pre code")?.text())
        assertEquals(2, document.select(".markdown-table-wrapper").size)
        assertEquals(2, document.select(".markdown-table-wrapper > table").size)
        assertTrue(document.select(".markdown-table-wrapper .markdown-table-wrapper").isEmpty())
        assertEquals("left", document.selectFirst("th")?.attr("align"))
        assertEquals("center", document.select("th").getOrNull(1)?.attr("align"))
        assertEquals("right", document.select("th").getOrNull(2)?.attr("align"))
    }

    @Test
    fun fencedCodeIsHighlightedAfterSanitizationWithoutScripts() {
        val source = """
            // tokenized on the Kotlin side
            val answer: Int = 42
            val payload = "</span><script id='code-payload'>alert(1)</script>"
        """.trimIndent()
        val markdown = buildString {
            appendLine("```kotlin")
            appendLine(source)
            appendLine("```")
            appendLine()
            appendLine(
                "<span id=\"raw-token\" class=\"syntax-keyword\" " +
                    "onclick=\"alert(2)\">raw token</span>",
            )
        }

        val output = renderer.render(
            markdown = markdown,
            stylesheetName = "github-dark.css",
            customCss = null,
        )
        val document = Jsoup.parse(output)
        val code = requireNotNull(document.selectFirst("pre > code.language-kotlin.syntax-highlighted"))
        val rawToken = requireNotNull(document.selectFirst("span#raw-token"))
        val tokenElements = code.select("span.syntax-token")
        val allowedTokenClasses = MarkdownPreviewerSyntaxTokenKind.entries.map { it.cssClassName }.toSet()

        assertEquals(source, code.wholeText().trimEnd('\r', '\n'))
        assertTrue(tokenElements.isNotEmpty())
        assertTrue(code.select(".syntax-comment").isNotEmpty())
        assertTrue(code.select(".syntax-keyword").any { it.text() == "val" })
        assertTrue(code.select(".syntax-type").any { it.text() == "Int" })
        assertTrue(code.select(".syntax-number").any { it.text() == "42" })
        assertTrue(code.select(".syntax-string").isNotEmpty())
        assertTrue(document.select("script").isEmpty())
        assertFalse(document.body().html().contains("<script id=\"code-payload\""))
        assertFalse(rawToken.hasAttr("class"))
        assertFalse(rawToken.hasAttr("onclick"))
        tokenElements.forEach { token ->
            assertEquals(2, token.classNames().size)
            assertTrue(token.hasClass("syntax-token"))
            assertTrue(token.classNames().any { it in allowedTokenClasses })
            assertTrue(token.attributes().asList().all { it.key == "class" })
        }

        val csp = document.selectFirst("meta[http-equiv=Content-Security-Policy]")
            ?.attr("content")
            .orEmpty()
        assertTrue(csp.contains("script-src 'none'"))
    }

    @Test
    fun unknownCodeLanguageRemainsPlainText() {
        val document = Jsoup.parse(
            renderer.render(
                markdown = """
                    ```unknown-language
                    let value = 42
                    ```
                """.trimIndent(),
                stylesheetName = "github-light.css",
                customCss = null,
            ),
        )
        val code = requireNotNull(document.selectFirst("pre > code.language-unknown-language"))

        assertFalse(code.hasClass("syntax-highlighted"))
        assertTrue(code.select("span.syntax-token").isEmpty())
        assertEquals("let value = 42", code.text())
    }

    @Test
    fun customCssCannotEscapeItsStyleElement() {
        val customCss =
            """body { color: red; }</StYlE><script id="escaped">alert(1)</script><style>"""

        val output = renderer.render(
            markdown = "# Safe",
            stylesheetName = "github-dark.css",
            customCss = customCss,
        )
        val document = Jsoup.parse(output)

        assertTrue(output.contains("<\\/style>", ignoreCase = true))
        assertFalse(output.contains("</style><script id=\"escaped\"", ignoreCase = true))
        assertNotNull(document.selectFirst("style#markdown-previewer-custom-style"))
        assertNull(document.selectFirst("script#escaped"))
    }

}
