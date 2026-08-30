---
title: Markdown Preview
description: Safe, focused reading inside AutoJs6
status: Ready
updated: 2026-08-31
---

# Markdown Preview

**Read safely. Navigate quickly. Export cleanly.**

Markdown Preview turns source text into a calm, readable document without leaving the AutoJs6 file manager.

> Documents stay read-only. JavaScript remains disabled, links are validated, and rendered HTML is sanitized before it reaches the WebView.

## Highlights

- [x] Document outline and in-page search
- [x] Five themes, custom CSS, and adjustable text size
- [x] Syntax highlighting without client-side scripts
- [x] Relative document navigation and PDF printing
- [x] YAML metadata and footnotes[^security]

## Release health

| Check | Result |
| :--- | ---: |
| JVM tests | 53 / 53 |
| Android 9-15 | 84 / 84 |
| Lint errors | 0 |

## Code sample

```kotlin
val preview = MarkdownPreview(
    scriptsEnabled = false,
    access = AccessMode.ReadOnly,
)
```

## A second document

Open [Release notes](release-notes.md) to try safe in-viewer navigation.

[^security]: Footnotes use sanitized anchors and bidirectional links, including repeated references.
