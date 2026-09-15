<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="markdown-previewer-ic-launcher" border="0" width="128" />
  </p>

  <p>Previsualiza documentos Markdown</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Idiomas (Languages)

******

El README.md actual admite los siguientes idiomas:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-fr.md)
- Español [es] # actual
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ar.md)

******

### Introducción

******

Markdown Previewer es un complemento de vista previa para el gestor de archivos de AutoJs6. Una vez activado, cada archivo Markdown del gestor de archivos muestra la acción `Vista previa de Markdown` en su menú secundario. Tóquela para leer el documento con formato, como una página web, en lugar de un bloque de texto fuente.

El complemento hace una sola cosa y la hace con seguridad: renderizado de solo lectura. El visor nunca ejecuta scripts, solo puede leer los archivos autorizados temporalmente por el anfitrión y realiza todo el renderizado en su propia pantalla. No modifica AutoJs6 ni afecta al entorno de ejecución de scripts.

******

### Puntos destacados

******

- Lea documentos Markdown con formato directamente en el gestor de archivos de AutoJs6, sin exportar archivos ni instalar lectores de terceros.
- Tablas, listas de tareas, tachado, enlaces automáticos, anclas de encabezado e imágenes del documento funcionan de inmediato y cubren la escritura al estilo de GitHub.
- GitHub (Auto), GitHub Light, GitHub Dark, Paper, Sepia. Los menús y diálogos siguen el idioma y modo oscuro de AutoJs6, al igual que GitHub (Auto) y el tema HTML automático. Las barras usan un color representativo de los bordes de la página y texto e iconos negros o blancos con contraste. Para degradados, imágenes y animaciones, el color permanece fijo hasta recargar para evitar parpadeos.
- Importe un archivo CSS personalizado para crear su propio estilo de lectura. Se superpone al estilo integrado y puede borrarse con un toque.
- Modo de pantalla completa inmersivo con la opción `Iniciar en modo de pantalla completa`. El botón atrás sale primero de la pantalla completa en lugar de cerrar la página.
- Zoom con dos dedos, actualización manual, tablas con desplazamiento horizontal y detección automática por BOM de las codificaciones UTF-8 / UTF-16 / UTF-32.
- Un entorno aislado de solo lectura: sin ejecución de JavaScript, sin cachés en disco y sin acceso a datos distintos de los archivos autorizados por el anfitrión.

******

### Capturas de pantalla

******

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/file-menu-action.png?raw=true" alt="Acción del menú de archivo" width="300" />
      <br />
      <sub>Acción del menú de archivo</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/viewer.png?raw=true" alt="Visor de documentos" width="300" />
      <br />
      <sub>Visor de documentos</sub>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/theme-dialog.png?raw=true" alt="Selector de tema" width="300" />
      <br />
      <sub>Selector de tema</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/fullscreen.png?raw=true" alt="Lectura a pantalla completa" width="300" />
      <br />
      <sub>Lectura a pantalla completa</sub>
    </td>
  </tr>
</table>

******

### Instalación y uso

******

Antes de empezar, confirme los siguientes requisitos:

```text
host app: AutoJs6 (org.autojs.autojs6)
minimum host build: 5269
minimum android: 7.0 (API 24)
plugin package: io.github.supermonster003.autojs6.plugin.markdownpreviewer
```

Desde la instalación hasta el primer documento renderizado hay 4 pasos:

1. Descargue e instale el APK del complemento. El complemento no tiene icono de inicio; tras la instalación queda gestionado por completo por AutoJs6.
2. Abra AutoJs6, entre en el `Centro de plugins`, localice `Vista previa de Markdown` y actívelo.
3. En el gestor de archivos de AutoJs6, localice cualquier archivo Markdown (por ejemplo `README.md`) y abra su menú secundario.
4. Seleccione `Vista previa de Markdown`. El documento se abre renderizado en un visor dedicado.

Dentro del visor, el menú superior derecho ofrece `Actualizar`, `Tema de vista previa`, `Importar CSS personalizado`, `Modo de pantalla completa` y `Configuración`, donde `Configuración` incluye el interruptor `Iniciar en modo de pantalla completa`. Los enlaces http/https del documento se abren en el navegador del sistema, mientras que los enlaces de anclas de encabezado saltan dentro del visor. Explorer Action v2 admite el botón principal y el menú contextual para un archivo, con permisos temporales de lectura del documento y su carpeta. Se requiere AutoJs6 build 5269 o posterior.

