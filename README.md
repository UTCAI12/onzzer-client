# onzzer-client




## Arborescence Client

```
onzzer-client
├── src
│   ├── main
│   │   ├── java
│   │	│	├── fr.utc.onzzer.client
│   │	│	│	├── data
│   │	│	│	├── communication
│   │	│	│	├── ihmmain
│   │	│	│	├── ihmmusic
│   │	│	│	├── MainClient.java
│   │	├── resources
│   │	│	├── fxml
│   │	│	├── css
│   ├── test
│   │	├── java
│   │	│	├── fr.utc.onzzer.client
│   │	├── resources
├── .gitignore
├── pom.xml
├── README.md
```


## Gestionnaire de dépendances

Pour la gestion de dépendances (JavaFx, onzzer-common, autres librairies), nous proposons l'utilisation du gestionnaire de dépendances **Maven**. Il permettra à l'ensemble des équipes de travailler avec des dépendances de même version, et facilitera leur installation sur les machines de chacun.

Remarque : Le fichier `pom.xml` présent dans les arborescences correspond à l'utilisation de ce gestionnaire.

## Nommage des Branches

3 types de branches:
 - main
 - module : **version-module**
 - fonctionnalité : **version-module/fonctionnalité**

Exemples:

  - Fonctionnalité de lecture de musique pour ihm-music : **2.a-ihm-music/play**
  
  - Fonctionnalité de création de compte ihm-main : **1.a-ihm-main/register**

Le developpement s'effectue uniquement sur une branche **fonctionnalité**.

On ne développe **JAMAIS** sur une branche **module**

Seuls les resps dev ont le droit de merge une branche **version-module/fonctionnalité** dans **version-module**.


