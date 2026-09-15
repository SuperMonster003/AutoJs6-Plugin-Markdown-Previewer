# Historique des versions

## v1.2.2

###### 2026/09/16

* `Amélioration` Après compileSdk, targetSdk passe à 37 (Android 17) ; le comportement du plugin ne dépend pas de la nouvelle cible

## v1.2.1

###### 2026/09/15

* `Amélioration` compileSdk passe à 37 (Android 17) ; targetSdk reste à 36 jusqu'à la vérification du comportement dépendant de la cible

## v1.2.0

###### 2026/09/13

* `Fonctionnalité` Historique local accessible depuis l'interface, avec traductions et repli en anglais
* `Correctif` Les ressources HTTPS utilisent des adresses DNS publiques vérifiées et chaque redirection est contrôlée; le réseau direct de WebView est bloqué et seuls GET et HEAD sont autorisés
* `Correctif` Conserver HTTP 206 et les autres codes de succès lors du chargement des ressources distantes
* `Amélioration` Vérification de la signature complète, des APK attendus et de la reproductibilité de la documentation
* `Dépendance` Ajout de OkHttp 4.12.0 pour le chargement contrôlé des ressources HTTPS

## v1.1.0

###### 2026/09/11

* `Fonctionnalité` Ajout du plan du document, de la recherche dans la page, du zoom de texte persistant et de la coloration syntaxique au rendu sans activer JavaScript
* `Fonctionnalité` Ajout de la navigation sécurisée vers les documents Markdown relatifs dans le répertoire autorisé par l'hôte, avec historique interne et gestion des ancres
* `Fonctionnalité` Ajout de l'impression Android / export PDF et de l'affichage borné du front matter YAML
* `Fonctionnalité` Ajout des notes de bas de page définies ou en ligne avec liens de retour bidirectionnels nettoyés
* `Correctif` Correction du rejet du protocole du bouton principal et des plantages des paramètres; apparence AutoJs6, couleurs des barres et contrôles monochromes synchronisés
* `Amélioration` Renforcement de la validation des intents explorer-action v1, URI, chemins, liens, ressources, HTML et notes de bas de page tout en conservant la limite de lecture seule sur un fichier
* `Amélioration` Ajout d'une Roadmap à cocher, de 4 captures réelles sur appareil utilisant uniquement des données synthétiques et de la génération reproductible README / CHANGELOG pour 10 langues
* `Amélioration` La vérification de compilation rejette les dépendances natives involontaires et produit un rapport JSON
* `Dépendance` Migration de l'ancien fork Atlassian CommonMark 0.9.0 vers les modules officiels core et extensions 0.30.0 de Maven Central, avec core library desugaring pour l'API 24

## v1.0.1

###### 2026/08/08

* `Correctif` Échec de l'activation du plugin dans le centre des plugins d'AutoJs6 car le service renvoyait une liaison vide (onNullBinding)
* `Amélioration` Simplification du nom et de la description du plugin et harmonisation de la documentation utilisateur entre les langues

## v1.0.0

###### 2026/08/06

* `Fonctionnalité` Première version de Markdown Previewer: une action de menu secondaire `Prévisualiser Markdown` pour le gestionnaire de fichiers d'AutoJs6 qui affiche un document en lecture seule
* `Fonctionnalité` Reconnaissance de 10 extensions (md / markdown / mdown / mkd / mkdn / mdwn / mdtext / mdtxt / rmd / qmd) ainsi que des types MIME `text/markdown` et `text/x-markdown`
* `Fonctionnalité` Rendu des tableaux, listes de tâches, texte barré, liens automatiques, ancres de titres et images du document
* `Fonctionnalité` Thèmes GitHub (Auto / clair / sombre), Papier et Sépia, avec import de CSS personnalisé, actualisation manuelle et mode plein écran
* `Fonctionnalité` Bac à sable en lecture seule avec assainissement par liste d'autorisation, contraintes CSP, JavaScript et stockage désactivés, filtrage des adresses privées et limites d'entrée (Markdown 8 MiB, CSS 256 KiB)
* `Fonctionnalité` Enregistrement du service du plugin via le protocole `org.autojs.plugin.EXPLORER_ACTION` et accès au fichier choisi et à son dossier parent par des URI de contenu temporaires accordés par l'hôte
* `Fonctionnalité` Localisation des informations du plugin, de l'interface, des instructions et de la documentation en chinois simplifié, chinois traditionnel (Hong Kong / Taïwan), anglais, français, espagnol, japonais, coréen, russe et arabe
