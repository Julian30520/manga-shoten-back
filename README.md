# Manga Shoten Backend

---

## Controllers resources

### UserController

-   **/users/all**

    Type : GET

    Récupère la liste des utilisateurs.

    auth: ROLE_ADMIN

    pas de body

-   **/users/username/{username}**

    Type : GET

    Récupère les informations d'un utilisateur.

    auth: ROLE_USER, ROLE_ADMIN

    pas de body

-   **/users/{user_id}**

    Type : GET

    Récupère les informations d'un utilisateur.

    auth: ROLE_USER, ROLE_ADMIN

    pas de body

-   **/users/{user_id}/tomes**

    Type : GET

    Récupère le liste des tomes de la bibliothèque d'un utilisateur

    auth: ROLE_USER, ROLE_ADMIN

    pas de body

-   **/users/{user_id}/delete**

    type: DELETE

    Supprime un utilisateur de la base de données

    auth: ROLE_USER, ROLE_ADMIN

    pas de body

-   **/users/{user_id}/{tome_id}**

    type: PATCH, PUT

    Ajoute un tome à la bibliothèque d'un utilisateur

    auth: ROLE_USER, ROLE_ADMIN

    pas de body

-   **/users/update**

    type: PATCH, PUT

    Met à jour les informations d'un utilisateur

    auth: ROLE_USER, ROLE_ADMIN

    body :

    ```json
    {
        "id": 12,
        "username": "toto",
        "role": {
            "roleId": 3,
            "codeRole": "ROLE_ADMIN"
        },
        "mail": "toto@prout.com",
        "avatar": null,
        "firstName": "toto",
        "lastName": "prout",
        "dateOfBirth": null
    }
    ```

-   **/users/sign-up**

    type: POST

    Enregistre un nouvel utilisateur dans la base

    auth: tout le monde

    body :

    ```json
    {
        "username": "toto",
        "mail": "toto@prout.com",
        "password": "kikoulol",
        "role": {
            "roleId": 3,
            "codeRole": "ROLE_ADMIN"
        }
    }
    ```

-   **/users/sign-in**

    type: POST

    auth: tout le monde

    Connecte un utilisateur et lui renvoie un JWT

    body :

    ```json
    {
        "username": "toto",
        "password": "kikoulol"
    }
    ```

-   **/users/manga/add/{userId}/{mangaId}**

type: POST

Ajoute un manga à la bibliothèque d'un utilisateur

auth: ROLE_USER, ROLE_ADMIN

-   **/users/tome/add/{userId}/{tomeNumber}/manga/{mangaId}**

type: POST

Ajoute le tome spécifié du manga dans la bibliothèque de l'utilisateur

auth: ROLE_USER, ROLE_ADMIN

-   **/users/tome/remove/{user_id}/{tome_id}**

type: DELETE

Retire un tome de la bibliothèque de l'utilisateur

auth: ROLE_USER, ROLE_ADMIN

-   **/users/manga/remove/{userId}/{mangaId}**

type: DELETE

Retire tous les tomes d'un manga de la bibliothèque d'un utilisateur

auth: ROLE_USER, ROLE_ADMIN

### MangaController

-   **/mannga/all**

type: GET

Obtient la liste de tous les mangas de MangaDex, en gérant la pagination

auth: tout le monde

pas de body

-   **/manga/{manga_id}**

type: GET

Obtient les onformations d'un manga depuis Mangadex à partir de son id (ne procède pas à l'extraction)

auth: tout le monde

pas de body

-   **/manga/title/{manga_name}**

type: GET

Obtient les onformations d'un manga depuis Mangadex à partir de son nom (ne procède pas à l'extraction)

auth: tout le monde

pas de body

### TomeController

-   **/tomes/{tomeNumber}/manga/{mangaId}**

type: GET

Récupère les informations d'un tome à partir de son numéro dans le manga auquel il appartient.

auth: ROLE_USER, ROLE_ADMIN
