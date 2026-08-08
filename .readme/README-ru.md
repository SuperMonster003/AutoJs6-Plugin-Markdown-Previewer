<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="markdown-preview-ic-launcher" border="0" width="128" />
  </p>

  <p>Плагин файлового менеджера. Безопасный просмотр файлов Markdown только для чтения</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Markdown-Preview?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Markdown-Preview?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Markdown-Preview?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Языки (Languages)

******

Текущий README.md поддерживает следующие языки:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-ko.md)
- Русский [ru] # текущий
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/.readme/README-ar.md)

******

### Введение

******

Markdown Preview добавляет в файловый менеджер действие для просмотра одного файла Markdown только для чтения. Содержимое отображается в отдельном средстве просмотра без встраивания реализации в приложение-хост.

******

### Возможности

******

- Регистрирует действие проводника только для чтения одного файла через общий протокол `org.autojs.plugin.EXPLORER_ACTION`.
- Получает временный доступ на чтение выбранного файла Markdown и ресурсов родительского каталога через URI содержимого вместо исходных путей файловой системы.
- Поддерживает автоматические ссылки, таблицы, зачеркивание, якоря заголовков, списки задач и изображения в документе.
- Предоставляет темы GitHub Auto, GitHub Light, GitHub Dark, Paper, Sepia и пользовательский CSS.
- Поддерживает обновление, полноэкранный режим, запуск в полноэкранном режиме и ограничения размера Markdown и CSS.

******

### Поддерживаемые форматы

******

Первый выпуск распознает следующие расширения файлов:

```text
md, markdown, mdown, mkd, mkdn, mdwn, mdtext, mdtxt, rmd, qmd
```

******

### Интерфейс плагина

******

Хост обнаруживает и запускает плагин по следующим идентификаторам:

```text
service action: org.autojs.plugin.EXPLORER_ACTION
execute action: org.autojs.plugin.EXPLORER_ACTION_EXECUTE
plugin id: markdown-preview
engine: explorer-action
variant: default
```

Версия 1 ограничена действием дополнительного меню только для чтения одного файла в файловом менеджере.

******

### Безопасность

******

Средство просмотра очищает результат по списку разрешений, отключает JavaScript, хранилище WebView, файлы cookie и прямой доступ к файлам, ограничивает ресурсы и навигацию политиками CSP и URI и принимает только временные разрешения от хоста.

******

### История выпусков

******

# v1.0.1

###### 2026/08/08

* `Исправление` Нулевая привязка службы, препятствовавшая включению в центре плагинов
* `Улучшение` Более ясные название, описание и пользовательская документация

# v1.0.0

###### 2026/08/06

* `Функция` Плагин Markdown Preview с ID `markdown-preview`, движком `explorer-action` и вариантом `default`
* `Функция` Действие дополнительного меню только для чтения одного файла в файловом менеджере через `org.autojs.plugin.EXPLORER_ACTION`
* `Функция` Запуск через `org.autojs.plugin.EXPLORER_ACTION_EXECUTE` с временным доступом на чтение URI содержимого файла и родительского каталога
* `Функция` Отображение Markdown с автоматическими ссылками, таблицами, зачеркиванием, якорями заголовков, списками задач и изображениями в документе
* `Функция` Темы GitHub Auto, GitHub Light, GitHub Dark, Paper, Sepia и пользовательский CSS с обновлением и полноэкранным режимом
* `Функция` Усиленная политика WebView с очисткой по списку разрешений, CSP, контролем URI, отключенными JavaScript и хранилищем и ограниченными входными данными
* `Функция` Локализация сведений, интерфейса, инструкций, README и changelog на испанский, французский, русский, арабский, японский, корейский, английский, упрощенный китайский, гонконгский традиционный китайский и тайваньский традиционный китайский

##### Дополнительная история выпусков

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Preview/blob/master/app/src/main/assets/doc/CHANGELOG-ru.md)

******

### Сборка

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Сборка Release:

```powershell
.\gradlew.bat :app:assembleRelease
```

Параметры сборки задаются в `version.properties`. Текущий минимальный SDK равен 24, целевой SDK равен 36.

******

### Структура ресурсов

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG-*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` локализует сведения о плагине и интерфейс, а `plugin_instruction.md` содержит инструкции для хоста. README и changelog генерируются из JSON скриптом `.python/generate_markdown.py`.

******

### Ссылки

******

- Документация AutoJs6: https://docs.autojs6.com
- CommonMark: https://commonmark.org
