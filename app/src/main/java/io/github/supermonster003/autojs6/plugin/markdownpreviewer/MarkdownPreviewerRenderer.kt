package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import org.commonmark.Extension
import org.commonmark.ext.autolink.AutolinkExtension
import org.commonmark.ext.footnotes.FootnotesExtension
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.AttributeProvider
import org.commonmark.renderer.html.AttributeProviderFactory
import org.commonmark.renderer.html.HtmlRenderer
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import java.net.InetAddress
import java.net.URI
import java.util.Locale

internal data class MarkdownPreviewerOutlineEntry(
    val level: Int,
    val title: String,
    val anchorId: String,
)

internal data class MarkdownPreviewerRenderResult(
    val html: String,
    val outline: List<MarkdownPreviewerOutlineEntry>,
)

class MarkdownPreviewerRenderer {

    private val extensions: List<Extension> = listOf(
        AutolinkExtension.create(),
        TablesExtension.create(),
        StrikethroughExtension.create(),
        FootnotesExtension.builder().inlineFootnotes(true).build(),
        HeadingAnchorExtension.Builder().build(),
    )

    private val parser = Parser.builder()
        .extensions(extensions)
        .build()

    private val renderer = HtmlRenderer.builder()
        .extensions(extensions)
        .escapeHtml(false)
        .percentEncodeUrls(true)
        .attributeProviderFactory(AttributeProviderFactory {
            AttributeProvider { _, tagName, attributes ->
                when (tagName) {
                    "a" -> attributes["href"]?.let { href ->
                        if (!MarkdownPreviewerUrlPolicy.isSafeLink(href)) {
                            attributes.remove("href")
                        }
                    }
                    "img" -> attributes["src"]?.let { src ->
                        if (!MarkdownPreviewerUrlPolicy.isSafeImageResource(src)) {
                            attributes.remove("src")
                        }
                    }
                }
            }
        })
        .build()

    fun render(
        markdown: String,
        stylesheetName: String,
        customCss: String?,
        frontMatterLabel: String = DEFAULT_FRONT_MATTER_LABEL,
    ): String = renderWithOutline(markdown, stylesheetName, customCss, frontMatterLabel).html

    internal fun renderWithOutline(
        markdown: String,
        stylesheetName: String,
        customCss: String?,
        frontMatterLabel: String = DEFAULT_FRONT_MATTER_LABEL,
    ): MarkdownPreviewerRenderResult {
        val frontMatter = MarkdownPreviewerFrontMatterParser.extract(markdown)
        val renderedBody = renderer.render(parser.parse(frontMatter?.markdownBody ?: markdown))
        val preparedBody = prepareMarkdownBody(
            renderedBody = renderedBody,
            frontMatterYaml = frontMatter?.yaml,
            frontMatterLabel = frontMatterLabel,
        )
        val customStyle = customCss
            ?.takeIf(String::isNotBlank)
            ?.let(MarkdownPreviewerSecurityPolicy::escapeInlineStyle)
            ?.let { """<style id="markdown-previewer-custom-style">$it</style>""" }
            .orEmpty()

        val html = """
            <!doctype html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=5">
                <meta name="color-scheme" content="light dark">
                <meta name="referrer" content="${MarkdownPreviewerSecurityPolicy.REFERRER_POLICY}">
                <meta http-equiv="Content-Security-Policy" content="${MarkdownPreviewerSecurityPolicy.CONTENT_SECURITY_POLICY}">
                <link rel="stylesheet" href="${MarkdownPreviewerWebOrigin.PREVIEWER_ASSET_BASE_URL}base.css">
                <link rel="stylesheet" href="${MarkdownPreviewerWebOrigin.PREVIEWER_ASSET_BASE_URL}$stylesheetName">
                $customStyle
            </head>
            <body>
                <article class="markdown-body">${preparedBody.html}</article>
            </body>
            </html>
        """.trimIndent()

        return MarkdownPreviewerRenderResult(
            html = html,
            outline = preparedBody.outline,
        )
    }

