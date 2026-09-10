在文件管理器中使用 Markdown Previewer:

1. 安装并启用 `Markdown Previewer` 插件.
2. 打开一个受支持 Markdown 文件的溢出菜单.
3. 选择 `预览 Markdown`.

插件通过 content URI 临时获得所选文件及其父目录的读取权限. 插件不会接收原始文件系统路径.

支持的扩展名: `md`, `markdown`, `mdown`, `mkd`, `mkdn`, `mdwn`, `mdtext`, `mdtxt`, `rmd`, `qmd`.

查看器支持自动链接, 表格, 删除线, 标题锚点, 任务列表, 文档内图片, 内置主题, 自定义 CSS, 刷新和全屏模式.

Explorer Action v2 同时支持单文件的主预览按钮和溢出菜单, 通过临时只读授权访问文档及其父目录. 需要 AutoJs6 构建 5269 或更高版本.
