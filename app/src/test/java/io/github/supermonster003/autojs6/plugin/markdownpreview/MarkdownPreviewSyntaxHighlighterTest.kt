package io.github.supermonster003.autojs6.plugin.markdownpreview

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkdownPreviewSyntaxHighlighterTest {

    @Test
    fun commonLanguageFamiliesProduceExpectedTokenKinds() {
        val cases = listOf(
            SyntaxCase(
                language = "kotlin",
                source = "// note\nfun greet(name: String) = \"Hello\" + 42",
                expectedKinds = setOf(
                    MarkdownPreviewSyntaxTokenKind.COMMENT,
                    MarkdownPreviewSyntaxTokenKind.KEYWORD,
                    MarkdownPreviewSyntaxTokenKind.FUNCTION,
                    MarkdownPreviewSyntaxTokenKind.TYPE,
                    MarkdownPreviewSyntaxTokenKind.STRING,
                    MarkdownPreviewSyntaxTokenKind.NUMBER,
                    MarkdownPreviewSyntaxTokenKind.OPERATOR,
                ),
            ),
            SyntaxCase(
                language = "python",
                source = "def greet(name):\n    # note\n    return f\"Hello {name}\"",
                expectedKinds = setOf(
                    MarkdownPreviewSyntaxTokenKind.KEYWORD,
                    MarkdownPreviewSyntaxTokenKind.FUNCTION,
                    MarkdownPreviewSyntaxTokenKind.COMMENT,
                    MarkdownPreviewSyntaxTokenKind.STRING,
                ),
            ),
            SyntaxCase(
                language = "json",
                source = "{\"enabled\": true, \"count\": 2}",
                expectedKinds = setOf(
                    MarkdownPreviewSyntaxTokenKind.PROPERTY,
                    MarkdownPreviewSyntaxTokenKind.LITERAL,
                    MarkdownPreviewSyntaxTokenKind.NUMBER,
                ),
            ),
            SyntaxCase(
                language = "bash",
                source = "for file in *.md; do echo \$HOME; done # note",
                expectedKinds = setOf(
                    MarkdownPreviewSyntaxTokenKind.KEYWORD,
                    MarkdownPreviewSyntaxTokenKind.VARIABLE,
                    MarkdownPreviewSyntaxTokenKind.COMMENT,
                    MarkdownPreviewSyntaxTokenKind.OPERATOR,
                ),
            ),
            SyntaxCase(
                language = "sql",
                source = "SELECT count(*) FROM tasks WHERE done = FALSE;",
                expectedKinds = setOf(
                    MarkdownPreviewSyntaxTokenKind.KEYWORD,
                    MarkdownPreviewSyntaxTokenKind.FUNCTION,
                    MarkdownPreviewSyntaxTokenKind.LITERAL,
                    MarkdownPreviewSyntaxTokenKind.OPERATOR,
                ),
            ),
            SyntaxCase(
                language = "html",
                source = "<section id=\"main\">&amp;</section>",
                expectedKinds = setOf(
                    MarkdownPreviewSyntaxTokenKind.TAG,
                    MarkdownPreviewSyntaxTokenKind.ATTRIBUTE,
                    MarkdownPreviewSyntaxTokenKind.STRING,
                    MarkdownPreviewSyntaxTokenKind.LITERAL,
                    MarkdownPreviewSyntaxTokenKind.OPERATOR,
                ),
            ),
            SyntaxCase(
                language = "css",
                source = ".card { color: #fff; margin: 1rem; }",
                expectedKinds = setOf(
                    MarkdownPreviewSyntaxTokenKind.TYPE,
                    MarkdownPreviewSyntaxTokenKind.PROPERTY,
                    MarkdownPreviewSyntaxTokenKind.NUMBER,
                ),
            ),
            SyntaxCase(
                language = "yaml",
                source = "enabled: true\nitems:\n  - 2 # note",
                expectedKinds = setOf(
                    MarkdownPreviewSyntaxTokenKind.PROPERTY,
                    MarkdownPreviewSyntaxTokenKind.LITERAL,
                    MarkdownPreviewSyntaxTokenKind.NUMBER,
                    MarkdownPreviewSyntaxTokenKind.COMMENT,
                    MarkdownPreviewSyntaxTokenKind.OPERATOR,
                ),
            ),
            SyntaxCase(
                language = "markdown",
                source = "# Title\n\n- **item** with `code`",
                expectedKinds = setOf(
                    MarkdownPreviewSyntaxTokenKind.META,
                    MarkdownPreviewSyntaxTokenKind.OPERATOR,
                    MarkdownPreviewSyntaxTokenKind.STRING,
                ),
            ),
        )

        cases.forEach { case ->
            val tokens = requireNotNull(
                MarkdownPreviewSyntaxHighlighter.tokenize(case.language, case.source),
            )
            assertTrue("${case.language} should produce syntax tokens", tokens.isNotEmpty())
            assertTrue(
                "${case.language} is missing ${case.expectedKinds - tokens.map { it.kind }.toSet()}",
                tokens.map { it.kind }.toSet().containsAll(case.expectedKinds),
            )
            assertOrderedRanges(case.source, tokens)
        }
    }

    @Test
    fun aliasesAreRecognizedAndUnknownLanguagesAreIgnored() {
        assertNotNull(MarkdownPreviewSyntaxHighlighter.tokenize("KT", "val answer = 42"))
        assertNotNull(MarkdownPreviewSyntaxHighlighter.tokenize("c++", "constexpr int answer = 42;"))
        assertNotNull(MarkdownPreviewSyntaxHighlighter.tokenize("c#", "public record Answer(int value);"))
        assertNull(MarkdownPreviewSyntaxHighlighter.tokenize("unknown-language", "value"))
        assertNull(MarkdownPreviewSyntaxHighlighter.tokenize("invalid language", "value"))
    }

    @Test
    fun oversizedOrTokenDenseCodeBlocksSafelyFallBackToPlainText() {
        val oversizedSource = "x".repeat(MarkdownPreviewSyntaxHighlighter.MAX_CODE_CHARACTERS + 1)
        val tokenDenseSource = "true ".repeat(MarkdownPreviewSyntaxHighlighter.MAX_TOKENS + 1)
        val oversizedTokens = requireNotNull(
            MarkdownPreviewSyntaxHighlighter.tokenize("kotlin", oversizedSource),
        )
        val denseTokens = requireNotNull(
            MarkdownPreviewSyntaxHighlighter.tokenize("json", tokenDenseSource),
        )

        assertTrue(oversizedTokens.isEmpty())
        assertTrue(denseTokens.isEmpty())
    }

    private fun assertOrderedRanges(
        source: String,
        tokens: List<MarkdownPreviewSyntaxToken>,
    ) {
        var previousEnd = 0
        tokens.forEach { token ->
            assertTrue(token.start >= previousEnd)
            assertTrue(token.endExclusive > token.start)
            assertTrue(token.endExclusive <= source.length)
            previousEnd = token.endExclusive
        }
    }

    private data class SyntaxCase(
        val language: String,
        val source: String,
        val expectedKinds: Set<MarkdownPreviewSyntaxTokenKind>,
    )
}