    private fun prepareMarkdownBody(
        renderedBody: String,
        frontMatterYaml: String?,
        frontMatterLabel: String,
    ): PreparedMarkdownBody {
        val fragment = Jsoup.parseBodyFragment(renderedBody)
        fragment.outputSettings().prettyPrint(false)
        val body = fragment.body()

        body.select(MARKDOWN_REMOVED_TAGS).remove()
        body.getAllElements().toList().asReversed().forEach { element ->
            if (element === body) return@forEach
            if (element.normalName() !in MARKDOWN_ALLOWED_TAGS) {
                element.unwrap()
            } else {
                sanitizeMarkdownElement(element)
            }
        }

        body.select("p").forEach { paragraph ->
            if (
                paragraph.children().isEmpty() &&
                MARKDOWN_HIDDEN_COMMENT_PATTERN.matches(paragraph.text())
            ) {
                paragraph.remove()
            }
        }

        frontMatterYaml?.let { yaml ->
            prependFrontMatter(body, yaml, frontMatterLabel)
        }
        enhanceTaskLists(body)
        wrapTables(body)
        highlightCodeBlocks(body)
        val outline = extractDocumentOutline(body)
        return PreparedMarkdownBody(
            html = body.html(),
            outline = outline,
        )
    }

    private fun prependFrontMatter(
        body: Element,
        yaml: String,
        label: String,
    ) {
        val safeLabel = label.normalizeOutlineWhitespace()
            .take(MAX_FRONT_MATTER_LABEL_LENGTH)
            .ifEmpty { DEFAULT_FRONT_MATTER_LABEL }
        val details = Element("details").addClass(FRONT_MATTER_CLASS)
        details.appendElement("summary").appendChild(TextNode(safeLabel))
        details.appendElement("pre")
            .appendElement("code")
            .addClass("language-yaml")
            .appendChild(TextNode(yaml))
        body.prependChild(details)
    }

    private fun extractDocumentOutline(body: Element): List<MarkdownPreviewerOutlineEntry> {
        val elementsById = mutableMapOf<String, Element>()
        val reservedIds = mutableSetOf<String>()
        body.select("[id]").forEach { element ->
            val id = element.id()
            if (id.isNotEmpty()) {
                elementsById.putIfAbsent(id, element)
                reservedIds += id
            }
        }

        val outline = mutableListOf<MarkdownPreviewerOutlineEntry>()
        var generatedAnchorIndex = 0
        for (heading in body.select(HEADING_SELECTOR)) {
            if (outline.size >= MAX_OUTLINE_ITEMS) break

            val existingId = heading.id().takeIf { id ->
                isUsableOutlineAnchor(id) && elementsById[id] === heading
            }
            val anchorId = existingId ?: run {
                var candidate: String
                do {
                    generatedAnchorIndex++
                    candidate = "$GENERATED_OUTLINE_ANCHOR_PREFIX$generatedAnchorIndex"
                } while (candidate in reservedIds)
                reservedIds += candidate
                heading.attr("id", candidate)
                candidate
            }

            outline += MarkdownPreviewerOutlineEntry(
                level = heading.normalName().removePrefix("h").toInt(),
                title = extractOutlineTitle(heading),
                anchorId = anchorId,
            )
        }
        return outline
    }

    private fun extractOutlineTitle(heading: Element): String {
        val visibleText = heading.text().normalizeOutlineWhitespace()
        val imageAlternativeText = heading.select("img[alt]")
            .joinToString(" ") { it.attr("alt") }
            .normalizeOutlineWhitespace()
        return visibleText
            .ifEmpty { imageAlternativeText }
            .ifEmpty { OUTLINE_UNTITLED_MARKER }
            .take(MAX_OUTLINE_TITLE_LENGTH)
    }

    private fun String.normalizeOutlineWhitespace(): String =
        replace(OUTLINE_WHITESPACE_PATTERN, " ").trim()

    private fun isUsableOutlineAnchor(id: String): Boolean =
        id.isNotBlank() &&
            id.length <= MAX_OUTLINE_ANCHOR_LENGTH &&
            id.none { it == '\u0000' || it.code < 0x20 || it.code == 0x7f }

