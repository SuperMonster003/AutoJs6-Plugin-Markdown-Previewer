<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="markdown-previewer-ic-launcher" border="0" width="128" />
  </p>

  <p>ファイルマネージャープラグイン. Markdown ファイルを安全に読み取り専用でプレビュー</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 言語 (Languages)

******

現在の README.md は次の言語をサポートします:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-es.md)
- 日本語 [ja] # 現在
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ar.md)

******

### 概要

******

Markdown Previewer は AutoJs6 ファイルマネージャー向けのプレビュープラグインです. 有効にすると, ファイルマネージャー内の各 Markdown ファイルのオーバーフローメニューに `Markdown をプレビュー` アクションが追加されます. タップすれば, 生のソーステキストではなく, Web ページのように整形された文書を読めます.

このプラグインはひとつのことを安全に行います: 読み取り専用レンダリングです. ビューアーはスクリプトを一切実行せず, ホストが一時的に許可したファイルだけを読み取り, すべての描画を専用画面内で完結させます. AutoJs6 本体を変更せず, スクリプト実行環境にも影響しません.

******

### 主な特長

******

- AutoJs6 ファイルマネージャー内で整形済みの Markdown 文書をそのまま読めます. ファイルのエクスポートやサードパーティ製リーダーは不要です.
- 表, タスクリスト, 取り消し線, 自動リンク, 見出しアンカー, 文書内画像が最初から動作し, 一般的な GitHub 流の記法をカバーします.
- GitHub (自動), GitHub ライト, GitHub ダーク, ペーパー, セピアの 5 種類のテーマを内蔵. 自動テーマはシステムのライト/ダークモードに追従します.
- カスタム CSS をインポートして自分だけの読書スタイルを構築できます. 内蔵スタイルの上に重なる方式で, ワンタップで消去できます.
- 没入感のある全画面モードと `全画面モードで開始` 設定を用意. 戻るボタンはページを閉じる前にまず全画面を解除します.
- ピンチズーム, 手動再読み込み, 表の横スクロールに対応し, BOM により UTF-8 / UTF-16 / UTF-32 エンコーディングを自動判別します.
- 読み取り専用のセキュリティサンドボックス: JavaScript を実行せず, ディスクキャッシュを残さず, ホストが許可したファイル以外のデータには一切アクセスしません.

******

### スクリーンショット

******

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/file-menu-action.png?raw=true" alt="ファイルメニューのアクション" width="300" />
      <br />
      <sub>ファイルメニューのアクション</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/viewer.png?raw=true" alt="ドキュメントビューア" width="300" />
      <br />
      <sub>ドキュメントビューア</sub>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/theme-dialog.png?raw=true" alt="テーマ選択" width="300" />
      <br />
      <sub>テーマ選択</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/fullscreen.png?raw=true" alt="全画面表示" width="300" />
      <br />
      <sub>全画面表示</sub>
    </td>
  </tr>
</table>

******

### インストールと使い方

******

開始する前に次の要件を確認してください:

```text
host app: AutoJs6 (org.autojs.autojs6)
minimum host build: 5269
minimum android: 7.0 (API 24)
plugin package: io.github.supermonster003.autojs6.plugin.markdownpreviewer
```

インストールから最初の文書表示まで 4 ステップです:

1. プラグインの APK をダウンロードしてインストールします. プラグインにランチャーアイコンはなく, インストール後は AutoJs6 が一元管理します.
2. AutoJs6 を開いて `プラグインセンター` に入り, `Markdown プレビュー` を見つけて有効にします.
3. AutoJs6 ファイルマネージャーで任意の Markdown ファイル (例: `README.md`) を探し, そのオーバーフローメニューを開きます.
4. `Markdown をプレビュー` を選択すると, 文書が専用ビューアーで整形表示されます.

ビューアーでは右上のメニューから `再読み込み`, `プレビューテーマ`, `カスタム CSS をインポート`, `全画面モード`, `設定` を利用でき, `設定` には `全画面モードで開始` スイッチがあります. 文書内の http/https リンクはシステムブラウザで開き, 見出しアンカーへのリンクはビューアー内で移動します. Explorer Action v2 は単一ファイルのメインプレビューボタンとメニューに対応し, 文書と親ディレクトリへの一時的な読み取り権限を使用します. AutoJs6 ビルド 5269 以降が必要です.

******

### 対応形式

