<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="markdown-previewer-ic-launcher" border="0" width="128" />
  </p>

  <p>文件管理器插件. 安全只读预览 Markdown 文件</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 语言 (Languages)

******

当前 README.md 支持以下语言:

- 简体中文 [zh-Hans] # 当前
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ar.md)

******

### 简介

******

Markdown Previewer 是 AutoJs6 文件管理器的预览插件. 启用后, 文件管理器中每个 Markdown 文件的溢出菜单都会出现 `预览 Markdown` 动作, 点击即可像阅读网页一样查看排版后的文档, 而不再是一整片源码文本.

插件只做一件事并把它做稳: 只读渲染. 查看器不执行任何脚本, 只能读取宿主临时授权的文件, 渲染在插件的独立界面中完成, 不改动 AutoJs6 本体, 也不影响脚本运行环境.

******

### 功能亮点

******

- 在 AutoJs6 文件管理器内直接阅读排版后的 Markdown 文档, 无需导出文件或安装第三方阅读器.
- 支持表格, 任务列表, 删除线, 自动链接, 标题锚点与文档内图片, 常见 GitHub 风格写法开箱即用.
- 内置 GitHub (自动), GitHub 浅色, GitHub 深色, 纸张, 棕褐色共 5 种预览主题, 自动模式跟随系统深浅色切换.
- 支持导入自定义 CSS 打造个人阅读样式, 样式叠加在内置排版之上, 可随时一键清除还原.
- 全屏模式沉浸阅读, 可开启 `以全屏模式启动`, 按返回键优先退出全屏而不会误关页面.
- 支持双指缩放, 手动刷新与表格横向滚动, 并按 BOM 自动识别 UTF-8 / UTF-16 / UTF-32 编码.
- 只读安全沙盒: 不执行 JavaScript, 不写入磁盘缓存, 除宿主授权的文件外不访问任何数据.

******

### 界面截图

******

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/file-menu-action.png?raw=true" alt="文件菜单动作" width="300" />
      <br />
      <sub>文件菜单动作</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/viewer.png?raw=true" alt="文档查看器" width="300" />
      <br />
      <sub>文档查看器</sub>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/theme-dialog.png?raw=true" alt="主题选择" width="300" />
      <br />
      <sub>主题选择</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/fullscreen.png?raw=true" alt="全屏阅读" width="300" />
      <br />
      <sub>全屏阅读</sub>
    </td>
  </tr>
</table>

******

### 安装与使用

******

开始前请确认以下环境要求:

```text
host app: AutoJs6 (org.autojs.autojs6)
minimum host build: 5269
minimum android: 7.0 (API 24)
plugin package: io.github.supermonster003.autojs6.plugin.markdownpreviewer
```

从安装到打开第一篇文档共 4 步:

1. 下载并安装本插件 APK. 插件没有独立桌面图标, 安装后统一由 AutoJs6 管理.
2. 打开 AutoJs6, 进入 `插件中心`, 找到 `Markdown 预览` 并启用.
3. 在 AutoJs6 文件管理器中定位任意 Markdown 文件 (如 `README.md`), 展开该文件的溢出菜单.
4. 点选 `预览 Markdown`, 文档随即在独立查看器中渲染打开.

进入查看器后, 右上角菜单提供 `刷新`, `预览主题`, `导入自定义 CSS`, `全屏模式` 与 `设置` 等操作, 其中 `设置` 可开启 `以全屏模式启动`. 文档内的 http/https 链接会交由系统浏览器打开, 标题锚点链接则在查看器内跳转. Explorer Action v2 同时支持单文件的主预览按钮和溢出菜单, 通过临时只读授权访问文档及其父目录. 需要 AutoJs6 构建 5269 或更高版本.

******

### 支持的格式

******

插件识别以下文件扩展名:

```text
md, markdown, mdown, mkd, mkdn, mdwn, mdtext, mdtxt, rmd, qmd
```

扩展名不在列表中但 MIME 类型为 `text/markdown` 或 `text/x-markdown` 的文件同样可以预览. 单个文档的预览上限为 8 MiB, 超限时会明确提示而不是截断渲染.

******

### 常见问题

******

**文件的菜单里没有出现 `预览 Markdown`?**

请依次检查: AutoJs6 版本代码是否不低于 5269 (6.8.0 及以上版本满足); 插件是否已在 `插件中心` 启用; 文件扩展名是否在支持列表中. 三者任一不满足, 菜单动作都不会出现.

**打开时提示 `无法读取 Markdown 文件`?**

常见原因: 文件在打开瞬间被移动, 重命名或删除; 文件大小超过 8 MiB; 或调用并非来自 AutoJs6 文件管理器. 出于安全考虑, 插件会拒绝其他来源的调用.

**文档里的图片为什么不显示?**

查看器仅加载三类图片: 相对路径指向文档所在目录 (含子目录) 的本地图片, `data:` 内联图片, 以及公网 `https` 图片. 明文 `http` 图片与内网, 保留地址会被安全策略拦截.

**能用它预览 HTML 文件吗?**

不能. 本插件专注 Markdown, HTML 预览由独立的 HTML Previewer 插件提供, 见下方相关链接.

**自定义 CSS 是如何生效的?**

在菜单中选择 `导入自定义 CSS` 并挑选一个不超过 256 KiB 的样式文件后, 主题自动切换为 `自定义 CSS`. 样式叠加在基础排版之上, 只需编写想覆盖的规则; 选择 `清除自定义 CSS` 即可恢复为 `GitHub (自动)`.

**语法高亮如何实现, 为什么 Mermaid 与数学公式仍不渲染?**

