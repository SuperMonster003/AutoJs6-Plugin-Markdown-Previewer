Markdown Previewer muestra documentos Markdown con formato en AutoJs6, con tablas, listas de tareas, notas al pie y resaltado de código, además de un esquema del documento, búsqueda en la página, varios temas y exportación a PDF. La vista previa solo lee el archivo y nunca modifica el original.

### Primeros pasos

1. Comprueba que este plugin esté activado en el "Centro de plugins" de AutoJs6.
2. Abre el gestor de archivos de AutoJs6 y busca el archivo Markdown.
3. Elige "Vista previa de Markdown" en el menú del archivo (en AutoJs6 6.8.1 y posteriores está en el menú de primer nivel "Markdown Previewer").
4. Usa el menú de la esquina superior derecha de la vista previa para buscar, ver el esquema, cambiar de tema, exportar a PDF, abrir la "Configuración" y más.

### Funciones principales

- **Buscar en la página**: resalta las coincidencias del texto que escribes y permite saltar entre ellas.
- **Esquema del documento**: muestra los encabezados del documento por niveles; toca uno para ir a él.
- **Tema de vista previa**: en "Tema de vista previa", elige "GitHub (Auto)", "GitHub claro", "GitHub oscuro", "Papel" o "Sepia"; "GitHub (Auto)" sigue el modo oscuro de AutoJs6.
- **CSS personalizado**: tras elegir una hoja de estilos de hasta 256 KiB con "Importar CSS personalizado", la vista previa pasa al tema "CSS personalizado"; elige "Borrar CSS personalizado" para volver a "GitHub (Auto)".
- **Impresión y exportación a PDF**: "Imprimir / exportar PDF" abre la pantalla de impresión del sistema, donde puedes imprimir o guardar como PDF; el plugin no necesita permiso de almacenamiento.
- **Sintaxis ampliada**: admite tablas, tachado, listas de tareas, notas al pie, enlaces automáticos y resaltado de código; los metadatos YAML al principio del documento se muestran en un bloque desplegable "Metadatos YAML".
- **Enlaces e imágenes**: los enlaces a archivos Markdown de la misma carpeta o de sus subcarpetas se abren en la vista previa, y los enlaces web en el navegador; se muestran las imágenes de la carpeta del documento y las imágenes HTTPS.
- **Tamaño de fuente y pantalla completa**: ajusta el "Tamaño de fuente" en la "Configuración" y, si quieres, activa "Iniciar en modo de pantalla completa"; el "Modo de pantalla completa" oculta la barra de herramientas y las barras del sistema, y se sale con la tecla Atrás.

### Requisitos

- Android 7.0 o posterior.
- AutoJs6 6.8.0 o posterior.
- No necesita permiso de almacenamiento; el plugin solo lee el archivo seleccionado y su carpeta, y usa la red únicamente para cargar las imágenes HTTPS del documento.

### Preguntas frecuentes

**Por qué falta "Vista previa de Markdown" en el menú del archivo**

El plugin solo procesa archivos con las extensiones md, markdown, mdown, mkd, mkdn, mdwn, mdtext, mdtxt, rmd, qmd. Comprueba la extensión del archivo y que el plugin esté activado en el "Centro de plugins".

**Por qué faltan algunas imágenes**

Las imágenes locales deben estar en la carpeta del archivo Markdown o en una de sus subcarpetas. Las imágenes de red deben usar direcciones HTTPS públicas; las direcciones HTTP y las de la red local no se cargan.

**Por qué un archivo supera el límite de vista previa**

Los archivos Markdown de más de 8 MiB no se previsualizan. Divide el documento en varios archivos más pequeños enlazados entre sí.

**Cómo previsualizar archivos HTML**

Este plugin solo previsualiza documentos Markdown. Para archivos HTML y MHTML, usa el plugin [HTML Previewer](https://github.com/SuperMonster003/AutoJs6-Plugin-HTML-Previewer).

Más detalles en la [página del proyecto](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer).
