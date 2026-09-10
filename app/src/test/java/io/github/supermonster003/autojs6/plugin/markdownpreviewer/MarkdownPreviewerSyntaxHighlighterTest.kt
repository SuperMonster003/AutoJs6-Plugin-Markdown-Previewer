package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkdownPreviewerSyntaxHighlighterTest {

    @Test
    fun commonLanguageFamiliesProduceExpectedTokenKinds() {
        val cases = listOf(
            SyntaxCase(
                language = "kotlin",
                source = "// note\nfun greet(name: String) = \"Hello\" + 42",
                expectedKinds = setOf(
                    MarkdownPreviewerSyntaxTokenKind.COMMENT,
                    MarkdownPreviewerSyntaxTokenKind.KEYWORD,
                    MarkdownPreviewerSyntaxTokenKind.FUNCTION,
                    MarkdownPreviewerSyntaxTokenKind.TYPE,
                    MarkdownPreviewerSyntaxTokenKind.STRING,
                    MarkdownPreviewerSyntaxTokenKind.NUMBER,
                    MarkdownPreviewerSyntaxTokenKind.OPERATOR,
                ),
            ),
            SyntaxCase(
                language = "python",
                source = "def greet(name):\n    # note\n    return f\"Hello {name}\"",
                expectedKinds = setOf(
                    MarkdownPreviewerSyntaxTokenKind.KEYWORD,
                    MarkdownPreviewerSyntaxTokenKind.FUNCTION,
                    MarkdownPreviewerSyntaxTokenKind.COMMENT,
                    MarkdownPreviewerSyntaxTokenKind.STRING,
                ),
            ),
            SyntaxCase(
                language = "json",
                source = "{\"enabled\": true, \"count\": 2}",
                expectedKinds = setOf(
                    MarkdownPreviewerSyntaxTokenKind.PROPERTY,
                    MarkdownPreviewerSyntaxTokenKind.LITERAL,
                    MarkdownPreviewerSyntaxTokenKind.NUMBER,
                ),
            ),
            SyntaxCase(
                language = "bash",
                source = "for file in *.md; do echo \$HOME; done # note",
                expectedKinds = setOf(
                    MarkdownPreviewerSyntaxTokenKind.KEYWORD,
                    MarkdownPreviewerSyntaxTokenKind.VARIABLE,
                    MarkdownPreviewerSyntaxTokenKind.COMMENT,
                    MarkdownPreviewerSyntaxTokenKind.OPERATOR,
                ),
            ),
            SyntaxCase(
                language = "sql",
                source = "SELECT count(*) FROM tasks WHERE done = FALSE;",
                expectedKinds = setOf(
                    MarkdownPreviewerSyntaxTokenKind.KEYWORD,
                    MarkdownPreviewerSyntaxTokenKind.FUNCTION,
                    MarkdownPreviewerSyntaxTokenKind.LITERAL,
                    MarkdownPreviewerSyntaxTokenKind.OPERATOR,
                ),
            ),
            SyntaxCase(
                language = "html",
                source = "<section id=\"main\">&amp;</section>",
                expectedKinds = setOf(
                    MarkdownPreviewerSyntaxTokenKind.TAG,
                    MarkdownPreviewerSyntaxTokenKind.ATTRIBUTE,
                    MarkdownPreviewerSyntaxTokenKind.STRING,
                    MarkdownPreviewerSyntaxTokenKind.LITERAL,
                    MarkdownPreviewerSyntaxTokenKind.OPERATOR,
                ),
            ),
            SyntaxCase(
                language = "css",
                source = ".card { color: #fff; margin: 1rem; }",
                expectedKinds = setOf(
                    MarkdownPreviewerSyntaxTokenKind.TYPE,
                    MarkdownPreviewerSyntaxTokenKind.PROPERTY,
                    MarkdownPreviewerSyntaxTokenKind.NUMBER,
                ),
            ),
            SyntaxCase(
                language = "yaml",
                source = "enabled: true\nitems:\n  - 2 # note",
                expectedKinds = setOf(
                    MarkdownPreviewerSyntaxTokenKind.PROPERTY,
                    MarkdownPreviewerSyntaxTokenKind.LITERAL,
                    MarkdownPreviewerSyntaxTokenKind.NUMBER,
                    MarkdownPreviewerSyntaxTokenKind.COMMENT,
                    MarkdownPreviewerSyntaxTokenKind.OPERATOR,
                ),
            ),
            SyntaxCase(
                language = "markdown",
                source = "# Title\n\n- **item** with `code`",
                expectedKinds = setOf(
                    MarkdownPreviewerSyntaxTokenKind.META,
                    MarkdownPreviewerSyntaxTokenKind.OPERATOR,
                    MarkdownPreviewerSyntaxTokenKind.STRING,
                ),
            ),
        )

        cases.forEach { case ->
            val tokens = requireNotNull(
                MarkdownPreviewerSyntaxHighlighter.tokenize(case.language, case.source),
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
        assertNotNull(MarkdownPreviewerSyntaxHighlighter.tokenize("KT", "val answer = 42"))
        assertNotNull(MarkdownPreviewerSyntaxHighlighter.tokenize("c++", "constexpr int answer = 42;"))
        assertNotNull(MarkdownPreviewerSyntaxHighlighter.tokenize("c#", "public record Answer(int value);"))
        assertNull(MarkdownPreviewerSyntaxHighlighter.tokenize("unknown-language", "value"))
        assertNull(MarkdownPreviewerSyntaxHighlighter.tokenize("invalid language", "value"))
    }

    @Test
    fun oversizedOrTokenDenseCodeBlocksSafelyFallBackToPlainText() {
        val oversizedSource = "x".repeat(MarkdownPreviewerSyntaxHighlighter.MAX_CODE_CHARACTERS + 1)
        val tokenDenseSource = "true ".repeat(MarkdownPreviewerSyntaxHighlighter.MAX_TOKENS + 1)
        val oversizedTokens = requireNotNull(
            MarkdownPreviewerSyntaxHighlighter.tokenize("kotlin", oversizedSource),
        )
        val denseTokens = requireNotNull(
            MarkdownPreviewerSyntaxHighlighter.tokenize("json", tokenDenseSource),
        )

        assertTrue(oversizedTokens.isEmpty())
        assertTrue(denseTokens.isEmpty())
    }

    private fun assertOrderedRanges(
        source: String,
        tokens: List<MarkdownPreviewerSyntaxToken>,
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
        val expectedKinds: Set<MarkdownPreviewerSyntaxTokenKind>,
    )
}
