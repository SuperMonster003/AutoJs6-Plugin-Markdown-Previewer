package io.github.supermonster003.autojs6.plugin.markdownpreviewer

internal data class MarkdownPreviewerFrontMatter(
    val yaml: String,
    val markdownBody: String,
)

/**
 * Extracts the bounded YAML metadata header used by Markdown, R Markdown, and Quarto documents.
 * This deliberately recognizes the envelope without deserializing untrusted YAML.
 */
internal object MarkdownPreviewerFrontMatterParser {

    const val MAX_FRONT_MATTER_CHARACTERS = 256 * 1024
    const val MAX_FRONT_MATTER_LINES = 4_096

    fun extract(markdown: String): MarkdownPreviewerFrontMatter? {
        if (markdown.isEmpty()) return null

        val documentStart = if (markdown.first() == BYTE_ORDER_MARK) 1 else 0
        val openingLineEnd = findLineEnd(markdown, documentStart)
        if (
            openingLineEnd == markdown.length ||
            !isDelimiterLine(markdown, documentStart, openingLineEnd, YAML_START_DELIMITER)
        ) {
            return null
        }

        val contentStart = nextLineStart(markdown, openingLineEnd)
        var lineStart = contentStart
        var contentLineCount = 0
        while (lineStart < markdown.length) {
            if (lineStart - contentStart > MAX_FRONT_MATTER_CHARACTERS) return null

            val lineEnd = findLineEnd(markdown, lineStart)
            val isClosingDelimiter = YAML_END_DELIMITERS.any { delimiter ->
                isDelimiterLine(markdown, lineStart, lineEnd, delimiter)
            }
            if (isClosingDelimiter) {
                val yaml = markdown.substring(contentStart, lineStart).trimEnd('\r', '\n')
                if (
                    yaml.length > MAX_FRONT_MATTER_CHARACTERS ||
                    !isPlausibleYamlMetadata(yaml)
                ) {
                    return null
                }
                val bodyStart = if (lineEnd < markdown.length) {
                    nextLineStart(markdown, lineEnd)
                } else {
                    markdown.length
                }
                return MarkdownPreviewerFrontMatter(
                    yaml = yaml,
                    markdownBody = markdown.substring(bodyStart),
                )
            }

            contentLineCount++
            if (contentLineCount > MAX_FRONT_MATTER_LINES || lineEnd == markdown.length) return null
            lineStart = nextLineStart(markdown, lineEnd)
        }
        return null
    }

    private fun isPlausibleYamlMetadata(yaml: String): Boolean {
        if (yaml.isBlank() || yaml.any(::isDisallowedControlCharacter)) return false
        return yaml.lineSequence().any(::isTopLevelMappingEntry)
    }

    private fun isTopLevelMappingEntry(rawLine: String): Boolean {
        val line = rawLine.trimEnd('\r')
        if (
            line.isEmpty() ||
            line.first().isWhitespace() ||
            line.first() in YAML_NON_MAPPING_PREFIXES
        ) {
            return false
        }
        val trimmedLine = line.trimEnd()
        val isFlowMapping = trimmedLine.startsWith('{') && trimmedLine.endsWith('}')

        var insideSingleQuote = false
        var insideDoubleQuote = false
        var escaped = false
        line.forEachIndexed { index, character ->
            when {
                escaped -> escaped = false
                insideDoubleQuote && character == '\\' -> escaped = true
                !insideDoubleQuote && character == '\'' -> insideSingleQuote = !insideSingleQuote
                !insideSingleQuote && character == '"' -> insideDoubleQuote = !insideDoubleQuote
                !insideSingleQuote && !insideDoubleQuote && character == ':' -> {
                    val followedBySeparator =
                        index == line.lastIndex || line[index + 1].isWhitespace() || isFlowMapping
                    if (followedBySeparator && line.substring(0, index).trim().isNotEmpty()) return true
                }
            }
        }
        return false
    }

    private fun isDelimiterLine(
        source: String,
        start: Int,
        end: Int,
        delimiter: String,
    ): Boolean {
        var trimmedEnd = end
        while (trimmedEnd > start && source[trimmedEnd - 1] in TRAILING_DELIMITER_WHITESPACE) {
            trimmedEnd--
        }
        return trimmedEnd - start == delimiter.length &&
            source.regionMatches(start, delimiter, 0, delimiter.length)
    }

    private fun findLineEnd(source: String, start: Int): Int =
        source.indexOfAny(LINE_ENDINGS, startIndex = start).takeIf { it >= 0 } ?: source.length

    private fun nextLineStart(source: String, lineEnd: Int): Int = when {
        lineEnd >= source.length -> source.length
        source[lineEnd] == '\r' && source.getOrNull(lineEnd + 1) == '\n' -> lineEnd + 2
        else -> lineEnd + 1
    }

    private fun isDisallowedControlCharacter(character: Char): Boolean =
        character == '\u0000' ||
            character.code == 0x7f ||
            character.code < 0x20 && character !in ALLOWED_YAML_CONTROLS

    private const val BYTE_ORDER_MARK = '\uFEFF'
    private const val YAML_START_DELIMITER = "---"
    private val YAML_END_DELIMITERS = listOf("---", "...")
    private val YAML_NON_MAPPING_PREFIXES =
        setOf('#', '-', '?', ':', '%', '!', '&', '*', '|', '>', '@', '`')
    private val TRAILING_DELIMITER_WHITESPACE = setOf(' ', '\t')
    private val LINE_ENDINGS = charArrayOf('\r', '\n')
    private val ALLOWED_YAML_CONTROLS = setOf('\t', '\r', '\n')
}
