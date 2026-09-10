package io.github.supermonster003.autojs6.plugin.markdownpreviewer

import java.util.Locale

internal enum class MarkdownPreviewerSyntaxTokenKind(
    val cssClassName: String,
) {
    ATTRIBUTE("syntax-attribute"),
    COMMENT("syntax-comment"),
    FUNCTION("syntax-function"),
    KEYWORD("syntax-keyword"),
    LITERAL("syntax-literal"),
    META("syntax-meta"),
    NUMBER("syntax-number"),
    OPERATOR("syntax-operator"),
    PROPERTY("syntax-property"),
    STRING("syntax-string"),
    TAG("syntax-tag"),
    TYPE("syntax-type"),
    VARIABLE("syntax-variable"),
}

internal data class MarkdownPreviewerSyntaxToken(
    val kind: MarkdownPreviewerSyntaxTokenKind,
    val start: Int,
    val endExclusive: Int,
)

internal object MarkdownPreviewerSyntaxHighlighter {

    const val MAX_CODE_CHARACTERS = 256 * 1024
    const val MAX_TOKENS = 20_000

    private val definitions by lazy {
        buildDefinitions()
    }

    fun tokenize(languageAlias: String, source: String): List<MarkdownPreviewerSyntaxToken>? {
        val normalizedAlias = languageAlias.trim().lowercase(Locale.ROOT)
        if (!LANGUAGE_ALIAS_PATTERN.matches(normalizedAlias)) return null
        val definition = definitions[normalizedAlias] ?: return null
        if (source.isEmpty() || source.length > MAX_CODE_CHARACTERS) return emptyList()

        val tokens = ArrayList<MarkdownPreviewerSyntaxToken>()
        for (match in definition.pattern.findAll(source)) {
            val ruleIndex = definition.rules.indices.firstOrNull { index ->
                match.groups[index + 1] != null
            } ?: continue
            if (tokens.size >= MAX_TOKENS) return emptyList()
            tokens += MarkdownPreviewerSyntaxToken(
                kind = definition.rules[ruleIndex].kind,
                start = match.range.first,
                endExclusive = match.range.last + 1,
            )
        }
        return tokens
    }

