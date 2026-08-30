# 版本記錄

## v1.1.0

###### 2026/08/31

* `新增` 新增文件大綱, 頁內尋找, 持久化字體大小調整與渲染階段語法高亮, 全程不啟用 JavaScript
* `新增` 新增宿主授權目錄內的相對 Markdown 文件安全跳轉, 支援檢視器內歷史返回與錨點處理
* `新增` 新增 Android 列印 / PDF 匯出與有界 YAML front matter 顯示
* `新增` 新增定義式與內聯式 Footnotes, 並提供淨化後的雙向返回連結
* `優化` 強化 explorer-action v1 Intent, URI, 路徑, 連結, 資源, HTML 與 Footnotes 驗證, 保持單一檔案唯讀邊界
* `優化` 新增可勾選 Roadmap, 4 張只含合成資料的實體裝置截圖, 以及涵蓋 10 種語言的可重現 README / CHANGELOG 生成流程
* `相依性` 從舊 Atlassian 0.9.0 fork 遷移至 Maven Central 官方 CommonMark 0.30.0 core 與擴展模組, 並透過 core library desugaring 保持 API 24 相容

## v1.0.1

###### 2026/08/08

* `修復` 在 AutoJs6 外掛中心啟用外掛時因服務回傳空繫結 (onNullBinding) 而無法啟用的問題
* `優化` 精簡外掛名稱與描述, 統一各語言使用者文件的表述

## v1.0.0

###### 2026/08/06

* `新增` Markdown Preview 首個版本: 為 AutoJs6 檔案管理器提供 `預覽 Markdown` 選單動作, 以唯讀方式轉譯單一文件
* `新增` 識別 md / markdown / mdown / mkd / mkdn / mdwn / mdtext / mdtxt / rmd / qmd 共 10 種副檔名以及 `text/markdown` 與 `text/x-markdown` MIME 類型
* `新增` 支援表格, 工作清單, 刪除線, 自動連結, 標題錨點與文件內圖片轉譯
* `新增` 內建 GitHub (自動 / 淺色 / 深色), 紙張與棕褐色主題, 支援匯入自訂 CSS, 手動重新整理與全螢幕模式
* `新增` 以允許清單淨化, CSP 約束, 停用 JavaScript 與儲存, 內部網路位址過濾及輸入上限 (Markdown 8 MiB, CSS 256 KiB) 建置唯讀安全沙箱
* `新增` 基於 `org.autojs.plugin.EXPLORER_ACTION` 協定註冊外掛服務, 經宿主暫時 content URI 授權存取所選檔案及其上層目錄
* `新增` 外掛資訊, 介面, 使用說明與文件支援簡體中文, 繁體中文 (香港 / 台灣), 英文, 法文, 西班牙文, 日文, 韓文, 俄文與阿拉伯文
