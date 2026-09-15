<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="markdown-previewer-ic-launcher" border="0" width="128" />
  </p>

  <p>Affiche un aperçu des documents Markdown</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Langues (Languages)

******

Le fichier README.md actuel prend en charge les langues suivantes:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-en.md)
- Français [fr] # actuel
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/.readme/README-ar.md)

******

### Introduction

******

Markdown Previewer est un plugin d'aperçu pour le gestionnaire de fichiers d'AutoJs6. Une fois active, chaque fichier Markdown du gestionnaire de fichiers propose l'action `Prévisualiser Markdown` dans son menu secondaire. Touchez-la pour lire le document mis en forme comme une page web, au lieu d'un bloc de texte source brut.

Le plugin fait une seule chose et la fait en toute sécurité: un rendu en lecture seule. La visionneuse n'exécute jamais de scripts, ne peut lire que les fichiers temporairement autorisés par l'hôte et effectue tout le rendu dans son propre écran. Elle ne modifie pas AutoJs6 et n'a aucun effet sur l'environnement d'exécution des scripts.

******

### Points forts

******

- Lisez les documents Markdown mis en forme directement dans le gestionnaire de fichiers d'AutoJs6, sans exporter de fichiers ni installer de lecteur tiers.
- Tableaux, listes de tâches, texte barré, liens automatiques, ancres de titres et images du document fonctionnent immédiatement et couvrent l'écriture de style GitHub.
- GitHub (Auto), GitHub Light, GitHub Dark, Paper, Sepia. Les menus et dialogues suivent la langue et le mode sombre AutoJs6, tout comme GitHub (Auto) et le thème HTML automatique. Les barres utilisent une couleur représentative des bords de la page avec du texte et des icônes noirs ou blancs contrastés. Pour les dégradés, images et animations, cette couleur reste fixe jusqu'au rechargement pour éviter le scintillement.
- Importez un fichier CSS personnalisé pour créer votre propre style de lecture. Il se superpose au style intégré et peut être effacé d'un geste.
- Mode plein écran immersif avec l'option `Démarrer en mode plein écran`. Le bouton retour quitte d'abord le plein écran au lieu de fermer la page.
- Zoom par pincement, actualisation manuelle, tableaux à défilement horizontal et détection automatique par BOM des encodages UTF-8 / UTF-16 / UTF-32.
- Bac à sable en lecture seule: aucune exécution de JavaScript, aucun cache sur disque et aucun accès aux données autres que les fichiers autorisés par l'hôte.

******

### Captures d'écran

******

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/file-menu-action.png?raw=true" alt="Action du menu de fichier" width="300" />
      <br />
      <sub>Action du menu de fichier</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/viewer.png?raw=true" alt="Visionneuse de document" width="300" />
      <br />
      <sub>Visionneuse de document</sub>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/theme-dialog.png?raw=true" alt="Sélecteur de thème" width="300" />
      <br />
      <sub>Sélecteur de thème</sub>
    </td>
    <td align="center">
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/images/screenshots/fullscreen.png?raw=true" alt="Lecture en plein écran" width="300" />
      <br />
      <sub>Lecture en plein écran</sub>
    </td>
  </tr>
</table>

******

### Installation et utilisation

******

Avant de commencer, vérifiez les prérequis suivants:

```text
host app: AutoJs6 (org.autojs.autojs6)
minimum host build: 5269
minimum android: 7.0 (API 24)
plugin package: io.github.supermonster003.autojs6.plugin.markdownpreviewer
```

Il faut 4 étapes entre l'installation et le premier document affiché:

1. Téléchargez et installez l'APK du plugin. Le plugin n'a pas d'icône de lanceur; après l'installation, il est entièrement géré par AutoJs6.
2. Ouvrez AutoJs6, accédez au `Centre des plugins`, repérez `Aperçu Markdown` et activez-le.
3. Dans le gestionnaire de fichiers d'AutoJs6, localisez un fichier Markdown (par exemple `README.md`) et ouvrez son menu secondaire.
4. Sélectionnez `Prévisualiser Markdown`. Le document s'ouvre mis en forme dans une visionneuse dédiée.

