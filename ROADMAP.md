# Markdown Preview Roadmap

更新日期: 2026-08-31

本文档是 Markdown Preview 从单文件只读查看器逐步演进为更完整 Markdown 阅读方案的执行清单. 每个条目只有在代码/测试与可验证的验收条件同时满足后才可勾选.

## 状态与证据规则

- `[x]`: 已完成, 且本仓库存在可复核证据 (代码, 测试或生成产物).
- `[ ]`: 尚未完成; 括号中的 `插件` / `API` / `宿主` / `测试` / `发布` 表示主要落点.
- 未勾选条目属于规划意向, 不代表当前版本能力; 依赖宿主或协议的条目需等待对应仓库先行支持.

## 总览

| 里程碑 | 状态 | 核心结果 | 主要落点 |
|---|---|---|---|
| M0 基线 | 已完成 | 单文件只读预览与安全沙盒 | 插件 |
| M1 阅读体验 | 已完成 | 大纲, 页内查找, 字号, 语法高亮 | 插件 |
| M2 导航与输出 | 已完成 | 文档间跳转, 打印/PDF, YAML front matter | 插件 |
| M3 格式与协议 | 进行中 | CommonMark 0.30.0, Footnotes 与截图物料已完成; 协议 v2 等待宿主发布 | 插件/API/宿主/发布 |

依赖顺序:

```text
M0 ──> M1 ──> M2
        └───> M3 (协议条目需宿主先行)
```

## M0: 基线能力 (v1.0.x, 已完成)

- [x] (插件) 通过 `org.autojs.plugin.EXPLORER_ACTION` 注册单文件只读溢出菜单动作, 文件管理器中 `预览 Markdown` 可用.
- [x] (插件) `onBind` 无条件返回绑定对象, 兼容宿主不携带 action 的绑定方式, 插件中心不再出现 onNullBinding 启用失败.
- [x] (插件) 渲染管线: commonmark-java 搭配 Autolink / Tables / Strikethrough / HeadingAnchor 扩展, Jsoup 允许列表净化, 任务列表与表格包装后处理.
- [x] (插件) 安全沙盒: 禁用 JavaScript / 存储 / Cookie / 文件访问, CSP 与请求拦截, 私网地址过滤 (防 SSRF), 内存内渲染, Markdown 8 MiB 与 CSS 256 KiB 输入上限.
- [x] (插件) 5 种内置主题与自定义 CSS 导入/清除, 全屏模式与 `以全屏模式启动` 偏好, 手动刷新, BOM 编码识别.
- [x] (插件) 界面, 插件信息, 使用说明, README 与 CHANGELOG 覆盖 10 种语言.
- [x] (发布) README 与 CHANGELOG 采用 `.python/generate_markdown.py` 多语言生成方案, 内置全角符号, 源布局, 版本一致性与 strings.xml 描述一致性校验.

验收条件: 在版本代码不低于 5268 的 AutoJs6 中启用插件后, 对 10 种受支持扩展名的单个文件可稳定完成只读渲染, 上述任一安全开关均不可被文档内容绕过. (已满足)

## M1: 阅读体验增强 (纯插件, 无协议依赖, 已完成)

- [x] (插件) 文档大纲面板: 渲染期收集 H1-H6 与既有锚点 id, 在查看器中提供大纲入口, 点击跳转对应位置; 无标题文档隐藏入口.
- [x] (插件) 页内查找: 工具栏查找入口, 基于 WebView `findAllAsync` / `findNext` 实现匹配高亮与计数, 不引入 JavaScript.
- [x] (插件) 字号调节: `设置` 中提供持久化字号偏好 (WebSettings textZoom), 与双指缩放互不干扰.
- [x] (插件) 代码块语法高亮: 在渲染期 (Kotlin 侧) 完成 token 化并输出带 class 的 span, 保持 `script-src 'none'`, 高亮配色与浅色/深色主题联动.
- [x] (测试) 大纲提取与安全锚点生成具备 JVM 用例, 大纲跳转具备真实窗口 WebView instrumentation 用例.
- [x] (测试) 页内查找具备匹配计数与前后跳转 instrumentation 用例.
- [x] (测试) 字号调节具备偏好持久化与 WebSettings 应用 instrumentation 用例.
- [x] (测试) 语法高亮输出具备无脚本且通过净化的 JVM / instrumentation 用例.

验收条件: 打开含大量标题的长文档时大纲与查找可用且流畅; 应用重启后字号保持; 语法高亮不改变净化与 CSP 行为. (已满足)

## M2: 文档间导航与输出 (已完成)

