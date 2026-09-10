在檔案管理器中使用 Markdown Previewer:

1. 安裝並啟用 `Markdown Previewer` 插件.
2. 開啟一個受支援 Markdown 檔案的更多選單.
3. 選擇 `預覽 Markdown`.

插件透過 content URI 臨時取得所選檔案及其上層目錄的讀取權限. 插件不會接收原始檔案系統路徑.

支援的副檔名: `md`, `markdown`, `mdown`, `mkd`, `mkdn`, `mdwn`, `mdtext`, `mdtxt`, `rmd`, `qmd`.

檢視器支援自動連結, 表格, 刪除線, 標題錨點, 工作清單, 文件內圖片, 內置主題, 自訂 CSS, 重新整理和全螢幕模式.

版本 1 僅支援檔案管理器中的單一檔案唯讀動作.

Explorer Action v2 同時支援單檔案的主預覽按鈕和溢出選單, 透過臨時唯讀授權存取文件及其父目錄. 需要 AutoJs6 組建 5269 或更新版本.

選單和對話框跟隨 AutoJs6 的語言與深色模式. GitHub (Auto) 和 HTML 自動主題也跟隨 AutoJs6. 工具列及系統列使用渲染頁面邊緣的代表色, 配合有對比度的黑色或白色文字與圖示. 漸變, 圖片和動畫背景取色後保持穩定, 重新載入時更新, 避免閃爍.