可识别语言的代码块会在 Markdown 渲染期生成静态语法高亮, 不需要 JavaScript. Mermaid 与数学公式仍依赖客户端脚本, 查看器保持禁用 JavaScript, 因此不会渲染这些内容.

******

### 安全

******

查看器按默认拒绝原则构建, 以下措施全部默认开启且无法关闭:

- 渲染结果经允许列表净化: 脚本, 表单, iframe, 内联事件等危险内容一律移除, JavaScript 全程禁用.
- WebView 关闭存储, Cookie, 表单保存与文件系统访问, 渲染结果仅存于内存, 页面关闭即销毁.
- 仅凭宿主授予的临时 content URI 读取所选文件及其所在目录, 不接收文件系统路径, 也不申请额外运行时权限.
- CSP 与请求拦截双重限制资源加载: 仅放行内置样式, 文档目录内资源, `data:` 与 `https` 图片, 其余请求一律拒绝.
- 远程图片经私网与保留地址过滤 (防 SSRF), 以 no-referrer 策略加载; 外部链接仅能交由系统浏览器打开.
- 输入有界: Markdown 上限 8 MiB, 自定义 CSS 上限 256 KiB, 显示名与路径长度同样受限.

******

### 插件接口 (面向开发者)

******

宿主通过以下标识发现并调用插件:

```text
service action: org.autojs.plugin.EXPLORER_ACTION
execute action: org.autojs.plugin.EXPLORER_ACTION_EXECUTE
plugin id: markdown-previewer
engine: explorer-action
variant: default
required host build: 5269
```

Explorer Action v2 同时支持单文件的主预览按钮和溢出菜单, 通过临时只读授权访问文档及其父目录. 需要 AutoJs6 构建 5269 或更高版本.

******

### 开发路线图

******

已完成能力与后续计划以可勾选清单维护在 ROADMAP.md 中. 未勾选条目表示规划意向, 不代表当前版本能力.

- [查看可勾选的 ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/ROADMAP.md)

******

### 版本记录

******

#### v1.1.0

###### 2026/08/31

* `新增` 新增文档大纲, 页内查找, 持久化字号调节与渲染期语法高亮, 全程不启用 JavaScript
* `新增` 新增宿主授权目录内的相对 Markdown 文档安全跳转, 支持查看器内历史返回与锚点处理
* `新增` 新增 Android 打印 / PDF 导出与有界 YAML front matter 展示
* `新增` 新增定义式与内联式 Footnotes, 并提供净化后的双向回链
* `优化` 强化 explorer-action v1 Intent, URI, 路径, 链接, 资源, HTML 与 Footnotes 校验, 保持单文件只读边界
* `优化` 新增可勾选 Roadmap, 4 张仅含合成数据的真实设备截图, 以及覆盖 10 种语言的可复现 README / CHANGELOG 生成链路
* `依赖` 从旧 Atlassian 0.9.0 fork 迁移至 Maven Central 官方 CommonMark 0.30.0 core 与扩展模块, 并通过 core library desugaring 保持 API 24 兼容

#### v1.0.1

###### 2026/08/08

* `修复` 在 AutoJs6 插件中心启用插件时因服务返回空绑定 (onNullBinding) 而无法启用的问题
* `优化` 精简插件名称与描述, 统一各语言用户文档的表述

#### v1.0.0

###### 2026/08/06

* `新增` Markdown Previewer 首个版本: 为 AutoJs6 文件管理器提供 `预览 Markdown` 溢出菜单动作, 以只读方式渲染单个文档
* `新增` 识别 md / markdown / mdown / mkd / mkdn / mdwn / mdtext / mdtxt / rmd / qmd 共 10 种扩展名以及 `text/markdown` 与 `text/x-markdown` MIME 类型
* `新增` 支持表格, 任务列表, 删除线, 自动链接, 标题锚点与文档内图片渲染
* `新增` 内置 GitHub (自动 / 浅色 / 深色), 纸张与棕褐色主题, 支持导入自定义 CSS, 手动刷新与全屏模式
* `新增` 以允许列表净化, CSP 约束, 禁用 JavaScript 与存储, 私网地址过滤及输入上限 (Markdown 8 MiB, CSS 256 KiB) 构建只读安全沙盒
* `新增` 基于 `org.autojs.plugin.EXPLORER_ACTION` 协议注册插件服务, 经宿主临时 content URI 授权访问所选文件及其父目录
* `新增` 插件信息, 界面, 使用说明与文档支持简体中文, 繁体中文 (香港 / 台湾), 英语, 法语, 西班牙语, 日语, 韩语, 俄语与阿拉伯语

##### 完整记录

* [CHANGELOG-zh-Hans.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hans.md)

******

### 构建

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release 构建:

```powershell
.\gradlew.bat :app:assembleRelease
```

构建参数来自 `version.properties`, 当前最低 SDK 为 24, 目标 SDK 为 36.

******

### 资源结构

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG-*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` 提供插件信息与查看器界面的本地化, `plugin_instruction.md` 提供宿主侧展示的使用说明. 全部 README 与 CHANGELOG 由 `.python/generate_markdown.py` 依据 JSON 源生成: 修改文档时请编辑 `.readme` 与 `.changelog` 下的 `lang_*.json` 并重新运行脚本, 不要直接编辑生成的 Markdown 文件.

******

### 相关链接

******

- AutoJs6 文档: https://docs.autojs6.com
- CommonMark 规范: https://commonmark.org
- HTML Previewer 插件 (预览 HTML 文件): https://github.com/SuperMonster003/AutoJs6-Plugin-HTML-Previewer
