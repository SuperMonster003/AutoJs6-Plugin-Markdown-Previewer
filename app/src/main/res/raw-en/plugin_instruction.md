Use Markdown Previewer from the file manager:

1. Install and enable the `Markdown Previewer` plugin.
2. Open the overflow menu for one supported Markdown file.
3. Select `Markdown Previewer`.

The plugin receives temporary read access to the selected file and its parent directory through content URIs. It does not receive a raw filesystem path.

Supported extensions: `md`, `markdown`, `mdown`, `mkd`, `mkdn`, `mdwn`, `mdtext`, `mdtxt`, `rmd`, `qmd`.

The viewer supports automatic links, tables, strikethrough, heading anchors, task lists, in-document images, built-in themes, custom CSS, refresh, and fullscreen mode.

Explorer Action v2 supports both the primary previewer button and the overflow menu for a single file, using temporary read grants for the document and its parent directory. AutoJs6 build 5269 or later is required.

Menus and dialogs follow the AutoJs6 language and dark mode. GitHub (Auto), or the automatic HTML theme, follows AutoJs6 as well. The toolbar and system bars use one representative color sampled from the rendered page edges, with contrasting black or white text and icons. For gradients, images, and animation, that color stays fixed until reload to avoid flicker.
