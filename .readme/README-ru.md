<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="markdown-previewer-ic-launcher" border="0" width="128" />
  </p>

  <p>Предпросмотр документов Markdown</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Языки (Languages)

******

Текущий README.md поддерживает следующие языки:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ko.md)
- Русский [ru] # текущий
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ar.md)

******

### Введение

******

Markdown Previewer - это плагин предварительного просмотра для файлового менеджера AutoJs6. После включения у каждого файла Markdown в файловом менеджере появляется действие `Предпросмотр Markdown` в дополнительном меню. Нажмите его, чтобы читать аккуратно отрисованный документ, как веб-страницу, вместо сплошного исходного текста.

Плагин делает одно дело и делает его безопасно: отрисовку только для чтения. Средство просмотра никогда не выполняет скрипты, читает лишь файлы, временно разрешенные хостом, и выполняет всю отрисовку на собственном экране. Оно не изменяет AutoJs6 и не влияет на среду выполнения скриптов.

******

### Основные возможности

******

- Читайте отрисованные документы Markdown прямо в файловом менеджере AutoJs6, без экспорта файлов и сторонних приложений для чтения.
- Таблицы, списки задач, зачеркивание, автоматические ссылки, якоря заголовков и изображения в документе работают сразу и покрывают привычный стиль GitHub.
- GitHub (Auto), GitHub Light, GitHub Dark, Paper, Sepia. Меню и диалоги используют язык и тёмный режим AutoJs6, как и GitHub (Auto) и автоматическая тема HTML. Панели получают представительный цвет краёв страницы и контрастные чёрные или белые надписи и значки. Для градиентов, изображений и анимации цвет сохраняется до перезагрузки, исключая мерцание.
- Импортируйте файл пользовательского CSS, чтобы создать собственный стиль чтения. Он накладывается поверх встроенного оформления и удаляется одним нажатием.
- Полноэкранный режим с настройкой `Запускать в полноэкранном режиме`. Кнопка назад сначала выходит из полноэкранного режима, а не закрывает страницу.
- Масштабирование щипком, ручное обновление, горизонтальная прокрутка таблиц и автоматическое определение кодировок UTF-8 / UTF-16 / UTF-32 по BOM.
- Песочница только для чтения: никакого выполнения JavaScript, никаких кешей на диске и никакого доступа к данным, кроме файлов, разрешенных хостом.

******

### Снимки экрана

******

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/file-menu-action.png?raw=true" alt="Действие в меню файла" width="300" />
      <br />
      <sub>Действие в меню файла</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/viewer.png?raw=true" alt="Просмотр документа" width="300" />
      <br />
      <sub>Просмотр документа</sub>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/theme-dialog.png?raw=true" alt="Выбор темы" width="300" />
      <br />
      <sub>Выбор темы</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/fullscreen.png?raw=true" alt="Полноэкранный режим" width="300" />
      <br />
      <sub>Полноэкранный режим</sub>
    </td>
  </tr>
</table>

******

### Установка и использование

******

Перед началом проверьте следующие требования:

```text
host app: AutoJs6 (org.autojs.autojs6)
minimum host build: 5269
minimum android: 7.0 (API 24)
plugin package: io.github.supermonster003.autojs6.plugin.markdownpreviewer
```

От установки до первого открытого документа всего 4 шага:

1. Скачайте и установите APK плагина. У плагина нет значка запуска; после установки им полностью управляет AutoJs6.
2. Откройте AutoJs6, зайдите в `Центр плагинов`, найдите `Просмотр Markdown` и включите его.
3. В файловом менеджере AutoJs6 найдите любой файл Markdown (например `README.md`) и откройте его дополнительное меню.
4. Выберите `Предпросмотр Markdown`. Документ откроется отрисованным в отдельном средстве просмотра.

В средстве просмотра меню в правом верхнем углу содержит команды `Обновить`, `Тема просмотра`, `Импортировать пользовательский CSS`, `Полноэкранный режим` и `Настройки`, где `Настройки` включают переключатель `Запускать в полноэкранном режиме`. Ссылки http/https из документа открываются в системном браузере, а ссылки на якоря заголовков работают внутри средства просмотра. Explorer Action v2 поддерживает основную кнопку и контекстное меню одного файла с временным доступом на чтение документа и родительского каталога. Требуется AutoJs6 build 5269 или новее.

******

### Поддерживаемые форматы

******

Плагин распознает следующие расширения файлов:

```text
md, markdown, mdown, mkd, mkdn, mdwn, mdtext, mdtxt, rmd, qmd
```

Файлы, расширение которых не указано в списке, но MIME-тип равен `text/markdown` или `text/x-markdown`, также можно просматривать. Максимальный размер документа - 8 MiB; для слишком больших файлов выводится понятное сообщение вместо усеченной отрисовки.

******

### Частые вопросы

******

**В меню файла нет пункта `Предпросмотр Markdown`?**

Проверьте по порядку: код версии AutoJs6 не ниже 5269 (подходит версия 6.8.0 и новее); плагин включен в `Центре плагинов`; расширение файла есть в списке поддерживаемых. Если хотя бы одно условие не выполнено, действие в меню не появится.

**При открытии появляется `Не удалось прочитать файл Markdown`?**

