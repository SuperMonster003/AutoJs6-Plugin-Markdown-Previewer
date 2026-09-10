Utilisez Markdown Previewer depuis le gestionnaire de fichiers:

1. Installez et activez le plugin `Markdown Previewer`.
2. Ouvrez le menu secondaire d'un fichier Markdown pris en charge.
3. Sélectionnez `Prévisualiser Markdown`.

Le plugin reçoit un accès temporaire en lecture au fichier sélectionné et à son dossier parent par des URI de contenu. Il ne reçoit aucun chemin brut du système de fichiers.

Extensions prises en charge: `md`, `markdown`, `mdown`, `mkd`, `mkdn`, `mdwn`, `mdtext`, `mdtxt`, `rmd`, `qmd`.

La visionneuse prend en charge les liens automatiques, les tableaux, le texte barré, les ancres de titres, les listes de tâches, les images du document, les thèmes intégrés, le CSS personnalisé, l'actualisation et le plein écran.

La version 1 prend uniquement en charge les actions de lecture seule sur un fichier dans le gestionnaire de fichiers.

Explorer Action v2 prend en charge le bouton principal et le menu contextuel pour un fichier, avec une autorisation temporaire de lecture du document et de son dossier parent. AutoJs6 build 5269 ou ultérieur est requis.

Les menus et dialogues suivent la langue et le mode sombre AutoJs6, tout comme GitHub (Auto) et le thème HTML automatique. Les barres utilisent une couleur représentative des bords de la page avec du texte et des icônes noirs ou blancs contrastés. Pour les dégradés, images et animations, cette couleur reste fixe jusqu'au rechargement pour éviter le scintillement.
