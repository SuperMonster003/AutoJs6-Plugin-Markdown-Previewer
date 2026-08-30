# Release History

## v1.1.0

###### 2026/08/31

* `Feature` Added a document outline, in-page search, persistent text zoom, and render-time syntax highlighting without enabling JavaScript
* `Feature` Added safe navigation to relative Markdown documents inside the host-authorized directory, with in-viewer history and anchor handling
* `Feature` Added Android printing / PDF export and bounded YAML front matter rendering
* `Feature` Added definition and inline Footnotes with sanitized bidirectional backlinks
* `Improvement` Hardened explorer-action v1 Intent, URI, path, link, resource, HTML, and Footnotes validation while preserving the single-file read-only boundary
* `Improvement` Added a checkable Roadmap, 4 synthetic-data real-device screenshots, and reproducible README / CHANGELOG generation for 10 languages
* `Dependency` Migrated CommonMark from the legacy Atlassian 0.9.0 fork to the official Maven Central 0.30.0 core and extension modules, with core library desugaring for API 24

## v1.0.1

###### 2026/08/08

* `Fix` Plugin enablement failing in the AutoJs6 plugin center because the service returned an empty binding (onNullBinding)
* `Improvement` Streamlined the plugin name and description and unified the wording of user documentation across languages

## v1.0.0

###### 2026/08/06

* `Feature` First release of Markdown Preview: a `Preview Markdown` overflow menu action for the AutoJs6 file manager that renders a single document read-only
* `Feature` Recognizes 10 extensions (md / markdown / mdown / mkd / mkdn / mdwn / mdtext / mdtxt / rmd / qmd) plus the `text/markdown` and `text/x-markdown` MIME types
* `Feature` Renders tables, task lists, strikethrough, automatic links, heading anchors, and in-document images
* `Feature` Ships GitHub (Auto / Light / Dark), Paper, and Sepia themes with custom CSS import, manual refresh, and fullscreen mode
* `Feature` Builds a read-only security sandbox with allowlist sanitization, CSP constraints, disabled JavaScript and storage, private address filtering, and input caps (Markdown 8 MiB, CSS 256 KiB)
* `Feature` Registers the plugin service through the `org.autojs.plugin.EXPLORER_ACTION` protocol and accesses the selected file and its parent directory via temporary host-granted content URIs
* `Feature` Localizes plugin metadata, UI, instructions, and documentation into Simplified Chinese, Traditional Chinese (Hong Kong / Taiwan), English, French, Spanish, Japanese, Korean, Russian, and Arabic
