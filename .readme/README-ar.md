<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="markdown-previewer-ic-launcher" border="0" width="128" />
  </p>

  <p>ملحق مدير الملفات. معاينة آمنة للقراءة فقط لملفات Markdown</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=534BAE&label=License"/></a>
  </p>
</div>

******

### اللغات (Languages)

******

يدعم README.md الحالي اللغات التالية:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ru.md)
- العربية [ar] # الحالي

******

### مقدمة

******

Markdown Previewer هو ملحق معاينة لمدير الملفات في AutoJs6. بعد التمكين, يظهر إجراء `معاينة Markdown` في القائمة الإضافية لكل ملف Markdown في مدير الملفات. اضغط عليه لقراءة المستند بتنسيق جميل مثل صفحة ويب, بدلا من كتلة نص مصدري خام.

يقوم الملحق بمهمة واحدة وينجزها بأمان: العرض للقراءة فقط. لا ينفذ العارض أي نصوص برمجية, ولا يقرأ سوى الملفات المصرح بها مؤقتا من المضيف, وينجز كل العرض داخل شاشته الخاصة. لا يعدل AutoJs6 نفسه ولا يؤثر على بيئة تشغيل النصوص البرمجية.

******

### أبرز الميزات

******

- اقرأ مستندات Markdown المنسقة مباشرة داخل مدير ملفات AutoJs6, دون تصدير الملفات أو تثبيت تطبيقات قراءة خارجية.
- الجداول وقوائم المهام والنص المشطوب والروابط التلقائية ومراسي العناوين والصور داخل المستند تعمل فورا وتغطي أسلوب الكتابة المعتاد في GitHub.
- خمس سمات مدمجة: GitHub (تلقائي) و GitHub فاتح و GitHub داكن وورق وبني داكن. تتبع السمة التلقائية وضع النظام الفاتح/الداكن.
- استورد ملف CSS مخصصا لبناء أسلوب قراءتك الخاص. يوضع فوق التنسيق المدمج ويمكن مسحه بلمسة واحدة.
- وضع ملء الشاشة الغامر مع خيار `البدء في وضع ملء الشاشة`. زر الرجوع يخرج من ملء الشاشة أولا بدلا من إغلاق الصفحة.
- تكبير بالقرص, وتحديث يدوي, وجداول قابلة للتمرير أفقيا, واكتشاف تلقائي عبر BOM لترميزات UTF-8 / UTF-16 / UTF-32.
- بيئة معزولة للقراءة فقط: لا تنفيذ لـ JavaScript, ولا ذاكرة تخزين مؤقت على القرص, ولا وصول إلى أي بيانات غير الملفات المصرح بها من المضيف.

******

### لقطات الشاشة

******

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/file-menu-action.png?raw=true" alt="إجراء قائمة الملف" width="300" />
      <br />
      <sub>إجراء قائمة الملف</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/viewer.png?raw=true" alt="عارض المستند" width="300" />
      <br />
      <sub>عارض المستند</sub>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/theme-dialog.png?raw=true" alt="منتقي السمة" width="300" />
      <br />
      <sub>منتقي السمة</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/fullscreen.png?raw=true" alt="القراءة في وضع ملء الشاشة" width="300" />
      <br />
      <sub>القراءة في وضع ملء الشاشة</sub>
    </td>
  </tr>
</table>

******

### التثبيت والاستخدام

******

قبل البدء, تأكد من المتطلبات التالية:

```text
host app: AutoJs6 (org.autojs.autojs6)
minimum host build: 5269
minimum android: 7.0 (API 24)
plugin package: io.github.supermonster003.autojs6.plugin.markdownpreviewer
```

من التثبيت إلى فتح أول مستند 4 خطوات:

1. نزل ملف APK الخاص بالملحق وثبته. لا يملك الملحق أيقونة تشغيل; بعد التثبيت يديره AutoJs6 بالكامل.
2. افتح AutoJs6, وادخل إلى `مركز الاضافات`, وحدد `معاينة Markdown` وقم بتمكينه.
3. في مدير ملفات AutoJs6, حدد أي ملف Markdown (مثل `README.md`) وافتح قائمته الإضافية.
4. اختر `معاينة Markdown`. يفتح المستند منسقا في عارض مخصص.

داخل العارض, توفر القائمة في الأعلى `تحديث` و `سمة المعاينة` و `استيراد CSS مخصص` و `وضع ملء الشاشة` و `الإعدادات`, حيث تحتوي `الإعدادات` على مفتاح `البدء في وضع ملء الشاشة`. تفتح روابط http/https في المستند عبر متصفح النظام, بينما تنتقل روابط مراسي العناوين داخل العارض. يدعم Explorer Action v2 زر العرض الرئيسي وقائمة الملف الواحد مع إذن قراءة مؤقت للمستند والمجلد الأب. يلزم AutoJs6 بالإصدار الداخلي 5269 أو أحدث.

******

### التنسيقات المدعومة

******

يتعرف الملحق على امتدادات الملفات التالية:

