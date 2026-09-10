<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="markdown-previewer-ic-launcher" border="0" width="128" />
  </p>

  <p>檔案管理器外掛程式. 安全唯讀預覽 Markdown 檔案</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 語言 (Languages)

******

目前 README.md 支援以下語言:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hans.md)
- 繁體中文 (香港) [zh-Hant-HK] # 目前
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ar.md)

******

### 簡介

******

Markdown Previewer 是 AutoJs6 檔案管理器的預覽插件. 啟用後, 檔案管理器中每個 Markdown 檔案的更多選單都會出現 `預覽 Markdown` 動作, 點擊即可像閱讀網頁一樣查看排版後的文件, 而不再是一整片原始碼文字.

插件只做一件事並把它做穩: 唯讀渲染. 檢視器不執行任何指令碼, 只能讀取宿主臨時授權的檔案, 渲染在插件的獨立介面中完成, 不改動 AutoJs6 本體, 也不影響指令碼執行環境.

******

### 功能亮點

******

- 在 AutoJs6 檔案管理器內直接閱讀排版後的 Markdown 文件, 無需匯出檔案或安裝第三方閱讀器.
- 支援表格, 工作清單, 刪除線, 自動連結, 標題錨點與文件內圖片, 常見 GitHub 風格寫法開箱即用.
- GitHub (Auto), GitHub Light, GitHub Dark, Paper, Sepia. 選單和對話框跟隨 AutoJs6 的語言與深色模式. GitHub (Auto) 和 HTML 自動主題也跟隨 AutoJs6. 工具列及系統列使用渲染頁面邊緣的代表色, 配合有對比度的黑色或白色文字與圖示. 漸變, 圖片和動畫背景取色後保持穩定, 重新載入時更新, 避免閃爍.
- 支援匯入自訂 CSS 打造個人閱讀樣式, 樣式疊加在內置排版之上, 可隨時一鍵清除還原.
- 全螢幕模式沉浸閱讀, 可開啟 `以全螢幕模式啟動`, 按返回鍵優先退出全螢幕而不會誤關頁面.
- 支援雙指縮放, 手動重新整理與表格橫向捲動, 並按 BOM 自動識別 UTF-8 / UTF-16 / UTF-32 編碼.
- 唯讀安全沙盒: 不執行 JavaScript, 不寫入磁碟快取, 除宿主授權的檔案外不存取任何資料.

******

### 介面截圖

******

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/file-menu-action.png?raw=true" alt="檔案選單動作" width="300" />
      <br />
      <sub>檔案選單動作</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/viewer.png?raw=true" alt="文件檢視器" width="300" />
      <br />
      <sub>文件檢視器</sub>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/theme-dialog.png?raw=true" alt="主題選擇" width="300" />
      <br />
      <sub>主題選擇</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/fullscreen.png?raw=true" alt="全螢幕閱讀" width="300" />
      <br />
      <sub>全螢幕閱讀</sub>
    </td>
  </tr>
</table>

******

### 安裝與使用

******

開始前請確認以下環境要求:

```text
host app: AutoJs6 (org.autojs.autojs6)
minimum host build: 5269
minimum android: 7.0 (API 24)
plugin package: io.github.supermonster003.autojs6.plugin.markdownpreviewer
```

從安裝到開啟第一篇文件共 4 步:

1. 下載並安裝本插件 APK. 插件沒有獨立桌面圖示, 安裝後統一由 AutoJs6 管理.
2. 開啟 AutoJs6, 進入 `插件中心`, 找到 `Markdown 預覽` 並啟用.
3. 在 AutoJs6 檔案管理器中定位任意 Markdown 檔案 (如 `README.md`), 展開該檔案的更多選單.
4. 點選 `預覽 Markdown`, 文件隨即在獨立檢視器中渲染開啟.

進入檢視器後, 右上角選單提供 `重新整理`, `預覽主題`, `匯入自訂 CSS`, `全螢幕模式` 與 `設定` 等操作, 其中 `設定` 可開啟 `以全螢幕模式啟動`. 文件內的 http/https 連結會交由系統瀏覽器開啟, 標題錨點連結則在檢視器內跳轉. Explorer Action v2 同時支援單檔案的主預覽按鈕和溢出選單, 透過臨時唯讀授權存取文件及其父目錄. 需要 AutoJs6 組建 5269 或更新版本.