    private fun sanitizeMarkdownElement(element: Element) {
        val tagName = element.normalName()
        val allowedAttributes = MARKDOWN_GLOBAL_ATTRIBUTES +
            MARKDOWN_TAG_ATTRIBUTES[tagName].orEmpty()

        element.attributes().asList()
            .map { it.key }
            .filter { it.lowercase(Locale.ROOT) !in allowedAttributes }
            .forEach(element::removeAttr)

        sanitizeMarkdownClasses(element, tagName)
        MARKDOWN_FOOTNOTE_BOOLEAN_ATTRIBUTES.forEach { attribute ->
            if (element.hasAttr(attribute)) {
                element.attr(attribute, "")
            }
        }
        if (
            element.hasAttr("data-footnote-backref-idx") &&
            !SAFE_FOOTNOTE_BACKREF_INDEX.matches(element.attr("data-footnote-backref-idx"))
        ) {
            element.removeAttr("data-footnote-backref-idx")
        }

        if (element.hasAttr("align") && element.attr("align").lowercase(Locale.ROOT) !in SAFE_ALIGNMENTS) {
            element.removeAttr("align")
        }
        listOf("width", "height", "colspan", "rowspan", "start", "value", "span").forEach { attribute ->
            if (element.hasAttr(attribute) && !SAFE_INTEGER_ATTRIBUTE.matches(element.attr(attribute))) {
                element.removeAttr(attribute)
            }
        }

        if (element.hasAttr("href") && !MarkdownPreviewerUrlPolicy.isSafeLink(element.attr("href"))) {
            element.removeAttr("href")
        }
        if (element.hasAttr("cite") && !MarkdownPreviewerUrlPolicy.isSafeLink(element.attr("cite"))) {
            element.removeAttr("cite")
        }
        if (tagName == "a" && element.hasAttr("href")) {
            element.attr("rel", "noopener noreferrer")
        }
        if (tagName == "img" && element.hasAttr("src")) {
            if (!MarkdownPreviewerUrlPolicy.isSafeImageResource(element.attr("src"))) {
                element.removeAttr("src")
            }
            configureRemoteImage(element)
        }
    }

    private fun sanitizeMarkdownClasses(element: Element, tagName: String) {
        if (!element.hasAttr("class") || tagName == "code") return

        val allowedClasses = MARKDOWN_SAFE_CLASS_NAMES[tagName].orEmpty()
        val safeClasses = element.classNames()
            .filter { className -> className in allowedClasses }
        if (safeClasses.isEmpty()) {
            element.removeAttr("class")
        } else {
            element.attr("class", safeClasses.joinToString(" "))
        }
    }

    private fun configureRemoteImage(element: Element) {
        if (
            element.normalName() == "img" &&
            element.hasAttr("src") &&
            MarkdownPreviewerUrlPolicy.isSafeRemoteHttpsResource(element.attr("src"))
        ) {
            element.attr("loading", "lazy")
            element.attr("decoding", "async")
            element.attr("referrerpolicy", MarkdownPreviewerSecurityPolicy.REFERRER_POLICY)
        }
    }

    private fun enhanceTaskLists(body: Element) {
        body.select("li").forEach { item ->
            val textNode = firstTextNode(item) ?: return@forEach
            val match = TASK_ITEM_PATTERN.find(textNode.wholeText) ?: return@forEach
            val checked = match.groupValues[1].equals("x", ignoreCase = true)
            textNode.text(textNode.wholeText.removeRange(match.range))
            item.addClass("task-list-item")
            item.parent()?.takeIf { it.normalName() == "ul" || it.normalName() == "ol" }
                ?.addClass("task-list")
            item.prependElement("input")
                .attr("type", "checkbox")
                .attr("disabled", "")
                .also { if (checked) it.attr("checked", "") }
        }
    }

    private fun wrapTables(body: Element) {
        body.select("table").forEach { table ->
            val wrapper = Element("div").addClass("markdown-table-wrapper")
            table.before(wrapper)
            wrapper.appendChild(table)
        }
    }

