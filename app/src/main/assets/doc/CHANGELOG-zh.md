# 版本记录

## v1.2.3

###### 2026/09/18

* `修复` 页面背景渲染后及时更新状态栏和导航栏颜色, 无需等待图片等资源加载完成

## v1.2.2

###### 2026/09/16

* `优化` 继 compileSdk 之后将 targetSdk 提升到 37 (Android 17), 插件行为不受新目标版本影响

## v1.2.1

###### 2026/09/15

* `优化` 将 compileSdk 提升到 37 (Android 17), targetSdk 保持 36, 待依赖目标版本的行为验证后再提升

## v1.2.0

###### 2026/09/13

* `新增` 界面提供本地发行历史, 支持多语言及英语回退
* `修复` HTTPS 资源连接仅使用经检查的公网 DNS 地址并逐次校验重定向; 禁止 WebView 直接联网, 资源请求仅支持 GET 和 HEAD
* `修复` 加载远程资源时保留 HTTP 206 等成功状态码
* `优化` 校验发行签名配置, 预期 APK 集合与可复现文档
* `依赖` 附加 OkHttp 4.12.0 用于受控 HTTPS 资源加载

## v1.1.0

###### 2026/09/11

* `新增` 新增文档大纲, 页内查找, 持久化字号调节与渲染期语法高亮, 全程不启用 JavaScript
* `新增` 新增宿主授权目录内的相对 Markdown 文档安全跳转, 支持查看器内历史返回与锚点处理
* `新增` 新增 Android 打印 / PDF 导出与有界 YAML front matter 展示
* `新增` 新增定义式与内联式 Footnotes, 并提供净化后的双向回链
* `修复` 修复主预览按钮协议拒绝和设置崩溃; 同步宿主个性化配置, 页面栏位配色及对话框黑白控件
* `优化` 强化 explorer-action v1 Intent, URI, 路径, 链接, 资源, HTML 与 Footnotes 校验, 保持单文件只读边界
* `优化` 新增可勾选 Roadmap, 4 张仅含合成数据的真实设备截图, 以及覆盖 10 种语言的可复现 README / CHANGELOG 生成链路
* `优化` 构建阶段阻止意外引入原生依赖, 并输出 JSON 校验报告
* `依赖` 从旧 Atlassian 0.9.0 fork 迁移至 Maven Central 官方 CommonMark 0.30.0 core 与扩展模块, 并通过 core library desugaring 保持 API 24 兼容

## v1.0.1

###### 2026/08/08

* `修复` 在 AutoJs6 插件中心启用插件时因服务返回空绑定 (onNullBinding) 而无法启用的问题
* `优化` 精简插件名称与描述, 统一各语言用户文档的表述

## v1.0.0

###### 2026/08/06

* `新增` Markdown Previewer 首个版本: 为 AutoJs6 文件管理器提供 `预览 Markdown` 溢出菜单动作, 以只读方式渲染单个文档
* `新增` 识别 md / markdown / mdown / mkd / mkdn / mdwn / mdtext / mdtxt / rmd / qmd 共 10 种扩展名以及 `text/markdown` 与 `text/x-markdown` MIME 类型
* `新增` 支持表格, 任务列表, 删除线, 自动链接, 标题锚点与文档内图片渲染
* `新增` 内置 GitHub (自动 / 浅色 / 深色), 纸张与棕褐色主题, 支持导入自定义 CSS, 手动刷新与全屏模式
* `新增` 以允许列表净化, CSP 约束, 禁用 JavaScript 与存储, 私网地址过滤及输入上限 (Markdown 8 MiB, CSS 256 KiB) 构建只读安全沙盒
* `新增` 基于 `org.autojs.plugin.EXPLORER_ACTION` 协议注册插件服务, 经宿主临时 content URI 授权访问所选文件及其父目录
* `新增` 插件信息, 界面, 使用说明与文档支持简体中文, 繁体中文 (香港 / 台湾), 英语, 法语, 西班牙语, 日语, 韩语, 俄语与阿拉伯语