******

### Formatos compatibles

******

El complemento reconoce las siguientes extensiones de archivo:

```text
md, markdown, mdown, mkd, mkdn, mdwn, mdtext, mdtxt, rmd, qmd
```

Los archivos cuya extensión no aparece en la lista pero cuyo tipo MIME es `text/markdown` o `text/x-markdown` también pueden previsualizarse. Un documento puede ocupar como máximo 8 MiB; los archivos que superan el límite generan un mensaje claro en lugar de un renderizado truncado.

******

### Preguntas frecuentes

******

**El menú del archivo no muestra `Vista previa de Markdown`?**

Compruebe en orden: que el código de versión de AutoJs6 sea al menos 5269 (la versión 6.8.0 o posterior es válida); que el complemento esté activado en el `Centro de plugins`; y que la extensión del archivo figure en la lista compatible. Si falla cualquiera de las tres condiciones, la acción no aparece.

**Al abrir aparece `No se puede leer el archivo Markdown`?**

Causas habituales: el archivo se movió, se renombró o se eliminó en el momento de abrirlo; el archivo supera 8 MiB; o la llamada no procede del gestor de archivos de AutoJs6. Por motivos de seguridad, el complemento rechaza las invocaciones de cualquier otro origen.

**Por qué no se muestran las imágenes del documento?**

El visor solo carga tres tipos de imágenes: imágenes locales referenciadas con rutas relativas dentro del directorio del documento (incluidos los subdirectorios), imágenes en línea `data:` e imágenes públicas `https`. Las imágenes `http` sin cifrar y las direcciones privadas o reservadas quedan bloqueadas por la política de seguridad.

**Puede previsualizar archivos HTML?**

No. Este complemento se centra en Markdown. La vista previa de HTML la ofrece el complemento independiente HTML Previewer; consulte los enlaces más abajo.

**Cómo funciona el CSS personalizado?**

Elija `Importar CSS personalizado` en el menú y seleccione una hoja de estilos de como máximo 256 KiB. El tema cambia automáticamente a `CSS personalizado`. La hoja se superpone al estilo base, así que solo debe escribir las reglas que quiera sobrescribir. Elija `Borrar CSS personalizado` para volver a `GitHub (Auto)`.

**Cómo funciona el resaltado de sintaxis y por qué no se renderizan Mermaid ni las fórmulas matemáticas?**

El resaltado de sintaxis se genera durante el renderizado de Markdown para los lenguajes reconocidos y no requiere JavaScript. Mermaid y las fórmulas matemáticas aún dependen de scripts del cliente, por lo que no se renderizan porque el visor mantiene JavaScript desactivado.

******

### Seguridad

******

El visor se construye sobre el principio de denegación por defecto. Todas las medidas siguientes están siempre activas y no pueden desactivarse:

- La salida renderizada se depura con una lista de permitidos: scripts, formularios, iframes, controladores de eventos en línea y otros contenidos peligrosos se eliminan, y JavaScript permanece desactivado en todo momento.
- El WebView funciona sin almacenamiento, sin cookies, sin guardado de formularios y sin acceso al sistema de archivos. El resultado renderizado vive solo en memoria y se destruye al cerrar la página.
- El complemento lee el archivo seleccionado y su directorio únicamente mediante URI de contenido temporales concedidos por el anfitrión. No recibe rutas del sistema de archivos ni solicita permisos adicionales.
- La CSP y la interceptación de solicitudes restringen doblemente la carga de recursos: solo pasan las hojas de estilo integradas, los recursos del directorio del documento, las imágenes `data:` y las imágenes `https`; cualquier otra solicitud se rechaza.
- Las imágenes remotas se filtran contra direcciones privadas y reservadas (anti-SSRF) y se cargan con una política no-referrer; los enlaces externos solo pueden abrirse en el navegador del sistema.
- Las entradas están acotadas: Markdown hasta 8 MiB, CSS personalizado hasta 256 KiB, con longitudes de nombres y rutas igualmente limitadas.
- Los recursos HTTPS usan direcciones DNS públicas verificadas y se comprueba cada redirección; se bloquea la red directa de WebView y solo se admiten GET y HEAD

