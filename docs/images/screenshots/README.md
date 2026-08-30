# Screenshot sources

These release assets are unedited screenshots captured from a physical Android device. They contain synthetic project fixtures only and are safe to reuse in every generated README language.

## Capture environment

- Device: physical Android 12 device at 1096 x 2560 pixels.
- Locale: `en-US`.
- Host: AutoJs6 6.8.0, build 5276.
- Plugin: Markdown Preview 1.0.1, version code 5.
- Viewer theme: GitHub (Auto) with the device in dark mode.
- Capture method: Android `screencap` PNG output. No compositing, cropping, AI generation, or post-processing was applied.

## Synthetic inputs

- [`markdown-showcase.md`](../../fixtures/markdown-showcase.md) supplies the viewer, theme dialog, and fullscreen content.
- [`release-notes.md`](../../fixtures/release-notes.md) supplies the second file shown behind the host menu.

## Asset map

| File | Screen state |
|---|---|
| `file-menu-action.png` | AutoJs6 file overflow menu with the `Markdown Preview: Preview Markdown` action visible. |
| `viewer.png` | Rendered document at its initial scroll position. |
| `theme-dialog.png` | Five-theme picker over the rendered document. |
| `fullscreen.png` | Immersive viewer with system bars and third-party overlays hidden. |

When refreshing these assets, keep the filenames and pixel dimensions stable, use only the fixtures above, inspect every image for unrelated device content, and rerun `.python/generate_markdown.py` to verify all README outputs.
