<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="markdown-previewer-ic-launcher" border="0" width="128" />
  </p>

  <p>Preview Markdown documents</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Languages

******

The current README.md supports the following languages:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-TW.md)
- English [en] # current
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ar.md)

******

### Introduction

******

Markdown Previewer is a previewer plugin for the AutoJs6 file manager. Once enabled, every Markdown file in the file manager gains a `Markdown Previewer` action in its overflow menu. Tap it to read the document nicely rendered, like a web page, instead of a wall of raw source text.

The plugin does one thing and does it safely: read-only rendering. The viewer never executes scripts, can only read files temporarily authorized by the host, and renders everything in its own screen. It does not modify AutoJs6 itself and has no effect on the script runtime.

******

### Highlights

******

- Read rendered Markdown documents right inside the AutoJs6 file manager, with no file exports and no third-party reader apps.
- Tables, task lists, strikethrough, automatic links, heading anchors, and in-document images work out of the box, covering common GitHub-style writing.
- GitHub (Auto), GitHub Light, GitHub Dark, Paper, Sepia. Menus and dialogs follow the AutoJs6 language and dark mode. GitHub (Auto), or the automatic HTML theme, follows AutoJs6 as well. The toolbar and system bars use one representative color sampled from the rendered page edges, with contrasting black or white text and icons. For gradients, images, and animation, that color stays fixed until reload to avoid flicker.
- Import a custom CSS file to build your own reading style. It layers on top of the built-in styling and can be cleared with one tap.
- Immersive fullscreen mode with an optional `Start in fullscreen mode` preference. The back button leaves fullscreen first instead of closing the page.
- Pinch to zoom, manual refresh, horizontally scrollable tables, and automatic BOM-based detection of UTF-8 / UTF-16 / UTF-32 encodings.
- A read-only security sandbox: no JavaScript execution, no disk caches, and no access to any data beyond the files authorized by the host.

******

### Screenshots

******

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/file-menu-action.png?raw=true" alt="File menu action" width="300" />
      <br />
      <sub>File menu action</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/viewer.png?raw=true" alt="Document viewer" width="300" />
      <br />
      <sub>Document viewer</sub>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/theme-dialog.png?raw=true" alt="Theme picker" width="300" />
      <br />
      <sub>Theme picker</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/fullscreen.png?raw=true" alt="Fullscreen reading" width="300" />
      <br />
      <sub>Fullscreen reading</sub>
    </td>
  </tr>
</table>

******

### Installation and Usage

******

Before starting, confirm the following requirements:

```text
host app: AutoJs6 (org.autojs.autojs6)
minimum host build: 5269
minimum android: 7.0 (API 24)
plugin package: io.github.supermonster003.autojs6.plugin.markdownpreviewer
```

It takes 4 steps from installation to your first rendered document:

1. Download and install the plugin APK. The plugin has no launcher icon; after installation it is managed entirely by AutoJs6.
2. Open AutoJs6, enter the `Plugin center`, locate `Markdown Previewer`, and enable it.
3. In the AutoJs6 file manager, locate any Markdown file (such as `README.md`) and open its overflow menu.
4. Select `Markdown Previewer`. The document opens rendered in a dedicated viewer.

Inside the viewer, the top-right menu offers `Refresh`, `Previewer theme`, `Import custom CSS`, `Fullscreen mode`, and `Settings`, where `Settings` provides the `Start in fullscreen mode` switch. http/https links in the document open in the system browser, while heading anchor links jump within the viewer. Explorer Action v2 supports both the primary previewer button and the overflow menu for a single file, using temporary read grants for the document and its parent directory. AutoJs6 build 5269 or later is required.

******

### Supported Formats

******

The plugin recognizes the following filename extensions:

```text
md, markdown, mdown, mkd, mkdn, mdwn, mdtext, mdtxt, rmd, qmd
```

Files whose extension is not listed but whose MIME type is `text/markdown` or `text/x-markdown` can be viewed as well. A single document may be at most 8 MiB; oversized files produce a clear message instead of a truncated rendering.

******

### FAQ

******

**The file menu does not show `Markdown Previewer`?**

Check the following in order: the AutoJs6 version code is at least 5269 (version 6.8.0 or later qualifies); the plugin is enabled in the `Plugin center`; and the file extension is in the supported list. If any of the three fails, the menu action will not appear.

**Opening fails with `Cannot read the Markdown file`?**

Common causes: the file was moved, renamed, or deleted at the moment of opening; the file exceeds 8 MiB; or the call did not come from the AutoJs6 file manager. For security reasons the plugin rejects invocations from any other source.

