# Release History

## v1.2.0

###### 2026/09/13

* `Feature` Local release history is available from the interface, with localized text and an English fallback
* `Fix` HTTPS resources use verified public DNS addresses with every redirect checked; direct WebView networking is blocked and resource requests support GET and HEAD only
* `Improvement` Release packages are checked for a complete signing configuration, exact APK contents and reproducible documentation
* `Dependency` Add OkHttp 4.12.0 for controlled HTTPS resource loading

## v1.1.0

###### 2026/09/11

* `Feature` Added a document outline, in-page search, persistent text zoom, and render-time syntax highlighting without enabling JavaScript
* `Feature` Added safe navigation to relative Markdown documents inside the host-authorized directory, with in-viewer history and anchor handling
* `Feature` Added Android printing / PDF export and bounded YAML front matter rendering
* `Feature` Added definition and inline Footnotes with sanitized bidirectional backlinks
* `Fix` Fixed primary previewer protocol rejection and settings crashes; synchronized the host appearance, page chrome, and monochrome dialog controls
* `Improvement` Hardened explorer-action v1 Intent, URI, path, link, resource, HTML, and Footnotes validation while preserving the single-file read-only boundary
* `Improvement` Added a checkable Roadmap, 4 synthetic-data real-device screenshots, and reproducible README / CHANGELOG generation for 10 languages
* `Improvement` Build verification rejects accidental native dependencies and produces a JSON report
* `Dependency` Migrated CommonMark from the legacy Atlassian 0.9.0 fork to the official Maven Central 0.30.0 core and extension modules, with core library desugaring for API 24

## v1.0.1

###### 2026/08/08

* `Fix` Plugin enablement failing in the AutoJs6 plugin center because the service returned an empty binding (onNullBinding)
* `Improvement` Streamlined the plugin name and description and unified the wording of user documentation across languages

## v1.0.0

###### 2026/08/06

* `Feature` First release of Markdown Previewer: a `Markdown Previewer` overflow menu action for the AutoJs6 file manager that renders a single document read-only
* `Feature` Recognizes 10 extensions (md / markdown / mdown / mkd / mkdn / mdwn / mdtext / mdtxt / rmd / qmd) plus the `text/markdown` and `text/x-markdown` MIME types
* `Feature` Renders tables, task lists, strikethrough, automatic links, heading anchors, and in-document images
* `Feature` Ships GitHub (Auto / Light / Dark), Paper, and Sepia themes with custom CSS import, manual refresh, and fullscreen mode
* `Feature` Builds a read-only security sandbox with allowlist sanitization, CSP constraints, disabled JavaScript and storage, private address filtering, and input caps (Markdown 8 MiB, CSS 256 KiB)
* `Feature` Registers the plugin service through the `org.autojs.plugin.EXPLORER_ACTION` protocol and accesses the selected file and its parent directory via temporary host-granted content URIs
* `Feature` Localizes plugin metadata, UI, instructions, and documentation into Simplified Chinese, Traditional Chinese (Hong Kong / Taiwan), English, French, Spanish, Japanese, Korean, Russian, and Arabic