Dans la visionneuse, le menu en haut à droite propose `Actualiser`, `Thème de prévisualisation`, `Importer un CSS personnalisé`, `Mode plein écran` et `Paramètres`, où `Paramètres` contient l'option `Démarrer en mode plein écran`. Les liens http/https du document s'ouvrent dans le navigateur du système, tandis que les liens d'ancres de titres restent dans la visionneuse. Explorer Action v2 prend en charge le bouton principal et le menu contextuel pour un fichier, avec une autorisation temporaire de lecture du document et de son dossier parent. AutoJs6 build 5269 ou ultérieur est requis.

******

### Formats pris en charge

******

Le plugin reconnaît les extensions de fichiers suivantes:

```text
md, markdown, mdown, mkd, mkdn, mdwn, mdtext, mdtxt, rmd, qmd
```

Les fichiers dont l'extension n'est pas listée mais dont le type MIME est `text/markdown` ou `text/x-markdown` peuvent aussi être prévisualisés. Un document ne peut pas dépasser 8 MiB; un fichier trop volumineux produit un message clair plutôt qu'un rendu tronqué.

******

### Questions fréquentes

******

**Le menu du fichier n'affiche pas `Prévisualiser Markdown`?**

Vérifiez dans l'ordre: le code de version d'AutoJs6 est au moins 5269 (la version 6.8.0 ou ultérieure convient); le plugin est activé dans le `Centre des plugins`; l'extension du fichier figure dans la liste prise en charge. Si l'une des trois conditions manque, l'action n'apparaît pas.

**L'ouverture échoue avec `Lecture du fichier Markdown impossible`?**

Causes courantes: le fichier a été déplacé, renommé ou supprimé au moment de l'ouverture; le fichier dépasse 8 MiB; ou l'appel ne provient pas du gestionnaire de fichiers d'AutoJs6. Pour des raisons de sécurité, le plugin rejette les appels de toute autre origine.

**Pourquoi les images du document ne s'affichent-elles pas?**

La visionneuse ne charge que trois types d'images: les images locales référencées par des chemins relatifs dans le dossier du document (sous-dossiers compris), les images en ligne `data:` et les images publiques `https`. Les images `http` en clair et les adresses privées ou réservées sont bloquées par la politique de sécurité.

**Peut-il prévisualiser des fichiers HTML?**

Non. Ce plugin se concentre sur Markdown. L'aperçu HTML est assuré par le plugin distinct HTML Previewer; voir les liens ci-dessous.

**Comment fonctionne le CSS personnalisé?**

Choisissez `Importer un CSS personnalisé` dans le menu et sélectionnez une feuille de style d'au plus 256 KiB. Le thème passe automatiquement à `CSS personnalisé`. La feuille se superpose au style de base: n'écrivez que les règles à remplacer. Choisissez `Effacer le CSS personnalisé` pour revenir à `GitHub (Auto)`.

**Comment fonctionne la coloration syntaxique, et pourquoi Mermaid et les formules mathématiques ne sont-ils pas rendus?**

La coloration syntaxique est produite pendant le rendu Markdown pour les langages reconnus et ne nécessite pas JavaScript. Mermaid et les formules mathématiques dépendent encore de scripts côté client et ne sont donc pas rendus, car la visionneuse maintient JavaScript désactivé.

******

### Sécurité

******

La visionneuse applique le principe du refus par défaut. Toutes les mesures suivantes sont toujours actives et ne peuvent pas être désactivées:

- Le rendu est assaini selon une liste d'autorisation: scripts, formulaires, iframes, gestionnaires d'événements en ligne et autres contenus dangereux sont supprimés, et JavaScript reste désactivé en permanence.
- La WebView fonctionne sans stockage, sans cookies, sans enregistrement de formulaires et sans accès au système de fichiers. Le rendu ne vit qu'en mémoire et disparaît à la fermeture de la page.
- Le plugin lit le fichier choisi et son dossier uniquement via des URI de contenu temporaires accordés par l'hôte. Il ne reçoit aucun chemin du système de fichiers et ne demande aucune permission supplémentaire.
- La CSP et l'interception des requêtes limitent doublement le chargement des ressources: seuls les styles intégrés, les ressources du dossier du document, les images `data:` et les images `https` passent; toute autre requête est rejetée.
- Les images distantes sont filtrées contre les adresses privées et réservées (anti-SSRF) et chargées avec une politique no-referrer; les liens externes ne peuvent s'ouvrir que dans le navigateur du système.
- Les entrées sont bornées: Markdown jusqu'à 8 MiB, CSS personnalisé jusqu'à 256 KiB, avec des longueurs de noms et de chemins également limitées.
- Les ressources HTTPS utilisent des adresses DNS publiques vérifiées et chaque redirection est contrôlée; le réseau direct de WebView est bloqué et seuls GET et HEAD sont autorisés

******

### Interface du plugin (pour les développeurs)

******

L'hôte détecte et invoque le plugin avec les identités suivantes:

```text
service action: org.autojs.plugin.EXPLORER_ACTION
execute action: org.autojs.plugin.EXPLORER_ACTION_EXECUTE
plugin id: markdown-previewer
engine: explorer-action
variant: default
required host build: 5269
```

Explorer Action v2 prend en charge le bouton principal et le menu contextuel pour un fichier, avec une autorisation temporaire de lecture du document et de son dossier parent. AutoJs6 build 5269 ou ultérieur est requis.

******

### Feuille de route

******

Les capacités achevées et les projets à venir sont tenus sous forme de liste cochable dans ROADMAP.md. Les cases non cochées expriment une intention et ne décrivent pas les capacités actuelles.

- [Ouvrir le ROADMAP.md cochable](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/ROADMAP.md)

******

### Historique des versions

******

#### v1.2.2

###### 2026/09/16

* `Amélioration` Après compileSdk, targetSdk passe à 37 (Android 17) ; le comportement du plugin ne dépend pas de la nouvelle cible

#### v1.2.1

###### 2026/09/15

* `Amélioration` compileSdk passe à 37 (Android 17) ; targetSdk reste à 36 jusqu'à la vérification du comportement dépendant de la cible

#### v1.2.0

###### 2026/09/13

* `Fonctionnalité` Historique local accessible depuis l'interface, avec traductions et repli en anglais
* `Correctif` Les ressources HTTPS utilisent des adresses DNS publiques vérifiées et chaque redirection est contrôlée; le réseau direct de WebView est bloqué et seuls GET et HEAD sont autorisés
* `Correctif` Conserver HTTP 206 et les autres codes de succès lors du chargement des ressources distantes
* `Amélioration` Vérification de la signature complète, des APK attendus et de la reproductibilité de la documentation
* `Dépendance` Ajout de OkHttp 4.12.0 pour le chargement contrôlé des ressources HTTPS

##### Historique complet

* [CHANGELOG-fr.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/app/src/main/assets/doc/CHANGELOG-fr.md)

******

### Compilation

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Compilation Release:

```powershell
.\gradlew.bat :app:assembleRelease
```

Les paramètres de compilation proviennent de `version.properties`. Le SDK minimum actuel est 24 et le SDK cible est 36.

******

### Organisation des ressources

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG-*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` localise les informations du plugin et l'interface de la visionneuse, tandis que `plugin_instruction.md` fournit les instructions affichées par l'hôte. Tous les fichiers README et CHANGELOG sont générés depuis des sources JSON par `.python/generate_markdown.py`: pour modifier la documentation, éditez les fichiers `lang_*.json` sous `.readme` et `.changelog` puis relancez le script au lieu de modifier les fichiers Markdown générés.

******

### Liens

******

- Documentation AutoJs6: https://docs.autojs6.com
- Spécification CommonMark: https://commonmark.org
- Plugin HTML Previewer (aperçu des fichiers HTML): https://github.com/SuperMonster003/AutoJs6-Plugin-HTML-Previewer


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer/blob/master/docs/16kb.md)