******

### Interfaz del complemento (para desarrolladores)

******

El anfitrión descubre e invoca el complemento con las siguientes identidades:

```text
service action: org.autojs.plugin.EXPLORER_ACTION
execute action: org.autojs.plugin.EXPLORER_ACTION_EXECUTE
plugin id: markdown-previewer
engine: explorer-action
variant: default
required host build: 5269
```

Explorer Action v2 admite el botón principal y el menú contextual para un archivo, con permisos temporales de lectura del documento y su carpeta. Se requiere AutoJs6 build 5269 o posterior.

******

### Hoja de ruta

******

Las capacidades completadas y los planes futuros se mantienen como una lista verificable en ROADMAP.md. Los elementos sin marcar expresan una intención y no describen capacidades actuales.

- [Abrir el ROADMAP.md verificable](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/ROADMAP.md)

******

### Historial de versiones

******

#### v1.2.1

###### 2026/09/15

* `Mejora` compileSdk sube a 37 (Android 17); targetSdk se mantiene en 36 hasta verificar el comportamiento que depende del objetivo

#### v1.2.0

###### 2026/09/13

* `Función` Historial de versiones local desde la interfaz con traducciones y alternativa en inglés
* `Corrección` Los recursos HTTPS usan direcciones DNS públicas verificadas y se comprueba cada redirección; se bloquea la red directa de WebView y solo se admiten GET y HEAD
* `Corrección` Conservar HTTP 206 y otros estados correctos al cargar recursos remotos
* `Mejora` Comprobación de la firma completa, los APK esperados y la documentación reproducible de cada versión
* `Dependencia` Añadir OkHttp 4.12.0 para la carga controlada de recursos HTTPS

#### v1.1.0

###### 2026/09/11

* `Función` Se añadieron el esquema del documento, la búsqueda en la página, el zoom de texto persistente y el resaltado de sintaxis durante el renderizado sin habilitar JavaScript
* `Función` Se añadió la navegación segura a documentos Markdown relativos dentro del directorio autorizado por el host, con historial interno y manejo de anclas
* `Función` Se añadieron la impresión de Android / exportación a PDF y la visualización acotada de front matter YAML
* `Función` Se añadieron notas al pie definidas y en línea con enlaces de retorno bidireccionales saneados
* `Corrección` Corregidos el rechazo de protocolo del botón principal y los cierres de ajustes; sincronizados la apariencia de AutoJs6, las barras y los controles monocromos
* `Mejora` Se reforzó la validación de intents explorer-action v1, URI, rutas, enlaces, recursos, HTML y notas al pie, manteniendo el límite de solo lectura de un archivo
* `Mejora` Se añadieron una Roadmap verificable, 4 capturas reales con datos sintéticos y generación reproducible de README / CHANGELOG para 10 idiomas
* `Mejora` La verificación de compilación rechaza dependencias nativas accidentales y genera un informe JSON
* `Dependencia` Se migró CommonMark del fork heredado de Atlassian 0.9.0 a los módulos oficiales core y extensiones 0.30.0 de Maven Central, con core library desugaring para API 24

##### Historial completo

* [CHANGELOG-es.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/assets/doc/CHANGELOG-es.md)

******

### Compilación

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Compilación Release:

```powershell
.\gradlew.bat :app:assembleRelease
```

Los parámetros de compilación provienen de `version.properties`. El SDK mínimo actual es 24 y el SDK de destino es 36.

******

### Estructura de recursos

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG-*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` localiza la información del complemento y la interfaz del visor, mientras que `plugin_instruction.md` proporciona las instrucciones mostradas por el anfitrión. Todos los archivos README y CHANGELOG se generan desde fuentes JSON con `.python/generate_markdown.py`: para modificar la documentación, edite los archivos `lang_*.json` de `.readme` y `.changelog` y vuelva a ejecutar el script en lugar de editar los archivos Markdown generados.

******

### Enlaces

******

- Documentación de AutoJs6: https://docs.autojs6.com
- Especificación CommonMark: https://commonmark.org
- Complemento HTML Previewer (vista previa de archivos HTML): https://github.com/SuperMonster003/AutoJs6-Plugin-HTML-Previewer


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/16kb.md)