```text
md, markdown, mdown, mkd, mkdn, mdwn, mdtext, mdtxt, rmd, qmd
```

الملفات التي لا يظهر امتدادها في القائمة لكن نوع MIME لها هو `text/markdown` أو `text/x-markdown` يمكن معاينتها أيضا. الحد الأقصى للمستند الواحد هو 8 MiB; الملفات الأكبر تعرض رسالة واضحة بدلا من عرض مبتور.

******

### الأسئلة الشائعة

******

**لا يظهر `معاينة Markdown` في قائمة الملف؟**

تحقق بالترتيب: أن يكون رمز إصدار AutoJs6 لا يقل عن 5269 (الإصدار 6.8.0 أو أحدث يفي بالشرط); وأن الملحق ممكن في `مركز الاضافات`; وأن امتداد الملف موجود في قائمة الدعم. إذا لم يتحقق أي شرط من الثلاثة, فلن يظهر الإجراء في القائمة.

**يفشل الفتح مع رسالة `تعذرت قراءة ملف Markdown`؟**

الأسباب الشائعة: نقل الملف أو إعادة تسميته أو حذفه لحظة الفتح; أو تجاوز حجم الملف 8 MiB; أو أن الاستدعاء لم يأت من مدير ملفات AutoJs6. لأسباب أمنية يرفض الملحق الاستدعاءات من أي مصدر آخر.

**لماذا لا تظهر الصور داخل المستند؟**

لا يحمل العارض سوى ثلاثة أنواع من الصور: الصور المحلية المشار إليها بمسارات نسبية داخل مجلد المستند (بما فيه المجلدات الفرعية), وصور `data:` المضمنة, وصور `https` العامة. تحجب سياسة الأمان صور `http` غير المشفرة والعناوين الخاصة أو المحجوزة.

**هل يمكنه معاينة ملفات HTML؟**

لا. يركز هذا الملحق على Markdown. توفر معاينة HTML عبر ملحق HTML Previewer المستقل; انظر الروابط أدناه.

**كيف يعمل CSS المخصص؟**

اختر `استيراد CSS مخصص` من القائمة وحدد ورقة أنماط لا تتجاوز 256 KiB. تتحول السمة تلقائيا إلى `CSS مخصص`. توضع الورقة فوق التنسيق الأساسي, لذا يكفي كتابة القواعد التي تريد تجاوزها فقط. اختر `مسح CSS المخصص` للعودة إلى `GitHub (تلقائي)`.

**كيف يعمل تمييز بناء الجملة ولماذا لا يتم عرض Mermaid والمعادلات الرياضية؟**

يتم إنشاء تمييز بناء الجملة للغات البرمجة المعروفة أثناء عرض Markdown ولا يحتاج إلى JavaScript. ما زالت Mermaid والمعادلات الرياضية تعتمد على نصوص برمجية من جهة العميل, لذلك لا يتم عرضها لأن العارض يبقي JavaScript معطلا.

******

### الأمان

******

بني العارض على مبدأ الرفض الافتراضي. جميع الإجراءات التالية مفعلة دائما ولا يمكن تعطيلها:

- ينقى ناتج العرض وفق قائمة سماح: تزال النصوص البرمجية والنماذج وإطارات iframe ومعالجات الأحداث المضمنة وأي محتوى خطير آخر, ويبقى JavaScript معطلا طوال الوقت.
- يعمل WebView مع تعطيل التخزين وملفات تعريف الارتباط وحفظ النماذج والوصول إلى نظام الملفات. يعيش ناتج العرض في الذاكرة فقط ويدمر عند إغلاق الصفحة.
- يقرأ الملحق الملف المحدد ومجلده فقط عبر content URI مؤقتة يمنحها المضيف. لا يتلقى مسارات نظام الملفات ولا يطلب أي أذونات إضافية.
- تقيد CSP واعتراض الطلبات تحميل الموارد بشكل مزدوج: لا يمر سوى الأنماط المدمجة وموارد مجلد المستند وصور `data:` وصور `https`; ويرفض أي طلب آخر.
- ترشح الصور البعيدة ضد العناوين الخاصة والمحجوزة (حماية من SSRF) وتحمل بسياسة no-referrer; ولا تفتح الروابط الخارجية إلا في متصفح النظام.
- المدخلات محدودة: Markdown حتى 8 MiB, و CSS المخصص حتى 256 KiB, مع تقييد أطوال أسماء العرض والمسارات أيضا.

******

### واجهة الملحق (للمطورين)

******

يكتشف المضيف الملحق ويستدعيه بالمعرفات التالية:

```text
service action: org.autojs.plugin.EXPLORER_ACTION
execute action: org.autojs.plugin.EXPLORER_ACTION_EXECUTE
plugin id: markdown-previewer
engine: explorer-action
variant: default
required host build: 5269
```

يدعم Explorer Action v2 زر العرض الرئيسي وقائمة الملف الواحد مع إذن قراءة مؤقت للمستند والمجلد الأب. يلزم AutoJs6 بالإصدار الداخلي 5269 أو أحدث.

