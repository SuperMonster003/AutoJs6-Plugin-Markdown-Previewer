<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="markdown-previewer-ic-launcher" border="0" width="128" />
  </p>

  <p>파일 관리자 플러그인. Markdown 파일의 안전한 읽기 전용 미리보기</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 언어 (Languages)

******

현재 README.md는 다음 언어를 지원합니다:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ja.md)
- 한국어 [ko] # 현재
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ar.md)

******

### 소개

******

Markdown Previewer는 AutoJs6 파일 관리자를 위한 미리보기 플러그인입니다. 활성화하면 파일 관리자의 모든 Markdown 파일 더보기 메뉴에 `Markdown 미리보기` 동작이 추가됩니다. 이를 탭하면 원시 소스 텍스트 덩어리 대신 웹 페이지처럼 보기 좋게 렌더링된 문서를 읽을 수 있습니다.

이 플러그인은 한 가지 일을 안전하게 수행합니다: 읽기 전용 렌더링입니다. 뷰어는 스크립트를 전혀 실행하지 않고, 호스트가 임시로 허가한 파일만 읽을 수 있으며, 모든 렌더링을 자체 화면 안에서 끝냅니다. AutoJs6 본체를 수정하지 않으며 스크립트 실행 환경에도 영향을 주지 않습니다.

******

### 주요 기능

******

- AutoJs6 파일 관리자 안에서 렌더링된 Markdown 문서를 바로 읽을 수 있습니다. 파일 내보내기나 제3자 리더 앱이 필요 없습니다.
- 표, 작업 목록, 취소선, 자동 링크, 제목 앵커, 문서 내 이미지가 기본으로 동작하여 일반적인 GitHub 스타일 문법을 지원합니다.
- GitHub (Auto), GitHub Light, GitHub Dark, Paper, Sepia. 메뉴와 대화상자는 AutoJs6 언어와 다크 모드를 따릅니다. GitHub (Auto)와 HTML 자동 테마도 동일합니다. 도구 모음과 시스템 표시줄은 렌더링 영역 가장자리의 대표색과 대비되는 검정 또는 흰색 글자 및 아이콘을 사용합니다. 그라데이션, 이미지, 애니메이션에서는 다시 로드할 때까지 색을 고정하여 깜박임을 방지합니다.
- 사용자 지정 CSS를 가져와 나만의 읽기 스타일을 만들 수 있습니다. 기본 스타일 위에 겹쳐 적용되며 한 번의 탭으로 지울 수 있습니다.
- 몰입형 전체 화면 모드와 `전체 화면 모드로 시작` 설정을 제공합니다. 뒤로 버튼은 페이지를 닫기 전에 먼저 전체 화면을 해제합니다.
- 두 손가락 확대/축소, 수동 새로 고침, 표 가로 스크롤을 지원하고 BOM으로 UTF-8 / UTF-16 / UTF-32 인코딩을 자동 인식합니다.
- 읽기 전용 보안 샌드박스: JavaScript를 실행하지 않고, 디스크 캐시를 남기지 않으며, 호스트가 허가한 파일 외의 어떤 데이터에도 접근하지 않습니다.

******

### 스크린샷

******

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/file-menu-action.png?raw=true" alt="파일 메뉴 동작" width="300" />
      <br />
      <sub>파일 메뉴 동작</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/viewer.png?raw=true" alt="문서 뷰어" width="300" />
      <br />
      <sub>문서 뷰어</sub>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/theme-dialog.png?raw=true" alt="테마 선택" width="300" />
      <br />
      <sub>테마 선택</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/fullscreen.png?raw=true" alt="전체 화면 읽기" width="300" />
      <br />
      <sub>전체 화면 읽기</sub>
    </td>
  </tr>
</table>

******

### 설치 및 사용

******

시작하기 전에 다음 요구 사항을 확인하세요:

```text
host app: AutoJs6 (org.autojs.autojs6)
minimum host build: 5269
minimum android: 7.0 (API 24)
plugin package: io.github.supermonster003.autojs6.plugin.markdownpreviewer
```

설치부터 첫 문서를 여는 것까지 4단계입니다:

1. 플러그인 APK를 다운로드하여 설치합니다. 플러그인에는 런처 아이콘이 없으며 설치 후 AutoJs6가 전적으로 관리합니다.
2. AutoJs6를 열고 `플러그인 센터`에 들어가 `Markdown 미리보기`를 찾아 활성화합니다.
3. AutoJs6 파일 관리자에서 아무 Markdown 파일 (예: `README.md`)을 찾아 해당 파일의 더보기 메뉴를 엽니다.
4. `Markdown 미리보기`를 선택하면 문서가 전용 뷰어에서 렌더링되어 열립니다.

뷰어에서는 오른쪽 위 메뉴로 `새로 고침`, `미리보기 테마`, `사용자 지정 CSS 가져오기`, `전체 화면 모드`, `설정`을 사용할 수 있으며, `설정`에는 `전체 화면 모드로 시작` 스위치가 있습니다. 문서 안의 http/https 링크는 시스템 브라우저에서 열리고, 제목 앵커 링크는 뷰어 안에서 이동합니다. Explorer Action v2는 단일 파일의 기본 미리보기 버튼과 메뉴를 지원하며 문서와 상위 폴더의 임시 읽기 권한을 사용합니다. AutoJs6 빌드 5269 이상이 필요합니다.