******

プラグインは次のファイル拡張子を認識します:

```text
md, markdown, mdown, mkd, mkdn, mdwn, mdtext, mdtxt, rmd, qmd
```

拡張子が一覧になくても MIME タイプが `text/markdown` または `text/x-markdown` のファイルはプレビューできます. 1 つの文書の上限は 8 MiB で, 超過時は途中までの描画ではなく明確なメッセージを表示します.

******

### よくある質問

******

**ファイルのメニューに `Markdown をプレビュー` が表示されない?**

次の順に確認してください: AutoJs6 のバージョンコードが 5269 以上か (バージョン 6.8.0 以降なら条件を満たします); プラグインが `プラグインセンター` で有効になっているか; ファイル拡張子が対応リストに含まれているか. どれかひとつでも満たさないと, メニューアクションは表示されません.

**開くと `Markdown ファイルを読み取れません` と表示される?**

よくある原因: 開いた瞬間にファイルが移動, 名前変更, 削除された; ファイルサイズが 8 MiB を超えている; 呼び出しが AutoJs6 ファイルマネージャー以外から行われた. セキュリティ上の理由により, プラグインは他の呼び出し元を拒否します.

**文書内の画像が表示されないのはなぜ?**

ビューアーが読み込む画像は 3 種類だけです: 文書のあるディレクトリ (サブディレクトリを含む) 内を相対パスで参照するローカル画像, インラインの `data:` 画像, 公開 `https` 画像. 平文 `http` 画像とプライベート/予約アドレスはセキュリティポリシーによりブロックされます.

**HTML ファイルもプレビューできる?**

できません. このプラグインは Markdown 専用です. HTML のプレビューは独立した HTML Previewer プラグインが提供します. 下のリンクを参照してください.

**カスタム CSS はどのように機能する?**

メニューから `カスタム CSS をインポート` を選び, 256 KiB 以下のスタイルシートを選択すると, テーマが自動的に `カスタム CSS` に切り替わります. スタイルは基本スタイルの上に重なるため, 上書きしたいルールだけを書けば十分です. `カスタム CSS を消去` を選ぶと `GitHub (自動)` に戻ります.

**シンタックスハイライトはどのように動作し, Mermaid や数式がレンダリングされないのはなぜ?**

認識可能なコード言語のシンタックスハイライトは Markdown のレンダリング時に静的生成され, JavaScript は不要です. Mermaid と数式は引き続きクライアント側スクリプトに依存するため, JavaScript を無効に保つビューアではレンダリングされません.

******

### セキュリティ

******

ビューアーはデフォルト拒否の原則で構築されています. 次の対策はすべて常時有効で, 無効化できません:

- レンダリング結果は許可リストでサニタイズされます: スクリプト, フォーム, iframe, インラインイベントハンドラーなどの危険な内容は一律削除され, JavaScript は常に無効です.
- WebView はストレージ, Cookie, フォーム保存, ファイルシステムアクセスをすべて無効にして動作します. 描画結果はメモリ上にのみ存在し, 画面を閉じると破棄されます.
- ホストが付与した一時的な content URI だけで選択ファイルとそのディレクトリを読み取り, ファイルシステムパスを受け取らず, 追加の実行時権限も要求しません.
- CSP とリクエスト遮断がリソース読み込みを二重に制限します: 内蔵スタイル, 文書ディレクトリ内のリソース, `data:` 画像, `https` 画像のみを許可し, それ以外のリクエストはすべて拒否します.
- リモート画像はプライベート/予約アドレスのフィルター (SSRF 対策) を通し, no-referrer ポリシーで読み込みます. 外部リンクはシステムブラウザでのみ開けます.
- 入力には上限があります: Markdown は 8 MiB まで, カスタム CSS は 256 KiB まで, 表示名とパスの長さも制限されます.

******

### プラグインインターフェース (開発者向け)

******

ホストは次の識別情報でプラグインを検出して呼び出します:

```text
service action: org.autojs.plugin.EXPLORER_ACTION
execute action: org.autojs.plugin.EXPLORER_ACTION_EXECUTE
plugin id: markdown-previewer
engine: explorer-action
variant: default
required host build: 5269
```

Explorer Action v2 は単一ファイルのメインプレビューボタンとメニューに対応し, 文書と親ディレクトリへの一時的な読み取り権限を使用します. AutoJs6 ビルド 5269 以降が必要です.