******

### خارطة الطريق

******

تدار القدرات المكتملة والخطط القادمة كقائمة قابلة للتحديد في ROADMAP.md. تعبر العناصر غير المحددة عن نية ولا تصف القدرات الحالية.

- [فتح ROADMAP.md القابل للتحديد](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/ROADMAP.md)

******

### سجل الإصدارات

******

#### v1.1.0

###### 2026/08/31

* `ميزة` إضافة مخطط المستند والبحث داخل الصفحة وتكبير النص المحفوظ وتمييز بناء الجملة وقت العرض من دون تفعيل JavaScript
* `ميزة` إضافة تنقل آمن إلى مستندات Markdown النسبية داخل الدليل المصرح به من المضيف مع سجل داخل العارض ومعالجة الروابط المرساة
* `ميزة` إضافة طباعة Android / تصدير PDF وعرض محدود لبيانات YAML front matter
* `ميزة` إضافة Footnotes بالتعريف أو ضمن السطر مع روابط رجوع ثنائية الاتجاه بعد التنقية
* `تحسين` تعزيز التحقق من explorer-action v1 Intent و URI والمسارات والروابط والموارد و HTML و Footnotes مع الحفاظ على حد ملف واحد للقراءة فقط
* `تحسين` إضافة Roadmap قابلة للتحديد و 4 لقطات من جهاز حقيقي ببيانات اصطناعية فقط وتوليد README / CHANGELOG قابل لإعادة الإنتاج لعشر لغات
* `تبعية` نقل CommonMark من fork Atlassian 0.9.0 القديم إلى وحدات core والامتدادات الرسمية 0.30.0 من Maven Central مع core library desugaring للتوافق مع API 24

#### v1.0.1

###### 2026/08/08

* `إصلاح` فشل تمكين الملحق في مركز اضافات AutoJs6 بسبب إرجاع الخدمة ارتباطا فارغا (onNullBinding)
* `تحسين` تبسيط اسم الملحق ووصفه وتوحيد صياغة وثائق المستخدم عبر اللغات

#### v1.0.0

###### 2026/08/06

* `ميزة` الإصدار الأول من Markdown Previewer: إجراء `معاينة Markdown` في القائمة الإضافية لمدير ملفات AutoJs6 يعرض مستندا واحدا للقراءة فقط
* `ميزة` التعرف على 10 امتدادات (md / markdown / mdown / mkd / mkdn / mdwn / mdtext / mdtxt / rmd / qmd) إضافة إلى نوعي MIME `text/markdown` و `text/x-markdown`
* `ميزة` عرض الجداول وقوائم المهام والنص المشطوب والروابط التلقائية ومراسي العناوين والصور داخل المستند
* `ميزة` تضمين سمات GitHub (تلقائي / فاتح / داكن) وورق وبني داكن, مع استيراد CSS مخصص وتحديث يدوي ووضع ملء الشاشة
* `ميزة` بناء بيئة معزولة للقراءة فقط عبر التنقية بقائمة سماح وقيود CSP وتعطيل JavaScript والتخزين وترشيح العناوين الخاصة وحدود الإدخال (Markdown 8 MiB و CSS 256 KiB)
* `ميزة` تسجيل خدمة الملحق عبر بروتوكول `org.autojs.plugin.EXPLORER_ACTION` والوصول إلى الملف المحدد ومجلده الأصل عبر content URI مؤقتة يمنحها المضيف
* `ميزة` توطين معلومات الملحق والواجهة والتعليمات والوثائق إلى الصينية المبسطة والصينية التقليدية (هونغ كونغ / تايوان) والإنجليزية والفرنسية والإسبانية واليابانية والكورية والروسية والعربية

##### السجل الكامل

* [CHANGELOG-ar.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/assets/doc/CHANGELOG-ar.md)

******

### البناء

******

```powershell
.\gradlew.bat :app:assembleDebug
```

بناء Release:

```powershell
.\gradlew.bat :app:assembleRelease
```

تأتي معلمات البناء من `version.properties`. الحد الأدنى الحالي لـ SDK هو 24 والـ SDK المستهدف هو 36.

******

### هيكل الموارد

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG-*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

يوطن `strings.xml` معلومات الملحق وواجهة العارض, بينما يوفر `plugin_instruction.md` تعليمات الاستخدام التي يعرضها المضيف. تولد جميع ملفات README و CHANGELOG من مصادر JSON بواسطة `.python/generate_markdown.py`: لتعديل الوثائق, حرر ملفات `lang_*.json` تحت `.readme` و `.changelog` ثم أعد تشغيل السكربت بدلا من تحرير ملفات Markdown المولدة.

******

### الروابط

******

- وثائق AutoJs6: https://docs.autojs6.com
- مواصفة CommonMark: https://commonmark.org
- ملحق HTML Previewer (معاينة ملفات HTML): https://github.com/SuperMonster003/AutoJs6-Plugin-HTML-Previewer
