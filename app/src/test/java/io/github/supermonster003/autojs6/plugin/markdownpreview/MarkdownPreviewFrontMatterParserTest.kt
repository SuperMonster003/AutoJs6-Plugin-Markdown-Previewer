package io.github.supermonster003.autojs6.plugin.markdownpreview

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MarkdownPreviewFrontMatterParserTest {

    @Test
    fun extractsBomAndCrLfDelimitedMetadataWithoutChangingTheBody() {
        val source =
            "\uFEFF---\r\ntitle: Preview\r\nauthor:\r\n  - Ada\r\n---\r\n\r\n# Body\r\nText\r\n"

        val result = requireNotNull(MarkdownPreviewFrontMatterParser.extract(source))

        assertEquals("title: Preview\r\nauthor:\r\n  - Ada", result.yaml)
        assertEquals("\r\n# Body\r\nText\r\n", result.markdownBody)
    }

    @Test
    fun yamlEndMarkerDoesNotConflictWithIndentedBlockContent() {
        val source = """
            ---
            title: Block scalar
            abstract: |
              First line
              ---
              Last line
            ...
            # Document
        """.trimIndent()

        val result = requireNotNull(MarkdownPreviewFrontMatterParser.extract(source))

        assertEquals(
            """
                title: Block scalar
                abstract: |
                  First line
                  ---
                  Last line
            """.trimIndent(),
            result.yaml,
        )
        assertEquals("# Document", result.markdownBody)
    }

    @Test
    fun ordinaryHorizontalRulesAndMalformedHeadersAreNotConsumed() {
        listOf(
            "---\nOrdinary paragraph\n---\n# Body",
            "Before\n---\ntitle: Not at the start\n---",
            "  ---\ntitle: Indented delimiter\n---",
            "---\n# comment only\n---\nBody",
            "---\n  title: Nested only\n---\nBody",
            "---\ntitle: Missing terminator\nBody",
            "---\ntitle: Invalid control \u0000\n---\nBody",
        ).forEach { source ->
            assertNull(source, MarkdownPreviewFrontMatterParser.extract(source))
        }
    }

    @Test
    fun metadataCharacterAndLineBudgetsAreEnforced() {
        val oversizedCharacters = buildString {
            appendLine("---")
            append("title: ")
            append("a".repeat(MarkdownPreviewFrontMatterParser.MAX_FRONT_MATTER_CHARACTERS))
            appendLine()
            appendLine("---")
            appendLine("Body")
        }
        val oversizedLines = buildString {
            appendLine("---")
            appendLine("title: Many lines")
            repeat(MarkdownPreviewFrontMatterParser.MAX_FRONT_MATTER_LINES) { index ->
                appendLine("item-$index")
            }
            appendLine("---")
            appendLine("Body")
        }

        assertNull(MarkdownPreviewFrontMatterParser.extract(oversizedCharacters))
        assertNull(MarkdownPreviewFrontMatterParser.extract(oversizedLines))
    }

    @Test
    fun trailingDelimiterWhitespaceAndEmptyDocumentBodyAreSupported() {
        val result = requireNotNull(
            MarkdownPreviewFrontMatterParser.extract("---  \ntitle: Metadata\n---\t"),
        )

        assertEquals("title: Metadata", result.yaml)
        assertEquals("", result.markdownBody)
    }

    @Test
    fun jsonStyleFlowMappingIsRecognizedAsValidYamlMetadata() {
        val result = requireNotNull(
            MarkdownPreviewFrontMatterParser.extract(
                "---\n{\"title\":\"Flow metadata\",\"format\":\"html\"}\n---\nBody",
            ),
        )

        assertEquals("{\"title\":\"Flow metadata\",\"format\":\"html\"}", result.yaml)
        assertEquals("Body", result.markdownBody)
    }
}