    private fun buildDefinitions(): Map<String, SyntaxDefinition> {
        val result = mutableMapOf<String, SyntaxDefinition>()

        fun register(definition: SyntaxDefinition, vararg aliases: String) {
            aliases.forEach { alias -> result[alias] = definition }
        }

        val kotlin = codeDefinition(
            keywords = KOTLIN_KEYWORDS,
            leadingRules = listOf(
                SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, TRIPLE_DOUBLE_QUOTED_STRING),
                SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, ANNOTATION_PATTERN),
            ),
        )
        val java = codeDefinition(
            keywords = JAVA_KEYWORDS,
            leadingRules = listOf(
                SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, TRIPLE_DOUBLE_QUOTED_STRING),
                SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, ANNOTATION_PATTERN),
            ),
        )
        val javascript = codeDefinition(
            keywords = JAVASCRIPT_KEYWORDS,
            leadingRules = listOf(
                SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, TEMPLATE_STRING),
            ),
        )
        val typescript = codeDefinition(
            keywords = JAVASCRIPT_KEYWORDS + TYPESCRIPT_KEYWORDS,
            leadingRules = listOf(
                SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, TEMPLATE_STRING),
                SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, ANNOTATION_PATTERN),
            ),
        )
        val c = codeDefinition(C_KEYWORDS)
        val cpp = codeDefinition(C_KEYWORDS + CPP_KEYWORDS)
        val csharp = codeDefinition(CSHARP_KEYWORDS)
        val go = codeDefinition(GO_KEYWORDS)
        val rust = codeDefinition(
            keywords = RUST_KEYWORDS,
            leadingRules = listOf(
                SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, "#!?\\[[^]\\r\\n]+]"),
                SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, "'[_A-Za-z][_A-Za-z0-9]*"),
            ),
        )
        val swift = codeDefinition(
            keywords = SWIFT_KEYWORDS,
            leadingRules = listOf(
                SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, TRIPLE_DOUBLE_QUOTED_STRING),
                SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, ANNOTATION_PATTERN),
            ),
        )
        val dart = codeDefinition(
            keywords = DART_KEYWORDS,
            leadingRules = listOf(
                SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, ANNOTATION_PATTERN),
            ),
        )

        register(kotlin, "kotlin", "kt", "kts")
        register(java, "java")
        register(javascript, "javascript", "js", "jsx", "mjs", "cjs")
        register(typescript, "typescript", "ts", "tsx")
        register(c, "c", "h")
        register(cpp, "cpp", "c++", "cc", "cxx", "hpp", "h++")
        register(csharp, "csharp", "cs", "c#")
        register(go, "go", "golang")
        register(rust, "rust", "rs")
        register(swift, "swift")
        register(dart, "dart")
        register(pythonDefinition(), "python", "py", "py3")
        register(jsonDefinition(), "json", "jsonc")
        register(shellDefinition(), "bash", "sh", "shell", "zsh")
        register(sqlDefinition(), "sql")
        register(markupDefinition(), "html", "htm", "xml", "xhtml", "svg")
        register(cssDefinition(), "css", "scss", "sass", "less")
        register(yamlDefinition(), "yaml", "yml")
        register(markdownDefinition(), "markdown", "md", "mdx")
        return result
    }

    private fun codeDefinition(
        keywords: Set<String>,
        leadingRules: List<SyntaxRule> = emptyList(),
    ): SyntaxDefinition = SyntaxDefinition(
        buildList {
            add(SyntaxRule(MarkdownPreviewerSyntaxTokenKind.COMMENT, BLOCK_COMMENT))
            add(SyntaxRule(MarkdownPreviewerSyntaxTokenKind.COMMENT, SLASH_LINE_COMMENT))
            addAll(leadingRules)
            add(SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, DOUBLE_QUOTED_STRING))
            add(SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, SINGLE_QUOTED_STRING))
            add(SyntaxRule(MarkdownPreviewerSyntaxTokenKind.KEYWORD, wordPattern(keywords)))
            add(SyntaxRule(MarkdownPreviewerSyntaxTokenKind.LITERAL, wordPattern(COMMON_LITERALS)))
            add(SyntaxRule(MarkdownPreviewerSyntaxTokenKind.NUMBER, NUMBER_PATTERN))
            add(SyntaxRule(MarkdownPreviewerSyntaxTokenKind.FUNCTION, FUNCTION_PATTERN))
            add(SyntaxRule(MarkdownPreviewerSyntaxTokenKind.TYPE, TYPE_PATTERN))
            add(SyntaxRule(MarkdownPreviewerSyntaxTokenKind.OPERATOR, OPERATOR_PATTERN))
        },
    )

    private fun pythonDefinition(): SyntaxDefinition = SyntaxDefinition(
        listOf(
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, PYTHON_TRIPLE_DOUBLE_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, PYTHON_TRIPLE_SINGLE_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, PYTHON_DOUBLE_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, PYTHON_SINGLE_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.COMMENT, HASH_LINE_COMMENT),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, ANNOTATION_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.KEYWORD, wordPattern(PYTHON_KEYWORDS)),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.LITERAL, wordPattern(PYTHON_LITERALS)),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.NUMBER, NUMBER_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.FUNCTION, FUNCTION_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.TYPE, TYPE_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.OPERATOR, OPERATOR_PATTERN),
        ),
    )

    private fun jsonDefinition(): SyntaxDefinition = SyntaxDefinition(
        listOf(
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.COMMENT, BLOCK_COMMENT),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.COMMENT, SLASH_LINE_COMMENT),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.PROPERTY, JSON_PROPERTY),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, DOUBLE_QUOTED_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.LITERAL, wordPattern(JSON_LITERALS)),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.NUMBER, JSON_NUMBER_PATTERN),
        ),
    )

    private fun shellDefinition(): SyntaxDefinition = SyntaxDefinition(
        listOf(
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.COMMENT, HASH_LINE_COMMENT),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, SINGLE_QUOTED_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, DOUBLE_QUOTED_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, TEMPLATE_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.VARIABLE, SHELL_VARIABLE_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.KEYWORD, wordPattern(SHELL_KEYWORDS)),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.LITERAL, wordPattern(SHELL_LITERALS)),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.NUMBER, NUMBER_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.FUNCTION, FUNCTION_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.OPERATOR, SHELL_OPERATOR_PATTERN),
        ),
    )

    private fun sqlDefinition(): SyntaxDefinition = SyntaxDefinition(
        rules = listOf(
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.COMMENT, BLOCK_COMMENT),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.COMMENT, SQL_LINE_COMMENT),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, SQL_SINGLE_QUOTED_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, DOUBLE_QUOTED_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, TEMPLATE_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.KEYWORD, wordPattern(SQL_KEYWORDS)),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.LITERAL, wordPattern(SQL_LITERALS)),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.NUMBER, JSON_NUMBER_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.FUNCTION, FUNCTION_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.OPERATOR, OPERATOR_PATTERN),
        ),
        options = setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE),
    )

    private fun markupDefinition(): SyntaxDefinition = SyntaxDefinition(
        listOf(
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.COMMENT, HTML_COMMENT),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, CDATA_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, MARKUP_META_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, DOUBLE_QUOTED_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, SINGLE_QUOTED_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.TAG, MARKUP_TAG_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.ATTRIBUTE, MARKUP_ATTRIBUTE_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.LITERAL, HTML_ENTITY_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.OPERATOR, MARKUP_CLOSE_PATTERN),
        ),
    )

    private fun cssDefinition(): SyntaxDefinition = SyntaxDefinition(
        listOf(
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.COMMENT, BLOCK_COMMENT),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, DOUBLE_QUOTED_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, SINGLE_QUOTED_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, CSS_AT_RULE_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.PROPERTY, CSS_PROPERTY_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.NUMBER, CSS_HEX_COLOR_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.NUMBER, CSS_NUMBER_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.LITERAL, CSS_IMPORTANT_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.FUNCTION, FUNCTION_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.TYPE, CSS_SELECTOR_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.OPERATOR, OPERATOR_PATTERN),
        ),
    )

    private fun yamlDefinition(): SyntaxDefinition = SyntaxDefinition(
        listOf(
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, DOUBLE_QUOTED_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, SINGLE_QUOTED_STRING),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.COMMENT, HASH_LINE_COMMENT),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, YAML_DOCUMENT_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, YAML_ANCHOR_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.PROPERTY, YAML_PROPERTY_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.LITERAL, wordPattern(YAML_LITERALS)),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.NUMBER, JSON_NUMBER_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.OPERATOR, YAML_OPERATOR_PATTERN),
        ),
    )

    private fun markdownDefinition(): SyntaxDefinition = SyntaxDefinition(
        listOf(
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.COMMENT, HTML_COMMENT),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, MARKDOWN_INLINE_CODE_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, MARKDOWN_FENCE_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, MARKDOWN_HEADING_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.META, MARKDOWN_LIST_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.STRING, MARKDOWN_LINK_TARGET_PATTERN),
            SyntaxRule(MarkdownPreviewerSyntaxTokenKind.OPERATOR, MARKDOWN_EMPHASIS_PATTERN),
        ),
    )

    private fun wordPattern(words: Set<String>): String =
        words.joinToString(prefix = "\\b(?:", postfix = ")\\b", separator = "|") { word ->
            Regex.escape(word)
        }

    private fun words(value: String): Set<String> =
        value.split(' ', '\n', '\r', '\t').filter(String::isNotEmpty).toSet()

    private data class SyntaxRule(
        val kind: MarkdownPreviewerSyntaxTokenKind,
        val pattern: String,
    )

    private class SyntaxDefinition(
        val rules: List<SyntaxRule>,
        options: Set<RegexOption> = setOf(RegexOption.MULTILINE),
    ) {
        val pattern = Regex(
            rules.joinToString(separator = "|") { rule -> "(${rule.pattern})" },
            options,
        )

        init {
            check(pattern.toPattern().matcher("").groupCount() == rules.size) {
                "Syntax rules must use non-capturing groups internally"
            }
        }
    }

    private const val BLOCK_COMMENT = "/\\*[\\s\\S]*?(?:\\*/|\\z)"
    private const val SLASH_LINE_COMMENT = "//[^\\r\\n]*"
    private const val HASH_LINE_COMMENT = "#[^\\r\\n]*"
    private const val SQL_LINE_COMMENT = "--[^\\r\\n]*"
    private const val DOUBLE_QUOTED_STRING = "\"(?:\\\\.|[^\"\\\\\\r\\n])*\""
    private const val SINGLE_QUOTED_STRING = "'(?:\\\\.|[^'\\\\\\r\\n])*'"
    private const val TRIPLE_DOUBLE_QUOTED_STRING = "\"\"\"[\\s\\S]*?(?:\"\"\"|\\z)"
    private const val TEMPLATE_STRING = "`(?:\\\\.|[^`\\\\])*`"
    private const val ANNOTATION_PATTERN = "@[A-Za-z_][A-Za-z0-9_.]*"
    private const val NUMBER_PATTERN =
        "\\b(?:0[xX][0-9a-fA-F](?:_?[0-9a-fA-F])*|0[bB][01](?:_?[01])*|" +
            "\\d(?:_?\\d)*(?:\\.\\d(?:_?\\d)*)?(?:[eE][+-]?\\d(?:_?\\d)*)?[fFdDlLuU]*)\\b"
    private const val JSON_NUMBER_PATTERN = "-?\\b(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?\\b"
    private const val FUNCTION_PATTERN = "\\b[A-Za-z_$][A-Za-z0-9_$]*(?=\\s*\\()"
    private const val TYPE_PATTERN = "\\b[A-Z][A-Za-z0-9_$]*\\b"
    private const val OPERATOR_PATTERN = "[+\\-*/%=&|!<>^~?:]+"

    private const val PYTHON_STRING_PREFIX = "(?i:(?:r|u|b|f|br|rb|fr|rf)?)"
    private const val PYTHON_TRIPLE_DOUBLE_STRING =
        "$PYTHON_STRING_PREFIX\"\"\"[\\s\\S]*?(?:\"\"\"|\\z)"
    private const val PYTHON_TRIPLE_SINGLE_STRING =
        "$PYTHON_STRING_PREFIX'''[\\s\\S]*?(?:'''|\\z)"
    private const val PYTHON_DOUBLE_STRING = "$PYTHON_STRING_PREFIX$DOUBLE_QUOTED_STRING"
    private const val PYTHON_SINGLE_STRING = "$PYTHON_STRING_PREFIX$SINGLE_QUOTED_STRING"

    private const val JSON_PROPERTY = "\"(?:\\\\.|[^\"\\\\\\r\\n])*\"(?=\\s*:)"
    private const val SHELL_VARIABLE_PATTERN =
        "\\$(?:[A-Za-z_][A-Za-z0-9_]*|[0-9]+|\\{[^}\\r\\n]+\\}|[@*#?!_\\$-])"
    private const val SHELL_OPERATOR_PATTERN = "(?:&&|\\|\\||;;|[|&;<>])"
    private const val SQL_SINGLE_QUOTED_STRING = "'(?:''|[^'])*'"

    private const val HTML_COMMENT = "<!--[\\s\\S]*?(?:-->|\\z)"
    private const val CDATA_PATTERN = "<!\\[CDATA\\[[\\s\\S]*?(?:]]>|\\z)"
    private const val MARKUP_META_PATTERN = "(?:<![A-Za-z][^>]*>|<\\?[\\s\\S]*?(?:\\?>|\\z))"
    private const val MARKUP_TAG_PATTERN = "</?[A-Za-z][A-Za-z0-9:._-]*"
    private const val MARKUP_ATTRIBUTE_PATTERN = "[A-Za-z_:][A-Za-z0-9:._-]*(?=\\s*=)"
    private const val HTML_ENTITY_PATTERN = "&(?:#[0-9]+|#x[0-9a-fA-F]+|[A-Za-z][A-Za-z0-9]+);"
    private const val MARKUP_CLOSE_PATTERN = "/?>"

    private const val CSS_AT_RULE_PATTERN = "@[A-Za-z_-][A-Za-z0-9_-]*"
    private const val CSS_PROPERTY_PATTERN = "(?:--)?[A-Za-z_][A-Za-z0-9_-]*(?=\\s*:)"
    private const val CSS_HEX_COLOR_PATTERN = "#[0-9a-fA-F]{3,8}\\b"
    private const val CSS_NUMBER_PATTERN =
        "-?(?:\\d+(?:\\.\\d+)?|\\.\\d+)(?:%|[A-Za-z]+)?\\b"
    private const val CSS_IMPORTANT_PATTERN = "!important\\b"
    private const val CSS_SELECTOR_PATTERN = "[.#][A-Za-z_-][A-Za-z0-9_-]*"

    private const val YAML_DOCUMENT_PATTERN = "^\\s*(?:---|\\.\\.\\.)\\s*$"
    private const val YAML_ANCHOR_PATTERN = "[&*!][A-Za-z0-9_.-]+"
    private const val YAML_PROPERTY_PATTERN = "\\b[A-Za-z0-9_.-]+(?=\\s*:)"
    private const val YAML_OPERATOR_PATTERN = "^\\s*(?:-|\\?)|[|>]"

    private const val MARKDOWN_INLINE_CODE_PATTERN = "`[^`\\r\\n]+`"
    private const val MARKDOWN_FENCE_PATTERN = "^ {0,3}(?:`{3,}|~{3,})[^\\r\\n]*$"
    private const val MARKDOWN_HEADING_PATTERN = "^ {0,3}#{1,6}(?=\\s)"
    private const val MARKDOWN_LIST_PATTERN = "^\\s*(?:>|[-+*]|[0-9]+[.)])(?=\\s)"
    private const val MARKDOWN_LINK_TARGET_PATTERN = "(?<=]\\()[^)\\r\\n]+(?=\\))"
    private const val MARKDOWN_EMPHASIS_PATTERN = "[*_~]{1,2}"

    private val COMMON_LITERALS = words("true false null undefined NaN Infinity")
    private val JSON_LITERALS = words("true false null")
    private val PYTHON_LITERALS = words("True False None NotImplemented Ellipsis")
    private val SHELL_LITERALS = words("true false")
    private val SQL_LITERALS = words("NULL TRUE FALSE UNKNOWN")
    private val YAML_LITERALS = words("true false null Null NULL yes no on off")

    private val KOTLIN_KEYWORDS = words(
        "as break by catch class companion const constructor continue crossinline data delegate do " +
            "dynamic else enum expect external false field file final finally for fun get if import in " +
            "infix init inline inner interface internal is lateinit noinline null object open operator out " +
            "override package param private property protected public receiver reified return sealed set " +
            "setparam super suspend tailrec this throw true try typealias typeof val var vararg when where while",
    )
    private val JAVA_KEYWORDS = words(
        "abstract assert boolean break byte case catch char class const continue default do double else enum " +
            "exports extends final finally float for goto if implements import instanceof int interface long " +
            "module native new non-sealed open opens package permits private protected provides public record " +
            "requires return sealed short static strictfp super switch synchronized this throw throws to " +
            "transient transitive try uses var void volatile when while with yield",
    )
    private val JAVASCRIPT_KEYWORDS = words(
        "as async await break case catch class const continue debugger default delete do else export extends " +
            "finally for from function get if import in instanceof let new of return set static super switch " +
            "this throw try typeof var void while with yield",
    )
    private val TYPESCRIPT_KEYWORDS = words(
        "abstract any boolean constructor declare enum implements infer interface is keyof module namespace " +
            "never number object override private protected public readonly require satisfies string symbol " +
            "type undefined unique unknown using",
    )
    private val C_KEYWORDS = words(
        "auto break case char const continue default do double else enum extern float for goto if inline int " +
            "long register restrict return short signed sizeof static struct switch typedef union unsigned void " +
            "volatile while _Alignas _Alignof _Atomic _Bool _Complex _Generic _Imaginary _Noreturn _Static_assert " +
            "_Thread_local",
    )
    private val CPP_KEYWORDS = words(
        "alignas alignof and and_eq asm bitand bitor bool catch char8_t char16_t char32_t class compl concept " +
            "consteval constexpr constinit const_cast co_await co_return co_yield decltype delete dynamic_cast " +
            "explicit export false friend mutable namespace new noexcept not not_eq nullptr operator or or_eq " +
            "private protected public reinterpret_cast requires static_assert static_cast template this " +
            "thread_local throw true try typeid typename using virtual wchar_t xor xor_eq",
    )
    private val CSHARP_KEYWORDS = words(
        "abstract as base bool break byte case catch char checked class const continue decimal default delegate " +
            "do double else enum event explicit extern false finally fixed float for foreach goto if implicit in " +
            "int interface internal is lock long namespace new null object operator out override params private " +
            "protected public readonly record ref return sbyte sealed short sizeof stackalloc static string struct " +
            "switch this throw true try typeof uint ulong unchecked unsafe ushort using virtual void volatile while " +
            "async await dynamic get init partial set value var when where yield",
    )
    private val GO_KEYWORDS = words(
        "break case chan const continue default defer else fallthrough for func go goto if import interface map " +
            "package range return select struct switch type var",
    )
    private val RUST_KEYWORDS = words(
        "as async await break const continue crate dyn else enum extern false fn for if impl in let loop macro " +
            "match mod move mut pub ref return self Self static struct super trait true type union unsafe use where while yield",
    )
    private val SWIFT_KEYWORDS = words(
        "actor any as associatedtype async await break case catch class continue convenience copy consuming " +
            "defer deinit didSet distributed do dynamic else enum extension fallthrough false file fileprivate " +
            "final for func get guard if import indirect infix init inout internal is isolated lazy let macro " +
            "mutating nil nonisolated nonmutating open operator optional override package postfix precedencegroup " +
            "prefix private protocol public repeat required rethrows return self Self set some static struct subscript " +
            "super switch throws true try typealias unowned var weak where while willSet",
    )
    private val DART_KEYWORDS = words(
        "abstract as assert async await base break case catch class const continue covariant default deferred do " +
            "dynamic else enum export extends extension external factory false final finally for function get hide " +
            "if implements import in interface is late library mixin new null of on operator part required rethrow " +
            "return sealed set show static super switch sync this throw true try type typedef var void when while with yield",
    )
    private val PYTHON_KEYWORDS = words(
        "and as assert async await break class continue def del elif else except finally for from global if import " +
            "in is lambda nonlocal not or pass raise return try while with yield match case type",
    )
    private val SHELL_KEYWORDS = words(
        "case do done elif else esac fi for function if in select then time until while coproc",
    )
    private val SQL_KEYWORDS = words(
        "ADD ALL ALTER AND ANY AS ASC BEGIN BETWEEN BY CASE CHECK COLUMN COMMIT CONSTRAINT CREATE CROSS DATABASE " +
            "DEFAULT DELETE DESC DISTINCT DROP ELSE END ESCAPE EXISTS FOREIGN FROM FULL GRANT GROUP HAVING IN INDEX " +
            "INNER INSERT INTERSECT INTO IS JOIN KEY LEFT LIKE LIMIT NOT OFFSET ON OR ORDER OUTER PRIMARY PROCEDURE " +
            "REFERENCES RETURNING RIGHT ROLLBACK ROW SELECT SET TABLE THEN TRIGGER UNION UNIQUE UPDATE USING VALUES " +
            "VIEW WHEN WHERE WITH",
    )

    private val LANGUAGE_ALIAS_PATTERN = Regex("^[a-z0-9][a-z0-9+#.-]{0,31}$")
}