    private fun highlightCodeBlocks(body: Element) {
        var remainingCharacterBudget = MAX_HIGHLIGHTED_CODE_CHARACTERS_PER_DOCUMENT
        body.select("pre > code[class]").forEach { code ->
            if (remainingCharacterBudget <= 0) return@forEach
            val source = code.wholeText()
            if (source.isEmpty() || source.length > remainingCharacterBudget) return@forEach

            val tokens = code.classNames().asSequence()
                .filter { className -> className.startsWith(LANGUAGE_CLASS_PREFIX) }
                .map { className -> className.removePrefix(LANGUAGE_CLASS_PREFIX) }
                .mapNotNull { languageAlias ->
                    MarkdownPreviewerSyntaxHighlighter.tokenize(languageAlias, source)
                }
                .firstOrNull()
                ?: return@forEach
            if (tokens.isEmpty()) return@forEach

            remainingCharacterBudget -= source.length
            code.empty()
            code.addClass(SYNTAX_HIGHLIGHTED_CLASS)
            var cursor = 0
            tokens.forEach { token ->
                if (token.start > cursor) {
                    code.appendChild(TextNode(source.substring(cursor, token.start)))
                }
                val tokenElement = Element("span")
                    .addClass(SYNTAX_TOKEN_CLASS)
                    .addClass(token.kind.cssClassName)
                    .appendChild(TextNode(source.substring(token.start, token.endExclusive)))
                code.appendChild(tokenElement)
                cursor = token.endExclusive
            }
            if (cursor < source.length) {
                code.appendChild(TextNode(source.substring(cursor)))
            }
        }
    }

    private fun firstTextNode(node: Node): TextNode? {
        node.childNodes().forEach { child ->
            if (child is TextNode) return child
            firstTextNode(child)?.let { return it }
        }
        return null
    }

    private data class PreparedMarkdownBody(
        val html: String,
        val outline: List<MarkdownPreviewerOutlineEntry>,
    )

    companion object {
        private const val HEADING_SELECTOR = "h1, h2, h3, h4, h5, h6"
        private const val GENERATED_OUTLINE_ANCHOR_PREFIX = "markdown-previewer-outline-"
        private const val OUTLINE_UNTITLED_MARKER = "\u2026"
        private const val MAX_OUTLINE_ITEMS = 10_000
        private const val MAX_OUTLINE_TITLE_LENGTH = 512
        private const val MAX_OUTLINE_ANCHOR_LENGTH = 512
        private const val MAX_FRONT_MATTER_LABEL_LENGTH = 128
        private const val MAX_HIGHLIGHTED_CODE_CHARACTERS_PER_DOCUMENT = 512 * 1024
        private const val DEFAULT_FRONT_MATTER_LABEL = "YAML metadata"
        private const val FRONT_MATTER_CLASS = "markdown-front-matter"
        private const val LANGUAGE_CLASS_PREFIX = "language-"
        private const val SYNTAX_HIGHLIGHTED_CLASS = "syntax-highlighted"
        private const val SYNTAX_TOKEN_CLASS = "syntax-token"

        private val OUTLINE_WHITESPACE_PATTERN = Regex("""\s+""")
        private val TASK_ITEM_PATTERN = Regex("""^\s*\[([ xX])]\s+""")
        private val MARKDOWN_HIDDEN_COMMENT_PATTERN = Regex(
            """^\s*\[(?:comment|//)]\s*:\s*(?:<>|#)\s*(?:\(.*\))?\s*$""",
            RegexOption.IGNORE_CASE,
        )
        private val SAFE_INTEGER_ATTRIBUTE = Regex("""^[0-9]{1,5}$""")
        private val SAFE_FOOTNOTE_BACKREF_INDEX = Regex("""^[1-9][0-9]{0,5}(?:-[1-9][0-9]{0,5})?$""")
        private val SAFE_ALIGNMENTS = setOf("left", "right", "center", "justify")
        private val MARKDOWN_FOOTNOTE_BOOLEAN_ATTRIBUTES = setOf(
            "data-footnotes",
            "data-footnote-ref",
            "data-footnote-backref",
        )

        private const val MARKDOWN_REMOVED_TAGS =
            "script, style, link, meta, base, iframe, frame, frameset, object, embed, applet, " +
                "form, input, button, select, option, optgroup, textarea, template, noscript, " +
                "svg, math, canvas, audio, video, source, track, xmp, plaintext"

        private val MARKDOWN_ALLOWED_TAGS = setOf(
            "a", "abbr", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup",
            "dd", "del", "details", "dfn", "div", "dl", "dt", "em", "figcaption", "figure",
            "h1", "h2", "h3", "h4", "h5", "h6", "hr", "i", "img", "ins", "kbd", "li", "mark",
            "ol", "p", "pre", "q", "rp", "rt", "ruby", "s", "samp", "section", "small", "span", "strike",
            "strong", "sub", "summary", "sup", "table", "tbody", "td", "tfoot", "th", "thead",
            "time", "tr", "tt", "u", "ul", "var", "wbr",
        )

        private val MARKDOWN_GLOBAL_ATTRIBUTES = setOf(
            "id", "title", "dir", "lang", "role", "aria-label", "aria-labelledby",
            "aria-describedby", "aria-hidden",
        )

        private val MARKDOWN_TAG_ATTRIBUTES = mapOf(
            "a" to setOf(
                "href",
                "name",
                "title",
                "class",
                "data-footnote-ref",
                "data-footnote-backref",
                "data-footnote-backref-idx",
            ),
            "blockquote" to setOf("cite"),
            "code" to setOf("class"),
            "col" to setOf("span"),
            "del" to setOf("datetime"),
            "details" to setOf("open"),
            "div" to setOf("align"),
            "h1" to setOf("align"),
            "h2" to setOf("align"),
            "h3" to setOf("align"),
            "h4" to setOf("align"),
            "h5" to setOf("align"),
            "h6" to setOf("align"),
            "img" to setOf("src", "alt", "title", "width", "height", "align"),
            "ins" to setOf("datetime"),
            "li" to setOf("value"),
            "ol" to setOf("start", "type", "reversed"),
            "p" to setOf("align"),
            "q" to setOf("cite"),
            "section" to setOf("class", "data-footnotes"),
            "sup" to setOf("class"),
            "table" to setOf("align"),
            "td" to setOf("align", "colspan", "rowspan", "headers"),
            "th" to setOf("align", "colspan", "rowspan", "headers", "scope"),
            "time" to setOf("datetime"),
        )

        private val MARKDOWN_SAFE_CLASS_NAMES = mapOf(
            "a" to setOf("footnote-backref"),
            "section" to setOf("footnotes"),
            "sup" to setOf("footnote-ref"),
        )
    }
}

