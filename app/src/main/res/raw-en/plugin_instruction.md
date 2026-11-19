Markdown Previewer shows Markdown documents in AutoJs6 as formatted pages, with tables, task lists, footnotes and code highlighting, plus a document outline, find in page, several preview themes and PDF export. Previewing only reads the file and never changes the original.

### Getting started

1. Make sure this plugin is enabled in AutoJs6 "Plugin Center".
2. Open the AutoJs6 file manager and locate the Markdown file.
3. Choose "Preview Markdown" from the file menu (in AutoJs6 6.8.1 and later it is under the top-level menu "Markdown Previewer").
4. Use the menu at the top right of the preview for find, outline, themes, PDF export, "Settings" and more.

### Main features

- **Find in page**: highlights the matches of the text you type and lets you jump between them.
- **Document outline**: lists the headings of the document by level; tap one to jump to it.
- **Previewer theme**: in "Previewer theme", choose "GitHub (Auto)", "GitHub Light", "GitHub Dark", "Paper" or "Sepia"; "GitHub (Auto)" follows the AutoJs6 dark mode.
- **Custom CSS**: after you pick a style sheet of up to 256 KiB with "Import custom CSS", the preview switches to the "Custom CSS" theme; choose "Clear custom CSS" to return to "GitHub (Auto)".
- **Print and PDF export**: "Print / Export PDF" opens the system print screen, where you can print or save as PDF; the plugin needs no storage permission.
- **Extended syntax**: tables, strikethrough, task lists, footnotes, autolinks and code highlighting are supported; YAML metadata at the start of a document is shown in an expandable "YAML metadata" block.
- **Links and images**: links to Markdown files in the same folder or its subfolders open in the previewer, and web links open in the browser; images from the document folder and HTTPS images are shown.
- **Font size and fullscreen**: adjust "Font size" in "Settings" and optionally turn on "Start in fullscreen mode"; "Fullscreen mode" hides the toolbar and system bars, and the Back key leaves it.

### Requirements

- Android 7.0 or later.
- AutoJs6 6.8.0 or later.
- No storage permission is needed; the plugin only reads the selected file and its folder, and uses the network only to load HTTPS images in the document.

### FAQ

**Why "Preview Markdown" is missing from the file menu**

The plugin only handles files with the extensions md, markdown, mdown, mkd, mkdn, mdwn, mdtext, mdtxt, rmd, qmd. Check the file extension, and make sure the plugin is enabled in "Plugin Center".

**Why some images are missing**

Local images must be in the folder of the Markdown file or one of its subfolders. Network images must use public HTTPS addresses; HTTP and local network addresses are not loaded.

**Why a file is reported as over the preview limit**

Markdown files larger than 8 MiB are not previewed. Split the document into a few smaller files and link them to each other.

**How to preview HTML files**

This plugin previews Markdown documents only. To preview HTML and MHTML files, use the [HTML Previewer](https://github.com/SuperMonster003/AutoJs6-Plugin-HTML-Previewer) plugin.

See the [project homepage](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer) for more details.
