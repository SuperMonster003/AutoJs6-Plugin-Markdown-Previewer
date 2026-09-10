在文件管理器中使用 Markdown Previewer:

1. 安装并启用 `Markdown Previewer` 插件.
2. 打开一个受支持 Markdown 文件的溢出菜单.
3. 选择 `预览 Markdown`.

插件通过 content URI 临时获得所选文件及其父目录的读取权限. 插件不会接收原始文件系统路径.

支持的扩展名: `md`, `markdown`, `mdown`, `mkd`, `mkdn`, `mdwn`, `mdtext`, `mdtxt`, `rmd`, `qmd`.

查看器支持自动链接, 表格, 删除线, 标题锚点, 任务列表, 文档内图片, 内置主题, 自定义 CSS, 刷新和全屏模式.

版本 1 仅支持文件管理器中的单文件只读动作.

Explorer Action v2 同时支持单文件的主预览按钮和溢出菜单, 通过临时只读授权访问文档及其父目录. 需要 AutoJs6 构建 5269 或更高版本.

菜单和对话框跟随 AutoJs6 的语言与暗色模式. GitHub (Auto) 和 HTML 自动主题也跟随 AutoJs6. 工具栏及系统栏使用渲染页面边缘的代表色, 配合有对比度的黑色或白色文字与图标. 渐变, 图片和动画背景取色后保持稳定, 重新加载时更新, 避免闪烁.
