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
│   │	│	│	├── hmi
│   │	│	│	│	├── main
│   │	│	│	│	├── music
│   │	│	│	├── MainClient.java
│   │	├── resources
│   │	│	├── fxml
│   │	│	├── css
│   │	│	├── img
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