******

### 지원 형식

******

플러그인은 다음 파일 확장자를 인식합니다:

```text
md, markdown, mdown, mkd, mkdn, mdwn, mdtext, mdtxt, rmd, qmd
```

확장자가 목록에 없어도 MIME 유형이 `text/markdown` 또는 `text/x-markdown`인 파일은 미리 볼 수 있습니다. 문서 하나의 최대 크기는 8 MiB이며, 초과하면 잘린 렌더링 대신 명확한 메시지를 표시합니다.

******

### 자주 묻는 질문

******

**파일 메뉴에 `Markdown 미리보기`가 보이지 않나요?**

다음 순서로 확인하세요: AutoJs6 버전 코드가 5269 이상인지 (버전 6.8.0 이상이면 충족); 플러그인이 `플러그인 센터`에서 활성화되어 있는지; 파일 확장자가 지원 목록에 있는지. 셋 중 하나라도 충족하지 않으면 메뉴 동작이 나타나지 않습니다.

**열 때 `Markdown 파일을 읽을 수 없습니다`라고 표시되나요?**

흔한 원인: 여는 순간 파일이 이동, 이름 변경 또는 삭제됨; 파일 크기가 8 MiB를 초과함; 호출이 AutoJs6 파일 관리자에서 오지 않음. 보안상의 이유로 플러그인은 다른 출처의 호출을 거부합니다.

**문서의 이미지가 표시되지 않는 이유는 무엇인가요?**

뷰어는 세 종류의 이미지만 불러옵니다: 문서가 있는 디렉터리 (하위 디렉터리 포함) 안을 상대 경로로 가리키는 로컬 이미지, 인라인 `data:` 이미지, 공개 `https` 이미지. 평문 `http` 이미지와 사설/예약 주소는 보안 정책에 의해 차단됩니다.

**HTML 파일도 미리 볼 수 있나요?**

아니요. 이 플러그인은 Markdown 전용입니다. HTML 미리보기는 별도의 HTML Previewer 플러그인이 제공합니다. 아래 링크를 참고하세요.

**사용자 지정 CSS는 어떻게 동작하나요?**

메뉴에서 `사용자 지정 CSS 가져오기`를 선택하고 256 KiB 이하의 스타일시트를 고르면 테마가 자동으로 `사용자 지정 CSS`로 전환됩니다. 스타일은 기본 스타일 위에 겹쳐 적용되므로 바꾸고 싶은 규칙만 작성하면 됩니다. `사용자 지정 CSS 지우기`를 선택하면 `GitHub (자동)`으로 돌아갑니다.

**구문 강조는 어떻게 작동하며 Mermaid와 수식은 왜 렌더링되지 않나요?**

인식되는 코드 언어의 구문 강조는 Markdown 렌더링 단계에서 정적으로 생성되며 JavaScript가 필요하지 않습니다. Mermaid와 수식은 여전히 클라이언트 스크립트에 의존하므로 JavaScript를 비활성화한 뷰어에서는 렌더링되지 않습니다.

******

### 보안

******

뷰어는 기본 거부 원칙으로 만들어졌습니다. 다음 조치는 모두 항상 켜져 있으며 끌 수 없습니다:

- 렌더링 결과는 허용 목록으로 정화됩니다: 스크립트, 폼, iframe, 인라인 이벤트 핸들러 등 위험한 내용은 모두 제거되고 JavaScript는 항상 비활성화됩니다.
- WebView는 저장소, 쿠키, 폼 저장, 파일 시스템 접근을 모두 끈 상태로 동작합니다. 렌더링 결과는 메모리에만 존재하며 페이지를 닫으면 파기됩니다.
- 호스트가 부여한 임시 content URI로만 선택한 파일과 해당 디렉터리를 읽습니다. 파일 시스템 경로를 받지 않으며 추가 런타임 권한도 요청하지 않습니다.
- CSP와 요청 가로채기가 리소스 로드를 이중으로 제한합니다: 내장 스타일시트, 문서 디렉터리 안의 리소스, `data:` 이미지, `https` 이미지만 통과하며 그 외 요청은 모두 거부됩니다.
- 원격 이미지는 사설/예약 주소 필터 (SSRF 방지)를 거치고 no-referrer 정책으로 로드됩니다. 외부 링크는 시스템 브라우저로만 열 수 있습니다.
- 입력에는 상한이 있습니다: Markdown은 8 MiB까지, 사용자 지정 CSS는 256 KiB까지이며 표시 이름과 경로 길이도 제한됩니다.

******

### 플러그인 인터페이스 (개발자용)

******

호스트는 다음 식별 정보로 플러그인을 발견하고 호출합니다:

```text
service action: org.autojs.plugin.EXPLORER_ACTION
execute action: org.autojs.plugin.EXPLORER_ACTION_EXECUTE
plugin id: markdown-previewer
engine: explorer-action
variant: default
required host build: 5269
```

