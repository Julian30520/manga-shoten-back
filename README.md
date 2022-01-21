# Manga Shoten Backend
----

## Controllers resources

### UserController

* **/users/all**

    Type : GET

    Récupère la liste des utilisateurs.

    auth: ROLE_ADMIN

    pas de body

* **/users/username/{username}**

    Type : GET    

    Récupère les informations d'un utilisateur.

    auth: ROLE_USER, ROLE_ADMIN
    
    pas de body

* **/users/{user_id}**

    Type : GET

    Récupère les informations d'un utilisateur.

    auth: ROLE_USER, ROLE_ADMIN

    pas de body

* **/users/{user_id}/tomes**

    Type : GET

    Récupère le liste des tomes de la bibliothèque d'un utilisateur

    auth: ROLE_USER, ROLE_ADMIN

    pas de body

* **/users/{user_id}/delete**

    type: DELETE

    Supprime un utilisateur de la base de données

    auth: ROLE_USER, ROLE_ADMIN

    pas de body

* **/users/{user_id}/{tome_id}**

    type: PATCH, PUT

    Ajoute un tome à la bibliothèque d'un utilisateur

    auth: ROLE_USER, ROLE_ADMIN

    pas de body

* **/users/update**

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
    
* **/users/sign-up**

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

* **/users/sign-in**
    
    auth: tout le monde

    Connecte un utilisateur et lui renvoie un JWT

    body :

    ```json
    {
      "username": "toto",
      "password": "kikoulol"
    }
    ``` 