**Why are images in the document not shown?**

The viewer loads only three kinds of images: local images referenced by relative paths within the document directory (including subdirectories), inline `data:` images, and public `https` images. Plain `http` images and private or reserved network addresses are blocked by the security policy.

**Can it display HTML files?**

No. This plugin focuses on Markdown. HTML viewing is provided by the separate HTML Previewer plugin; see the links below.

**How does custom CSS work?**

Choose `Import custom CSS` from the menu and pick a stylesheet no larger than 256 KiB. The theme switches to `Custom CSS` automatically. The stylesheet layers on top of the base styling, so you only write the rules you want to override. Choose `Clear custom CSS` to return to `GitHub (Auto)`.

**How does syntax highlighting work, and why are Mermaid and math formulas not rendered?**

Syntax highlighting is generated during Markdown rendering for recognized code languages and does not require JavaScript. Mermaid and math formulas still depend on client-side scripts, so they are not rendered because the viewer keeps JavaScript disabled.

******

### Security

******

The viewer is built on a deny-by-default principle. All of the following measures are always on and cannot be disabled:

- Rendered output is sanitized against an allowlist: scripts, forms, iframes, inline event handlers, and other dangerous content are removed, and JavaScript stays disabled throughout.
- The WebView runs with storage, cookies, form saving, and filesystem access turned off. The rendered result lives only in memory and is destroyed when the page closes.
- The plugin reads the selected file and its directory solely through temporary content URIs granted by the host. It receives no filesystem paths and requests no additional runtime permissions.
- CSP plus request interception restrict resource loading twice over: only built-in stylesheets, resources inside the document directory, `data:` images, and `https` images pass; every other request is rejected.
- Remote images are filtered against private and reserved addresses (anti-SSRF) and loaded with a no-referrer policy; external links can only open in the system browser.
- Inputs are bounded: Markdown up to 8 MiB, custom CSS up to 256 KiB, with display name and path lengths limited as well.
- HTTPS resources use verified public DNS addresses with every redirect checked; direct WebView networking is blocked and resource requests support GET and HEAD only

******

### Plugin Interface (for Developers)

******

The host discovers and invokes the plugin with the following identities:

```text
service action: org.autojs.plugin.EXPLORER_ACTION
execute action: org.autojs.plugin.EXPLORER_ACTION_EXECUTE
plugin id: markdown-previewer
engine: explorer-action
variant: default
required host build: 5269
```

Explorer Action v2 supports both the primary previewer button and the overflow menu for a single file, using temporary read grants for the document and its parent directory. AutoJs6 build 5269 or later is required.

******

### Roadmap

******

Completed capabilities and upcoming plans are maintained as a checkable list in ROADMAP.md. Unchecked items express intent and do not describe current abilities.

- [Open the checkable ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/ROADMAP.md)

******

### Release History

******

#### v1.2.1

###### 2026/09/15

* `Improvement` Raise compileSdk to 37 (Android 17); targetSdk stays at 36 until the behavior that depends on the target is verified

#### v1.2.0

###### 2026/09/13

* `Feature` Local release history is available from the interface, with localized text and an English fallback
* `Fix` HTTPS resources use verified public DNS addresses with every redirect checked; direct WebView networking is blocked and resource requests support GET and HEAD only
* `Fix` Preserve HTTP 206 and other successful status codes when loading remote resources
* `Improvement` Release packages are checked for a complete signing configuration, exact APK contents and reproducible documentation
* `Dependency` Add OkHttp 4.12.0 for controlled HTTPS resource loading

#### v1.1.0

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

##### Full history

* [CHANGELOG-en.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/assets/doc/CHANGELOG-en.md)

******

### Build

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release build:

```powershell
.\gradlew.bat :app:assembleRelease
```

Build parameters come from `version.properties`. The current minimum SDK is 24 and the target SDK is 36.

******

### Resource Layout

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG-*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` localizes plugin metadata and the viewer UI, while `plugin_instruction.md` provides the usage instructions shown by the host. All README and CHANGELOG files are generated from JSON sources by `.python/generate_markdown.py`: to change the docs, edit the `lang_*.json` files under `.readme` and `.changelog` and rerun the script instead of editing the generated Markdown files.

******

### Links

******

- AutoJs6 documentation: https://docs.autojs6.com
- CommonMark specification: https://commonmark.org
- HTML Previewer plugin (renders HTML files): https://github.com/SuperMonster003/AutoJs6-Plugin-HTML-Previewer


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/16kb.md)
