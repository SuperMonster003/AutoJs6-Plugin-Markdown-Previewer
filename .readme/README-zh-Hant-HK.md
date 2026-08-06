<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-markdown-preview-ic-launcher" border="0" width="128" />
  </p>

  <p>用於 AutoJs6 檔案瀏覽器的安全 Markdown 預覽插件</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Markdown-Preview?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Markdown-Preview?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Markdown-Preview?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 語言 (Languages)

******

目前 README.md 支援以下語言:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-zh-Hans.md)
- 繁體中文 (香港) [zh-Hant-HK] # 目前
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-ar.md)

******

### 簡介

******

AutoJs6 Markdown Preview 插件為 AutoJs6 檔案瀏覽器加入單一檔案唯讀 Markdown 預覽動作. 插件在獨立檢視器中渲染內容, 無需將預覽實現嵌入宿主應用程式.

******

### 功能

******

- 透過共用的 `org.autojs.plugin.EXPLORER_ACTION` 協議註冊單一檔案唯讀檔案瀏覽器動作.
- 透過 content URI 臨時讀取所選 Markdown 檔案及其上層目錄資源, 不接收原始檔案系統路徑.
- 支援自動連結, 表格, 刪除線, 標題錨點, 工作清單和文件內圖片.
- 提供 GitHub 自動/淺色/深色, 紙張, 棕褐色和自訂 CSS 主題.
- 支援重新整理, 全螢幕模式, 啟動時全螢幕偏好和有界的 Markdown/CSS 輸入大小.

******

### 支援的格式

******

首個版本識別以下檔案副檔名:

```text
md, markdown, mdown, mkd, mkdn, mdwn, mdtext, mdtxt, rmd, qmd
```

******

### 插件介面

******

AutoJs6 使用以下識別資訊探索並執行插件:

```text
service action: org.autojs.plugin.EXPLORER_ACTION
execute action: org.autojs.plugin.EXPLORER_ACTION_EXECUTE
plugin id: markdown-preview
engine: explorer-action
variant: default
```

版本 1 僅支援 AutoJs6 主檔案瀏覽器中的單一檔案唯讀更多選單動作.

******

### 安全

******

檢視器按允許清單淨化渲染結果, 停用 JavaScript/WebView 儲存空間/Cookie 和直接檔案存取, 透過 CSP 與 URI 約束限制資源和導覽, 並只接受宿主授予的臨時讀取權限.

******

### 發行歷史

******

# v1.0.0

###### 2026/08/06

* `新增` Markdown Preview 插件, 插件 ID 為 `markdown-preview`, 引擎為 `explorer-action`, 變體為 `default`
* `新增` 透過 `org.autojs.plugin.EXPLORER_ACTION` 為 AutoJs6 主檔案瀏覽器提供單一檔案唯讀更多選單動作
* `新增` 透過 `org.autojs.plugin.EXPLORER_ACTION_EXECUTE` 接收檔案和上層目錄 content URI 的臨時讀取權限
* `新增` 支援自動連結/表格/刪除線/標題錨點/工作清單和文件內圖片的 Markdown 渲染
* `新增` 提供 GitHub 自動/淺色/深色/紙張/棕褐色/自訂 CSS 主題, 以及重新整理和全螢幕控制
* `新增` 透過允許清單淨化/CSP/受控 URI 導覽/停用 JavaScript 和儲存空間/有界輸入強化 WebView 安全
* `新增` 插件資訊/介面文字/使用說明/README/changelog 支援西班牙語/法語/俄語/阿拉伯語/日語/韓語/英語/簡體中文/香港繁體/台灣繁體

##### 更多發行歷史可參閱

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hant-HK.md)

******

### 構建

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release 構建:

```powershell
.\gradlew.bat :app:assembleRelease
```

構建參數來自 `version.properties`, 目前最低 SDK 為 24, 目標 SDK 為 36.

******

### 資源結構

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG-*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` 提供插件資訊和檢視器介面的本地化, `plugin_instruction.md` 提供宿主側展示的使用說明. README 與 changelog 檔案由 `.python/generate_markdown.py` 根據 JSON 源檔案生成.

******

### 相關連結

******

- AutoJs6 文件: https://docs.autojs6.com
- CommonMark: https://commonmark.org
