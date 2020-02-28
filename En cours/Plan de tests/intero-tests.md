# Tests sur l'API QAnswer

## Tests du formulaire

* Test d'identifiant 1
	* Test: Taper une question vide et cliquer sur Valider.
	* Résultat: Afficher un message d'erreur.
* Test d'identifiant 2
	* Test: Taper une question avec 1 seul caractère et cliquer sur Valider.
	* Résultat: Afficher un message d'erreur.
* Test d'identifiant 3
	* Test: Taper une langue vide et cliquer sur Valider.
	* Résultat: Afficher la réponse en Anglais.
* Test d'identifiant 4
	* Test: Taper une langue qui n'existe pas et cliquer sur Valider.
	* Résultat: Afficher la réponse en Anglais.
* Test d'identifiant 5
	* Test: Taper une question et choisir une langue puis cliquer sur valider.
	* Résultat: Afficher une réponse dans la langue choisie avec des images et des liens Wiki si disponibles.
* Test d'identifiant 6
	* Test

QAnswer :
- Envoi des données (question + langue) au clique sur le bouton Valider.
- Affichage du label adapté à chaque rubrique.
- Question incompréhensible : afficher un message d'erreur
- Question avec plus de 50 caractères : afficher un message d'erreur
- Temps de réponse maximale : 5 secondes. Sinon afficher un message d'erreur.

Interface Web :
- Test sur le NavBar (affichage et redirection des liens).
- Test du responsive en taille big, medium et light.
- Affichage de l'icône du NavBar.

Parser :
- Test en cas d'une extension différente de CSV.
- Test en cas d'un fichier CSV de taille lourde.
- Test en cas d'un fichier CSV avec un contenu non conforme.
- Test sur l'encodage (ISO-65001) des fichiers CSV.
- Test en cas d'une extension differente de PDF.
- Test en cas d'un fichier PDF de taille lourde.
- Test en cas d'un fichier PDF avec un contenu non conforme.
- Récupération du contenu du fichier dans un objet.
- Stockage du contenu de l'objet dans la base de données.
- Récupération de la balise HTML.

Relou Insertion Wikidata :
- Tests sur la recherche par entité/propriété :
	- Fonction à tester : ItemDocument laboratoireHC = (ItemDocument) wbdf.getEntityDocument("Q2");
	- Fonction à tester : List\<WbSearchEntitiesResult> entities = wbdf.searchEntities("Saint-Etienne");
	- Fonction à tester : PropertyDocument propertyTravaille = (PropertyDocument) wbdf.getEntityDocument("P163");

Interfcae Web Insertion Wikidata :
- Tests sur l'entité à rajouter (commencer par un Q)
- Tests sur la propriété à rajouter (commencer avec un P)
- Tests sur l'éntité et propriété à interroger