internal object MarkdownPreviewerSecurityPolicy {

    const val REFERRER_POLICY = "no-referrer"

    const val CONTENT_SECURITY_POLICY =
        "default-src 'none'; style-src 'self' 'unsafe-inline'; img-src 'self' data: https:; " +
            "font-src 'self' data:; media-src 'self' data:; script-src 'none'; " +
            "connect-src 'none'; frame-src 'none'; child-src 'none'; object-src 'none'; " +
            "worker-src 'none'; manifest-src 'none'; base-uri 'none'; form-action 'none'"

    fun escapeInlineStyle(css: String): String =
        css.replace(Regex("</style", RegexOption.IGNORE_CASE)) { "<\\/style" }
}

internal object MarkdownPreviewerUrlPolicy {

    private val schemePattern = Regex("""^([a-z][a-z0-9+.-]*):""", RegexOption.IGNORE_CASE)
    private val safeDataImagePattern = Regex(
        """^data:image/(?:png|jpeg|gif|webp|avif|bmp|svg\+xml)(?:;|,)""",
        RegexOption.IGNORE_CASE,
    )
    private val safeDataResourcePattern = Regex(
        """^data:(?:image/(?:png|jpeg|gif|webp|avif|bmp|svg\+xml)|audio/(?:mpeg|ogg|wav|mp4)|video/(?:mp4|webm|ogg))(?:;|,)""",
        RegexOption.IGNORE_CASE,
    )

    fun isSafeLink(value: String): Boolean {
        val normalized = normalize(value) ?: return false
        if (normalized.startsWith("//") || normalized.startsWith("\\\\")) return false
        val scheme = schemePattern.find(normalized)?.groupValues?.get(1)?.lowercase(Locale.ROOT)
        return scheme == null || scheme == "http" || scheme == "https"
    }

    fun isSafeResource(value: String): Boolean {
        val normalized = normalize(value) ?: return false
        if (normalized.isEmpty()) return false
        if (normalized.startsWith("//") || normalized.startsWith("\\\\")) return false
        val scheme = schemePattern.find(normalized)?.groupValues?.get(1)?.lowercase(Locale.ROOT)
        return when {
            scheme == null -> true
            scheme == "data" -> safeDataResourcePattern.containsMatchIn(normalized)
            else -> false
        }
    }