******

### 支援的格式

******

插件識別以下副檔名:

```text
md, markdown, mdown, mkd, mkdn, mdwn, mdtext, mdtxt, rmd, qmd
```

副檔名不在清單中但 MIME 類型為 `text/markdown` 或 `text/x-markdown` 的檔案同樣可以預覽. 單一文件的預覽上限為 8 MiB, 超限時會明確提示而不是截斷渲染.

******

### 常見問題

******

**檔案的選單裡沒有出現 `預覽 Markdown`?**

請依次檢查: AutoJs6 版本代碼是否不低於 5269 (6.8.0 及以上版本滿足); 插件是否已在 `插件中心` 啟用; 副檔名是否在支援清單中. 三者任一不滿足, 選單動作都不會出現.

**開啟時提示 `無法讀取 Markdown 檔案`?**

常見原因: 檔案在開啟瞬間被移動, 重新命名或刪除; 檔案大小超過 8 MiB; 或呼叫並非來自 AutoJs6 檔案管理器. 出於安全考慮, 插件會拒絕其他來源的呼叫.

**文件裡的圖片為什麼不顯示?**

檢視器僅載入三類圖片: 相對路徑指向文件所在目錄 (含子目錄) 的本地圖片, `data:` 內嵌圖片, 以及公網 `https` 圖片. 明文 `http` 圖片與內部網絡, 保留位址會被安全策略攔截.

**能用它預覽 HTML 檔案嗎?**

不能. 本插件專注 Markdown, HTML 預覽由獨立的 HTML Previewer 插件提供, 見下方相關連結.

**自訂 CSS 是如何生效的?**

在選單中選擇 `匯入自訂 CSS` 並挑選一個不超過 256 KiB 的樣式檔案後, 主題自動切換為 `自訂 CSS`. 樣式疊加在基礎排版之上, 只需編寫想覆蓋的規則; 選擇 `清除自訂 CSS` 即可恢復為 `GitHub (自動)`.

**語法突顯如何實現, 為甚麼 Mermaid 與數學公式仍不渲染?**

可識別語言的程式碼區塊會在 Markdown 渲染階段產生靜態語法突顯, 不需要 JavaScript. Mermaid 與數學公式仍依賴用戶端指令碼, 檢視器保持停用 JavaScript, 因此不會渲染這些內容.

******

### 安全

******

檢視器按預設拒絕原則構建, 以下措施全部預設開啟且無法關閉:

- 渲染結果經允許清單淨化: 指令碼, 表單, iframe, 內嵌事件等危險內容一律移除, JavaScript 全程停用.
- WebView 關閉儲存, Cookie, 表單儲存與檔案系統存取, 渲染結果僅存於記憶體, 頁面關閉即銷毀.
- 僅憑宿主授予的臨時 content URI 讀取所選檔案及其所在目錄, 不接收檔案系統路徑, 也不申請額外執行階段權限.
- CSP 與請求攔截雙重限制資源載入: 僅放行內置樣式, 文件目錄內資源, `data:` 與 `https` 圖片, 其餘請求一律拒絕.
- 遠端圖片經內部網絡與保留位址過濾 (防 SSRF), 以 no-referrer 策略載入; 外部連結僅能交由系統瀏覽器開啟.
- 輸入有界: Markdown 上限 8 MiB, 自訂 CSS 上限 256 KiB, 顯示名稱與路徑長度同樣受限.

******

### 插件介面 (面向開發者)

******

宿主透過以下標識發現並呼叫插件:

```text
service action: org.autojs.plugin.EXPLORER_ACTION
execute action: org.autojs.plugin.EXPLORER_ACTION_EXECUTE
plugin id: markdown-previewer
engine: explorer-action
variant: default
required host build: 5269
```