Частые причины: файл был перемещен, переименован или удален в момент открытия; размер файла превышает 8 MiB; вызов пришел не из файлового менеджера AutoJs6. По соображениям безопасности плагин отклоняет вызовы из любых других источников.

**Почему не отображаются изображения в документе?**

Средство просмотра загружает только три вида изображений: локальные изображения по относительным путям внутри каталога документа (включая подкаталоги), встроенные изображения `data:` и общедоступные изображения `https`. Незашифрованные изображения `http` и частные или зарезервированные адреса блокируются политикой безопасности.

**Можно ли просматривать файлы HTML?**

Нет. Этот плагин предназначен только для Markdown. Просмотр HTML обеспечивает отдельный плагин HTML Previewer; см. ссылки ниже.

**Как работает пользовательский CSS?**

Выберите в меню `Импортировать пользовательский CSS` и укажите таблицу стилей размером не более 256 KiB. Тема автоматически переключится на `Пользовательский CSS`. Таблица накладывается поверх базового оформления, поэтому достаточно написать только правила, которые нужно переопределить. Команда `Удалить пользовательский CSS` возвращает тему `GitHub (Авто)`.

**Как работает подсветка синтаксиса и почему Mermaid и математические формулы не отображаются?**

Подсветка синтаксиса для распознанных языков кода создается на этапе рендеринга Markdown и не требует JavaScript. Mermaid и математические формулы по-прежнему зависят от клиентских скриптов, поэтому не отображаются, так как средство просмотра оставляет JavaScript отключенным.

******

### Безопасность

******

Средство просмотра построено по принципу запрета по умолчанию. Все перечисленные меры всегда активны и не могут быть отключены:

- Результат отрисовки очищается по списку разрешенного: скрипты, формы, iframe, встроенные обработчики событий и другой опасный контент удаляются, а JavaScript остается отключенным постоянно.
- WebView работает с отключенными хранилищем, cookie, сохранением форм и доступом к файловой системе. Результат отрисовки существует только в памяти и уничтожается при закрытии страницы.
- Плагин читает выбранный файл и его каталог исключительно через временные content URI, выданные хостом. Он не получает путей файловой системы и не запрашивает дополнительных разрешений.
- CSP и перехват запросов вдвойне ограничивают загрузку ресурсов: проходят только встроенные стили, ресурсы каталога документа, изображения `data:` и `https`; любые другие запросы отклоняются.
- Удаленные изображения фильтруются от частных и зарезервированных адресов (защита от SSRF) и загружаются с политикой no-referrer; внешние ссылки открываются только в системном браузере.
- Входные данные ограничены: Markdown до 8 MiB, пользовательский CSS до 256 KiB, длины отображаемых имен и путей также ограничены.
- Ресурсы HTTPS используют проверенные публичные адреса DNS с проверкой каждого перенаправления; прямая сеть WebView отключена, поддерживаются только GET и HEAD

******

### Интерфейс плагина (для разработчиков)

******

Хост обнаруживает и вызывает плагин по следующим идентификаторам:

```text
service action: org.autojs.plugin.EXPLORER_ACTION
execute action: org.autojs.plugin.EXPLORER_ACTION_EXECUTE
plugin id: markdown-previewer
engine: explorer-action
variant: default
required host build: 5269
```

Explorer Action v2 поддерживает основную кнопку и контекстное меню одного файла с временным доступом на чтение документа и родительского каталога. Требуется AutoJs6 build 5269 или новее.

******

### План развития

******

Завершенные возможности и предстоящие планы ведутся в виде списка с отметками в ROADMAP.md. Неотмеченные пункты выражают намерение и не описывают текущие возможности.

- [Открыть ROADMAP.md со списком отметок](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/ROADMAP.md)

******

### История выпусков

******

#### v1.2.3

###### 2026/09/19

* `Исправление` Цвета строки состояния и панели навигации обновляются сразу после отображения фона страницы, не дожидаясь окончания загрузки изображений и других ресурсов
* `Исправление` Предупреждения чтения SDK XML v4 с AGP 9.1 и ошибочный запуск проверки выравнивания нативных библиотек APK при сборке модульных тестов JVM, устраненные общими плагинами сборки 1.8.3

#### v1.2.2

###### 2026/09/16

* `Улучшение` Вслед за compileSdk поднят targetSdk до 37 (Android 17); поведение плагина не зависит от нового целевого уровня

#### v1.2.1

###### 2026/09/15

* `Улучшение` Поднят compileSdk до 37 (Android 17); targetSdk остаётся 36 до проверки поведения, зависящего от целевого уровня

##### Полная история

* [CHANGELOG-ru.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/assets/doc/CHANGELOG-ru.md)

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

`strings.xml` локализует сведения о плагине и интерфейс средства просмотра, а `plugin_instruction.md` содержит инструкции, показываемые хостом. Все файлы README и CHANGELOG генерируются из JSON-источников скриптом `.python/generate_markdown.py`: для изменения документации редактируйте файлы `lang_*.json` в `.readme` и `.changelog` и запускайте скрипт заново, не редактируя сгенерированные файлы Markdown.

******

### Ссылки

******

- Документация AutoJs6: https://docs.autojs6.com
- Спецификация CommonMark: https://commonmark.org
- Плагин HTML Previewer (просмотр файлов HTML): https://github.com/SuperMonster003/AutoJs6-Plugin-HTML-Previewer


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/16kb.md)
