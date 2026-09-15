<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="markdown-previewer-ic-launcher" border="0" width="128" />
  </p>

  <p>Markdown ドキュメントをプレビュー</p>

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
- GitHub (Auto), GitHub Light, GitHub Dark, Paper, Sepia. メニューとダイアログは AutoJs6 の言語とダークモードに従います. GitHub (Auto) と HTML 自動テーマも同様です. ツールバーとシステムバーには描画領域の端から取得した代表色を使い, 文字とアイコンは対比のある黒または白にします. グラデーション, 画像, アニメーションでは再読み込みまで色を固定してちらつきを防ぎます.
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
- HTTPS リソースの接続先 DNS アドレスと各リダイレクトを検証し, WebView の直接通信を無効化; リソース要求は GET と HEAD のみ対応

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

#### v1.2.2

###### 2026/09/16

* `改善` compileSdk に続き targetSdk を 37 (Android 17) に引き上げ, プラグインの動作は新しいターゲットの影響を受けない

#### v1.2.1

###### 2026/09/15

* `改善` compileSdk を 37 (Android 17) に引き上げ, targetSdk はターゲット依存の動作を検証するまで 36 のまま

#### v1.2.0

###### 2026/09/13

* `機能` 画面からローカルのリリース履歴を表示し, 各言語と英語へのフォールバックに対応
* `修正` HTTPS リソースの接続先 DNS アドレスと各リダイレクトを検証し, WebView の直接通信を無効化; リソース要求は GET と HEAD のみ対応
* `修正` 外部リソースの読み込みで HTTP 206 などの成功ステータスを保持
* `改善` リリース署名の設定, APK の構成, ドキュメントの再生成結果を検証
* `依存関係` 制御された HTTPS リソース読み込みに OkHttp 4.12.0 を追加

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


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/16kb.md)
