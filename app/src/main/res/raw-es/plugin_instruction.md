Use Markdown Previewer desde el gestor de archivos:

1. Instale y active el complemento `Markdown Previewer`.
2. Abra el menú secundario de un archivo Markdown compatible.
3. Seleccione `Vista previa de Markdown`.

El complemento recibe acceso temporal de lectura al archivo seleccionado y a su directorio principal mediante URI de contenido. No recibe una ruta directa del sistema de archivos.

Extensiones compatibles: `md`, `markdown`, `mdown`, `mkd`, `mkdn`, `mdwn`, `mdtext`, `mdtxt`, `rmd`, `qmd`.

El visor admite enlaces automáticos, tablas, tachado, anclas de encabezado, listas de tareas, imágenes del documento, temas integrados, CSS personalizado, actualización y pantalla completa.

La versión 1 solo admite acciones de solo lectura para un archivo en el gestor de archivos.

Explorer Action v2 admite el botón principal y el menú contextual para un archivo, con permisos temporales de lectura del documento y su carpeta. Se requiere AutoJs6 build 5269 o posterior.

Los menús y diálogos siguen el idioma y modo oscuro de AutoJs6, al igual que GitHub (Auto) y el tema HTML automático. Las barras usan un color representativo de los bordes de la página y texto e iconos negros o blancos con contraste. Para degradados, imágenes y animaciones, el color permanece fijo hasta recargar para evitar parpadeos.