- [x] (插件) 同目录 Markdown 相对链接跳转: 点击指向已授权父目录内受支持扩展名文件的相对链接时在查看器内打开并支持返回; `..` 仅可在授权根内归一化, 越界与未授权路径保持拒绝.
- [x] (插件) 打印与导出 PDF: 经 Android 打印框架输出当前渲染结果, 不在磁盘落地中间 HTML.
- [x] (插件) YAML front matter 识别: 文档头部 front matter 以可折叠, 本地化且带 YAML 高亮的元信息块展示而非当作正文渲染; 识别过程不反序列化不受信任 YAML, 并设 256 KiB / 4096 行预算, rmd / qmd 文件优先受益.
- [x] (测试) 链接跳转的路径策略用例覆盖越界, 百分号与双重编码绕过, 非法字符, 超长路径和 MIME/扩展名边界; FileProvider / ContentResolver / WebView 真机用例覆盖文档内跳转与返回.
- [x] (测试) 打印输出的净化行为与查看器一致; Android 9 / 12 / 15 真机生成多页 PDF, 并完成文本、链接、页面结构与渲染检查.
- [x] (测试) front matter 用例覆盖 BOM / CRLF, `---` / `...` 结束符, YAML 缩进块, JSON 风格 flow mapping, 普通水平线防误判, 控制字符, 超限输入与 HTML / 脚本净化; Android 9 / 10 / 12 / 13 / 15 / 16 WebView 验证通过.

验收条件: 内链文档可在查看器内往返浏览且权限校验全程生效; 打印/导出产物与屏幕渲染一致且不含被过滤内容. (已满足)

## M3: 格式, 协议与发布物料 (进行中)

- [x] (插件) 渲染引擎升级: 从 JitPack atlassian fork (commonmark 0.9.0) 迁移至 Maven Central 上游 `org.commonmark:0.30.0`; core 与 Autolink / Tables / Strikethrough / HeadingAnchor / Footnotes 使用独立官方模块, Footnotes 同时支持定义式与内联式语法.
- [x] (插件/安全) Footnotes 输出仅新增 `section` 标签及 `footnotes` / `footnote-ref` / `footnote-backref` 三个固定 class, 4 个已知 footnote data 属性按标签闭集放行并校验回链序号; 任意 class, data 属性, 事件与危险 URI 仍被剥离. 为上游 Java 11 API 启用 core library desugaring, 保持 API 24 最低版本兼容.
- [x] (测试) 依赖图与源码已无旧 Atlassian 坐标; JVM 53/53, Android 9 / 10 / 12 / 13 / 15 / 16 各 21/21 (合计 126/126), Lint 0 errors / 36 warnings, Release R8 + L8 均通过. v1.1.0 Release APK 为 2,568,596 bytes, SHA-256 `504548D857B6B386124E4188C1023FBE7FF507A5155B0D08B76C341FB7F13603`, APK Signature Scheme v2 与签名证书均验证通过. 根 JitPack 仓库因 CircularReveal / EasyWindow / OpenCC / Toaster 等既有依赖仍需保留, 但 CommonMark 已不再经过 JitPack.
- [x] (API/宿主审计) 截至 2026-08-31, vendored `explorer-action-api.aar` 的 `VERSION`, `MIN_SUPPORTED_VERSION`, `MAX_SUPPORTED_VERSION` 均为 `1`, 目标类型仅定义 `TARGET_FILE = 1`; AutoJs6 官方公开上游的 [最新 Release v6.7.0](https://github.com/SuperMonster003/AutoJs6/releases/tag/v6.7.0) 与 [`master/plugin-api`](https://github.com/SuperMonster003/AutoJs6/tree/master/plugin-api) 尚未发布 explorer-action v2. 因此继续固定 v1 兼容边界, 下述实现项保持未勾选.
- [ ] (API/宿主) 跟进 explorer-action 协议 v2 的多选与目录目标类型, 在保持 v1 兼容的前提下扩展动作目录.
- [x] (发布) 界面截图物料: `docs/images/screenshots` 提供 4 张 1096 x 2560 真实设备截图, 内容仅使用 `docs/fixtures` 合成文档, 覆盖文件菜单动作, 查看器, 主题对话框与全屏模式; 图片已接入 README 模板及 10 种语言生成链路.
- [x] (发布) 文档整改后的首个对外版本: `version.properties` 已升级至 v1.1.0 (versionCode 6), 10 种语言 CHANGELOG 已补齐, 签名 APK 已发布至 [GitHub Release v1.1.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/releases/tag/v1.1.0).

验收条件: 升级后的渲染引擎通过现有净化与安全测试全集 (已满足); 协议 v2 条目仅在宿主发布对应能力后开始实施; README 截图在 GitHub 深浅色模式下显示正常.

## 边界 (非目标)

- 不执行 JavaScript: Mermaid, MathJax 等依赖脚本的动态渲染不在任何里程碑内; 数学与图表如需支持将采用渲染期静态方案另行立项.
- 不提供编辑能力: 插件保持只读, 写操作超出 explorer-action 只读访问模式的设计边界.
- 不预览 HTML: HTML 由姊妹插件 [AutoJs6-Plugin-HTML-Preview](https://github.com/SuperMonster003/AutoJs6-Plugin-HTML-Preview) 负责, 本插件仅保留扩展名与 MIME 冲突检测.
- 不脱离宿主运行: 不添加桌面入口, 不接受第三方应用调用.