Explorer Action v2 同時支援單檔案的主預覽按鈕和溢出選單, 透過臨時唯讀授權存取文件及其父目錄. 需要 AutoJs6 組建 5269 或更新版本.

******

### 開發路線圖

******

已完成能力與後續計劃以可勾選清單維護在 ROADMAP.md 中. 未勾選條目表示規劃意向, 不代表目前版本能力.

- [查看可勾選的 ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/ROADMAP.md)

******

### 版本記錄

******

#### v1.1.0

###### 2026/08/31

* `新增` 新增文件大綱, 頁內尋找, 持久化字體大小調整與渲染階段語法高亮, 全程不啟用 JavaScript
* `新增` 新增宿主授權目錄內的相對 Markdown 文件安全跳轉, 支援檢視器內歷史返回與錨點處理
* `新增` 新增 Android 列印 / PDF 匯出與有界 YAML front matter 顯示
* `新增` 新增定義式與內聯式 Footnotes, 並提供淨化後的雙向返回連結
* `修復` 修復主預覽按鈕協議拒絕和設定崩潰; 同步宿主個人化配置, 頁面列配色及對話框黑白控制項
* `優化` 強化 explorer-action v1 Intent, URI, 路徑, 連結, 資源, HTML 與 Footnotes 驗證, 保持單一檔案唯讀邊界
* `優化` 新增可勾選 Roadmap, 4 張只含合成資料的實體裝置截圖, 以及涵蓋 10 種語言的可重現 README / CHANGELOG 生成流程
* `依賴` 從舊 Atlassian 0.9.0 fork 遷移至 Maven Central 官方 CommonMark 0.30.0 core 與擴展模組, 並透過 core library desugaring 保持 API 24 相容

#### v1.0.1

###### 2026/08/08

* `修復` 在 AutoJs6 插件中心啟用插件時因服務回傳空繫結 (onNullBinding) 而無法啟用的問題
* `優化` 精簡插件名稱與描述, 統一各語言使用者文件的表述

#### v1.0.0

###### 2026/08/06

* `新增` Markdown Previewer 首個版本: 為 AutoJs6 檔案管理器提供 `預覽 Markdown` 選單動作, 以唯讀方式渲染單一文件
* `新增` 識別 md / markdown / mdown / mkd / mkdn / mdwn / mdtext / mdtxt / rmd / qmd 共 10 種副檔名以及 `text/markdown` 與 `text/x-markdown` MIME 類型
* `新增` 支援表格, 工作清單, 刪除線, 自動連結, 標題錨點與文件內圖片渲染
* `新增` 內置 GitHub (自動 / 淺色 / 深色), 紙張與棕褐色主題, 支援匯入自訂 CSS, 手動重新整理與全螢幕模式
* `新增` 以允許清單淨化, CSP 約束, 停用 JavaScript 與儲存, 內部網絡位址過濾及輸入上限 (Markdown 8 MiB, CSS 256 KiB) 構建唯讀安全沙盒
* `新增` 基於 `org.autojs.plugin.EXPLORER_ACTION` 協定註冊插件服務, 經宿主臨時 content URI 授權存取所選檔案及其上層目錄
* `新增` 插件資訊, 介面, 使用說明與文件支援簡體中文, 繁體中文 (香港 / 台灣), 英文, 法文, 西班牙文, 日文, 韓文, 俄文與阿拉伯文

##### 完整記錄

* [CHANGELOG-zh-Hant-HK.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hant-HK.md)

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

`strings.xml` 提供插件資訊與檢視器介面的本地化, `plugin_instruction.md` 提供宿主側展示的使用說明. 全部 README 與 CHANGELOG 由 `.python/generate_markdown.py` 依據 JSON 源生成: 修改文件時請編輯 `.readme` 與 `.changelog` 下的 `lang_*.json` 並重新執行指令碼, 不要直接編輯生成的 Markdown 檔案.

******

### 相關連結

******

- AutoJs6 文件: https://docs.autojs6.com
- CommonMark 規範: https://commonmark.org
- HTML Previewer 插件 (預覽 HTML 檔案): https://github.com/SuperMonster003/AutoJs6-Plugin-HTML-Previewer
