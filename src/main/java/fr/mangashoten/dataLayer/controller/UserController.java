package fr.mangashoten.dataLayer.controller;

import fr.mangashoten.dataLayer.dto.JsonWebToken;
import fr.mangashoten.dataLayer.exception.ExistingUsernameException;
import fr.mangashoten.dataLayer.exception.InvalidCredentialsException;
import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.ArrayList;

@RestController()
@CrossOrigin("*")
@RequestMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value="/all")
    public ArrayList<User> getAllUsers(){
        return userService.getUsers();
    }

    @GetMapping(value="/username/{name}")
    public User getUserByUsername(@PathVariable String name){
        return userService.getUserByUsername(name);
    }

    @GetMapping (value="/{user_id}")
    public User getUserById(@PathVariable Integer user_id){
        return userService.getUserById(user_id);
    }

    @GetMapping(value="/{user_id}/tomes")
    public ArrayList<Tome> getUserTomes(@PathVariable Integer user_id){
        User user = userService.getUserById(user_id);
        return userService.getTomes(user);
    }

//    @PostMapping(value="/add")
//    public ResponseEntity<User> addUser(@RequestBody User userDetails) throws ServerException {
//        User createdUser = userService.createUser(userDetails);
//        if(createdUser == null) throw new ServerException(String.format("Impossible de créer l'utilisateur %s", userDetails.getUsername()));
//        else return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//    }

    @DeleteMapping(value="/{user_id}/delete")
    public ResponseEntity<User> deleteUser(@PathVariable Integer user_id){
        try{
            User userToDelete = userService.getUserById(user_id);
            userService.deleteUser(userToDelete);
            return new ResponseEntity(String.format("{ userId: %d }", user_id), HttpStatus.OK);
//            return new ResponseEntity<>(userToDelete, HttpStatus.OK);
        }
        catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value="/{user_id}/{tome_id}")
    public void addTomeToUserLibrary(@PathVariable Integer user_id, @PathVariable Integer tome_id){

        userService.addTomeToLibrary(user_id, tome_id);

    }

    @PatchMapping(value="/update")
    public void updateUser(@RequestBody User userDetails){
        try{
            userService.updateUser(userDetails);
        }catch(Exception ex){
            System.out.println(String.format("Erreur lors de la mise à jour de l'utilisateur %s %s", userDetails.getFirstName(), userDetails.getLastName()));
        }
    }

    /**
     * Methode pour enregistrer un nouvel utilisateur dans la BD.
     * @param user utiliateur.
     * @return un JWT si la connection est OK sinon une mauvaise réponse
     */
    @PostMapping("/sign-up")
    public ResponseEntity<JsonWebToken> signUp(@RequestBody User user) {
        try {
            return ResponseEntity.ok(new JsonWebToken(userService.signup(user)));
        } catch (ExistingUsernameException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Methode pour se connecter (le user existe déjà).
     * @param user : utilisateur qui doit se connecter.
     * @return un JWT si la connection est OK sinon une mauvaise réponse.
     */
    @PostMapping("/sign-in")
    public ResponseEntity<JsonWebToken> signIn(@RequestBody User user) {
        try {
            // ici on créé un JWT en passant l'identifiant et le mot de passe
            // récupéré de l'objet user passé en paramètre.
            return ResponseEntity.ok(new JsonWebToken(userService.signin(user.getUsername(), user.getPassword())));
        } catch (InvalidCredentialsException ex) {
            // on renvoie une réponse négative
            return ResponseEntity.badRequest().build();
        }
    }

//    @PatchMapping("/change-pass")
//    public ResponseEntity<JsonWebToken> changePassword(@RequestBody User user){
//        try{
//            userService.updateUser(user);
//        }
//        catch(Exception e){
//
//        }
//    }

}
