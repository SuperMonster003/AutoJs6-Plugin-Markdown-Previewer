# Historial de versiones

## v1.2.0

###### 2026/09/13

* `Función` Historial de versiones local desde la interfaz con traducciones y alternativa en inglés
* `Corrección` Los recursos HTTPS usan direcciones DNS públicas verificadas y se comprueba cada redirección; se bloquea la red directa de WebView y solo se admiten GET y HEAD
* `Corrección` Conservar HTTP 206 y otros estados correctos al cargar recursos remotos
* `Mejora` Comprobación de la firma completa, los APK esperados y la documentación reproducible de cada versión
* `Dependencia` Añadir OkHttp 4.12.0 para la carga controlada de recursos HTTPS

## v1.1.0

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

## v1.0.1

###### 2026/08/08

* `Corrección` Fallo al activar el complemento en el centro de plugins de AutoJs6 porque el servicio devolvía un enlace vacío (onNullBinding)
* `Mejora` Se simplificaron el nombre y la descripción del complemento y se unificó la redacción de la documentación de usuario en todos los idiomas

## v1.0.0

###### 2026/08/06

* `Función` Primera versión de Markdown Previewer: una acción de menú secundario `Vista previa de Markdown` para el gestor de archivos de AutoJs6 que renderiza un documento en modo de solo lectura
* `Función` Reconoce 10 extensiones (md / markdown / mdown / mkd / mkdn / mdwn / mdtext / mdtxt / rmd / qmd) junto con los tipos MIME `text/markdown` y `text/x-markdown`
* `Función` Renderiza tablas, listas de tareas, tachado, enlaces automáticos, anclas de encabezado e imágenes del documento
* `Función` Incluye los temas GitHub (Auto / claro / oscuro), Papel y Sepia, con importación de CSS personalizado, actualización manual y modo de pantalla completa
* `Función` Construye un entorno aislado de solo lectura con depuración por lista de permitidos, restricciones CSP, JavaScript y almacenamiento desactivados, filtrado de direcciones privadas y límites de entrada (Markdown 8 MiB, CSS 256 KiB)
* `Función` Registra el servicio del complemento mediante el protocolo `org.autojs.plugin.EXPLORER_ACTION` y accede al archivo seleccionado y a su directorio principal mediante URI de contenido temporales concedidos por el anfitrión
* `Función` Localiza la información del complemento, la interfaz, las instrucciones y la documentación en chino simplificado, chino tradicional (Hong Kong / Taiwán), inglés, francés, español, japonés, coreano, ruso y árabe