******

### ロードマップ

******

完成済みの機能と今後の計画はチェック可能なリストとして ROADMAP.md で管理しています. 未チェックの項目は意向を示すもので, 現在の機能を表しません.

- [チェック可能な ROADMAP.md を開く](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/ROADMAP.md)

******

### リリース履歴

******

#### v1.1.0

###### 2026/08/31

* `機能` JavaScript を有効にせず, ドキュメントアウトライン, ページ内検索, 永続的な文字サイズ調整, レンダリング時のシンタックスハイライトを追加
* `機能` ホストが許可したディレクトリ内の相対 Markdown 文書への安全な移動を追加し, ビューア内履歴とアンカー処理に対応
* `機能` Android 印刷 / PDF 書き出しと上限付き YAML front matter 表示を追加
* `機能` 定義形式とインライン形式の Footnotes を追加し, サニタイズ済み双方向バックリンクに対応
* `改善` 単一ファイル読み取り専用境界を維持しながら, explorer-action v1 Intent, URI, パス, リンク, リソース, HTML, Footnotes の検証を強化
* `改善` チェック可能な Roadmap, 合成データのみを使った実機スクリーンショット 4 枚, 10 言語向けの再現可能な README / CHANGELOG 生成を追加
* `依存関係` CommonMark を旧 Atlassian 0.9.0 fork から Maven Central 公式 0.30.0 core / 拡張モジュールへ移行し, core library desugaring で API 24 互換性を維持

#### v1.0.1

###### 2026/08/08

* `修正` AutoJs6 プラグインセンターでプラグインを有効化する際, サービスが空のバインディング (onNullBinding) を返して有効化に失敗する問題
* `改善` プラグインの名称と説明を簡潔にし, 各言語のユーザードキュメントの表現を統一

#### v1.0.0

###### 2026/08/06

* `機能` Markdown Previewer 初回リリース: AutoJs6 ファイルマネージャーに単一文書を読み取り専用で表示する `Markdown をプレビュー` オーバーフローメニューアクションを提供
* `機能` md / markdown / mdown / mkd / mkdn / mdwn / mdtext / mdtxt / rmd / qmd の 10 種類の拡張子と `text/markdown` および `text/x-markdown` MIME タイプを認識
* `機能` 表, タスクリスト, 取り消し線, 自動リンク, 見出しアンカー, 文書内画像のレンダリングに対応
* `機能` GitHub (自動 / ライト / ダーク), ペーパー, セピアのテーマを搭載し, カスタム CSS のインポート, 手動再読み込み, 全画面モードに対応
* `機能` 許可リストによるサニタイズ, CSP 制約, JavaScript とストレージの無効化, プライベートアドレスのフィルタリング, 入力上限 (Markdown 8 MiB, CSS 256 KiB) による読み取り専用サンドボックスを構築
* `機能` `org.autojs.plugin.EXPLORER_ACTION` プロトコルでプラグインサービスを登録し, ホストが付与する一時的な content URI で選択ファイルと親ディレクトリにアクセス
* `機能` プラグイン情報, UI, 使用説明, ドキュメントを簡体字中国語, 繁体字中国語 (香港 / 台湾), 英語, フランス語, スペイン語, 日本語, 韓国語, ロシア語, アラビア語に対応

##### 完全な履歴

* [CHANGELOG-ja.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/assets/doc/CHANGELOG-ja.md)

******

### ビルド

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release ビルド:

```powershell
.\gradlew.bat :app:assembleRelease
```

ビルド設定は `version.properties` から読み込みます. 現在の最小 SDK は 24, ターゲット SDK は 36 です.

******

### リソース構成

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG-*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` はプラグイン情報とビューアー UI をローカライズし, `plugin_instruction.md` はホストに表示する使用説明を提供します. すべての README と CHANGELOG は `.python/generate_markdown.py` が JSON ソースから生成します: ドキュメントを変更するときは `.readme` と `.changelog` 配下の `lang_*.json` を編集してスクリプトを再実行し, 生成済み Markdown ファイルを直接編集しないでください.

******

### リンク

******

- AutoJs6 ドキュメント: https://docs.autojs6.com
- CommonMark 仕様: https://commonmark.org
- HTML Previewer プラグイン (HTML ファイルのプレビュー): https://github.com/SuperMonster003/AutoJs6-Plugin-HTML-Previewer
