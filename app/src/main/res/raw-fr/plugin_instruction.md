Markdown Previewer affiche les documents Markdown mis en forme dans AutoJs6, avec tableaux, listes de tâches, notes de bas de page et coloration du code, ainsi qu'un plan du document, la recherche dans la page, plusieurs thèmes et l'export PDF. L'aperçu se contente de lire le fichier et ne modifie jamais l'original.

### Prise en main

1. Vérifiez que ce plugin est activé dans le "Centre des plugins" d'AutoJs6.
2. Ouvrez le gestionnaire de fichiers d'AutoJs6 et repérez le fichier Markdown.
3. Choisissez "Prévisualiser Markdown" dans le menu du fichier (dans AutoJs6 6.8.1 et versions ultérieures, il se trouve dans le menu de premier niveau "Markdown Previewer").
4. Le menu en haut à droite de l'aperçu donne accès à la recherche, au plan, aux thèmes, à l'export PDF, aux "Paramètres", etc.

### Fonctionnalités principales

- **Rechercher dans la page**: surligne les correspondances du texte saisi et permet de passer de l'une à l'autre.
- **Plan du document**: liste les titres du document par niveau; touchez-en un pour y accéder.
- **Thème de prévisualisation**: dans "Thème de prévisualisation", choisissez "GitHub (Auto)", "GitHub clair", "GitHub sombre", "Papier" ou "Sépia"; "GitHub (Auto)" suit le mode sombre d'AutoJs6.
- **CSS personnalisé**: après avoir choisi une feuille de style de 256 KiB au plus avec "Importer un CSS personnalisé", l'aperçu passe au thème "CSS personnalisé"; choisissez "Effacer le CSS personnalisé" pour revenir à "GitHub (Auto)".
- **Impression et export PDF**: "Imprimer / exporter en PDF" ouvre l'écran d'impression du système, où vous pouvez imprimer ou enregistrer en PDF; le plugin n'a besoin d'aucune autorisation de stockage.
- **Syntaxe étendue**: tableaux, texte barré, listes de tâches, notes de bas de page, liens automatiques et coloration du code sont pris en charge; les métadonnées YAML en tête de document s'affichent dans un bloc dépliable "Métadonnées YAML".
- **Liens et images**: les liens vers des fichiers Markdown du même dossier ou de ses sous-dossiers s'ouvrent dans l'aperçu, les liens web dans le navigateur; les images du dossier du document et les images HTTPS sont affichées.
- **Taille de la police et plein écran**: réglez la "Taille de la police" dans les "Paramètres" et activez au besoin "Démarrer en mode plein écran"; le "Mode plein écran" masque la barre d'outils et les barres système, et la touche Retour permet d'en sortir.

### Prérequis

- Android 7.0 ou version ultérieure.
- AutoJs6 6.8.0 ou version ultérieure.
- Aucune autorisation de stockage n'est nécessaire; le plugin lit uniquement le fichier sélectionné et son dossier, et n'utilise le réseau que pour charger les images HTTPS du document.

### Questions fréquentes

**Pourquoi "Prévisualiser Markdown" n'apparaît pas dans le menu du fichier**

Le plugin ne traite que les fichiers portant les extensions md, markdown, mdown, mkd, mkdn, mdwn, mdtext, mdtxt, rmd, qmd. Vérifiez l'extension du fichier et assurez-vous que le plugin est activé dans le "Centre des plugins".

**Pourquoi certaines images ne s'affichent pas**

Les images locales doivent se trouver dans le dossier du fichier Markdown ou dans l'un de ses sous-dossiers. Les images réseau doivent utiliser des adresses HTTPS publiques; les adresses HTTP et celles du réseau local ne sont pas chargées.

**Pourquoi un fichier dépasse la limite de prévisualisation**

Les fichiers Markdown de plus de 8 MiB ne sont pas prévisualisés. Découpez le document en quelques fichiers plus petits reliés par des liens.

**Comment prévisualiser des fichiers HTML**

Ce plugin ne prévisualise que les documents Markdown. Pour les fichiers HTML et MHTML, utilisez le plugin [HTML Previewer](https://github.com/SuperMonster003/AutoJs6-Plugin-HTML-Previewer).

Plus de détails sur la [page du projet](https://github.com/SuperMonster003/AutoJs6-Plugin-Markdown-Previewer).