Explorer Action v2는 단일 파일의 기본 미리보기 버튼과 메뉴를 지원하며 문서와 상위 폴더의 임시 읽기 권한을 사용합니다. AutoJs6 빌드 5269 이상이 필요합니다.

******

### 로드맵

******

완료된 기능과 향후 계획은 체크 가능한 목록으로 ROADMAP.md에서 관리합니다. 체크되지 않은 항목은 의향을 나타내며 현재 기능을 설명하지 않습니다.

- [체크 가능한 ROADMAP.md 열기](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/ROADMAP.md)

******

### 릴리스 기록

******

#### v1.1.0

###### 2026/08/31

* `기능` JavaScript를 활성화하지 않고 문서 개요, 페이지 내 찾기, 영구 텍스트 크기 조절 및 렌더링 시 구문 강조 기능 추가
* `기능` 호스트가 승인한 디렉터리 내 상대 Markdown 문서로 안전하게 이동하고 뷰어 내 기록 및 앵커 처리를 지원
* `기능` Android 인쇄 / PDF 내보내기와 제한된 YAML front matter 표시 기능 추가
* `기능` 정의형 및 인라인 Footnotes와 정제된 양방향 돌아가기 링크 추가
* `수정` 기본 미리보기 버튼의 프로토콜 거부 및 설정 충돌 수정; 호스트 외관, 표시줄 색상, 대화상자 흑백 컨트롤 동기화
* `개선` 단일 파일 읽기 전용 경계를 유지하면서 explorer-action v1 Intent, URI, 경로, 링크, 리소스, HTML 및 Footnotes 검증 강화
* `개선` 체크 가능한 Roadmap, 합성 데이터만 사용한 실제 기기 스크린샷 4장, 10개 언어용 재현 가능한 README / CHANGELOG 생성 추가
* `의존성` CommonMark를 기존 Atlassian 0.9.0 fork에서 Maven Central 공식 0.30.0 core 및 확장 모듈로 이전하고 core library desugaring으로 API 24 호환성 유지

#### v1.0.1

###### 2026/08/08

* `수정` AutoJs6 플러그인 센터에서 플러그인을 활성화할 때 서비스가 빈 바인딩 (onNullBinding)을 반환하여 활성화에 실패하던 문제
* `개선` 플러그인 이름과 설명을 간결하게 다듬고 각 언어 사용자 문서의 표현을 통일

#### v1.0.0

###### 2026/08/06

* `기능` Markdown Previewer 첫 릴리스: AutoJs6 파일 관리자에 단일 문서를 읽기 전용으로 렌더링하는 `Markdown 미리보기` 더보기 메뉴 동작 제공
* `기능` md / markdown / mdown / mkd / mkdn / mdwn / mdtext / mdtxt / rmd / qmd 등 10가지 확장자와 `text/markdown` 및 `text/x-markdown` MIME 유형 인식
* `기능` 표, 작업 목록, 취소선, 자동 링크, 제목 앵커, 문서 내 이미지 렌더링 지원
* `기능` GitHub (자동 / 라이트 / 다크), 종이, 세피아 테마 내장, 사용자 지정 CSS 가져오기, 수동 새로 고침, 전체 화면 모드 지원
* `기능` 허용 목록 정화, CSP 제약, JavaScript 및 저장소 비활성화, 사설 주소 필터링, 입력 상한 (Markdown 8 MiB, CSS 256 KiB)으로 읽기 전용 보안 샌드박스 구축
* `기능` `org.autojs.plugin.EXPLORER_ACTION` 프로토콜로 플러그인 서비스를 등록하고 호스트가 부여한 임시 content URI로 선택한 파일과 상위 디렉터리에 접근
* `기능` 플러그인 정보, UI, 사용 설명, 문서를 중국어 간체, 중국어 번체 (홍콩 / 대만), 영어, 프랑스어, 스페인어, 일본어, 한국어, 러시아어, 아랍어로 지원

##### 전체 기록

* [CHANGELOG-ko.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/assets/doc/CHANGELOG-ko.md)

******

### 빌드

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release 빌드:

```powershell
.\gradlew.bat :app:assembleRelease
```

빌드 매개변수는 `version.properties`에서 가져옵니다. 현재 최소 SDK는 24이고 대상 SDK는 36입니다.

******

### 리소스 구조

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG-*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml`은 플러그인 정보와 뷰어 UI를 현지화하고, `plugin_instruction.md`는 호스트가 표시하는 사용 설명을 제공합니다. 모든 README와 CHANGELOG는 `.python/generate_markdown.py`가 JSON 소스에서 생성합니다: 문서를 수정할 때는 `.readme`와 `.changelog` 아래의 `lang_*.json`을 편집한 뒤 스크립트를 다시 실행하고, 생성된 Markdown 파일을 직접 편집하지 마세요.

******

### 링크

******

- AutoJs6 문서: https://docs.autojs6.com
- CommonMark 명세: https://commonmark.org
- HTML Previewer 플러그인 (HTML 파일 미리보기): https://github.com/SuperMonster003/AutoJs6-Plugin-HTML-Previewer