    fun isSafeImageResource(value: String): Boolean {
        val normalized = normalize(value) ?: return false
        if (normalized.isEmpty()) return false
        if (normalized.startsWith("//") || normalized.startsWith("\\\\")) return false
        val scheme = schemePattern.find(normalized)?.groupValues?.get(1)?.lowercase(Locale.ROOT)
        return when {
            scheme == null -> true
            scheme == "data" -> safeDataImagePattern.containsMatchIn(normalized)
            scheme == "https" -> isSafeRemoteHttpsResource(normalized)
            else -> false
        }
    }

    fun isSafeRemoteHttpsResource(value: String): Boolean =
        safeRemoteHttpsHost(value) != null

    fun safeRemoteHttpsHost(value: String): String? {
        val normalized = normalize(value) ?: return null
        val uri = remoteHttpsOrigin(normalized) ?: return null
        if (uri.rawUserInfo != null) return null
        val host = uri.host?.trimEnd('.')?.lowercase(Locale.ROOT) ?: return null
        if (host.isEmpty() || host == "localhost" || host.endsWith(".localhost")) return null
        return host.takeIf(::isSafeRemoteHost)
    }

    private fun remoteHttpsOrigin(value: String): URI? {
        val schemeSeparator = value.indexOf("://")
        if (schemeSeparator < 0 || !value.substring(0, schemeSeparator).equals("https", ignoreCase = true)) {
            return null
        }
        val authorityStart = schemeSeparator + 3
        val authorityEnd = value.indexOfAny(
            chars = charArrayOf('/', '?', '#'),
            startIndex = authorityStart,
        ).takeIf { it >= 0 } ?: value.length
        val authority = value.substring(authorityStart, authorityEnd)
        if (authority.isEmpty() || authority.any { it.isWhitespace() || it == '\\' }) return null
        return runCatching { URI("https://$authority") }.getOrNull()
    }

    private fun isSafeRemoteHost(host: String): Boolean {
        val unwrappedHost = host.removePrefix("[").removeSuffix("]")
        if (unwrappedHost.contains(':')) {
            val address = runCatching { InetAddress.getByName(unwrappedHost) }.getOrNull() ?: return false
            val bytes = address.address
            val first = bytes.firstOrNull()?.toInt()?.and(0xff) ?: return false
            if (
                address.isAnyLocalAddress ||
                address.isLoopbackAddress ||
                address.isLinkLocalAddress ||
                address.isSiteLocalAddress ||
                address.isMulticastAddress ||
                first and 0xfe == 0xfc
            ) {
                return false
            }
            return true
        }

        if (unwrappedHost.all(Char::isDigit) || unwrappedHost.startsWith("0x", ignoreCase = true)) {
            return false
        }
        if (unwrappedHost.any { it != '.' && !it.isDigit() }) {
            return true
        }
        val parts = unwrappedHost.split('.')
        if (
            parts.size != 4 ||
            parts.any { it.isEmpty() || it.length > 1 && it.startsWith('0') }
        ) {
            return false
        }
        val octets = parts.map { it.toIntOrNull() ?: return false }
        if (octets.any { it !in 0..255 }) return false
        val first = octets[0]
        val second = octets[1]
        return when {
            first == 0 || first == 10 || first == 127 || first >= 224 -> false
            first == 100 && second in 64..127 -> false
            first == 169 && second == 254 -> false
            first == 172 && second in 16..31 -> false
            first == 192 && second == 168 -> false
            first == 198 && second in 18..19 -> false
            else -> true
        }
    }

    private fun normalize(value: String): String? {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return ""
        if (trimmed.any { it == '\u0000' || it.code < 0x20 || it.code == 0x7f }) return null
        return trimmed
    }
}

internal object MarkdownPreviewerWebOrigin {
    const val DOMAIN = "appassets.androidplatform.net"
    const val DOCUMENT_PATH_PREFIX = "/document/"
    const val DOCUMENT_BASE_URL = "https://$DOMAIN$DOCUMENT_PATH_PREFIX"
    const val DOCUMENT_FILE_NAME = "__previewer__.html"
    const val DOCUMENT_URL = "$DOCUMENT_BASE_URL$DOCUMENT_FILE_NAME"
    const val PREVIEWER_ASSET_PATH_PREFIX = "/previewer-assets/"
    const val PREVIEWER_ASSET_BASE_URL = "https://$DOMAIN$PREVIEWER_ASSET_PATH_PREFIX"

    fun isDomain(host: String?): Boolean =
        host?.trimEnd('.')?.equals(DOMAIN, ignoreCase = true) == true
}
