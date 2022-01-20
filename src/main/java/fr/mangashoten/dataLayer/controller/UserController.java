package fr.mangashoten.dataLayer.controller;

import fr.mangashoten.dataLayer.dto.JsonWebToken;
import fr.mangashoten.dataLayer.dto.UserDto;
import fr.mangashoten.dataLayer.exception.ExistingUsernameException;
import fr.mangashoten.dataLayer.exception.InvalidCredentialsException;
import fr.mangashoten.dataLayer.exception.UserNotFoundException;
import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fr.mangashoten.dataLayer.dto.Mapper;

import java.lang.reflect.Method;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController()
@CrossOrigin("*")
@RequestMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    private Mapper mapper = new Mapper();

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @GetMapping(value="/all")
    public ResponseEntity<ArrayList<UserDto>> getAllUsers(){
        ArrayList<UserDto> allUsers;
        try{
            allUsers = new ArrayList<UserDto>(userService.getUsers().stream().map(mapper::toDto).collect(toList()));
            return ResponseEntity.ok(allUsers);
        }catch(Exception e){
            log.error("Erreur à la récupération de la  liste des utilisateurs. detail : {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping(value="/username/{name}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String name){
        try{
            User user = userService.getUserByUsername(name);
            return ResponseEntity.ok().body(mapper.toDto(user));
        }catch(UserNotFoundException unfEx){
            log.error(unfEx.getMessage());
            return ResponseEntity.notFound().build();
        }
        catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping (value="/{user_id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer user_id) throws UserNotFoundException {
        try{
            User user = userService.getUserById(user_id);
            return ResponseEntity.ok().body(mapper.toDto(user));
        }
        catch(UserNotFoundException unfEx){
            log.error(unfEx.getMessage());
            return ResponseEntity.notFound().build();
        }
        catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value="/{user_id}/tomes")
    public ResponseEntity<ArrayList<Tome>> getUserTomes(@PathVariable Integer user_id) {

        try{
            User user = userService.getUserById(user_id);
            return ResponseEntity.ok().body(userService.getTomes(user));
        }
        catch(UserNotFoundException unfEx){
            log.error(unfEx.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping(value="/{user_id}/delete")
    public ResponseEntity<Integer> deleteUser(@PathVariable Integer user_id){
        try{
            User userToDelete = userService.getUserById(user_id);
            userService.deleteUser(userToDelete);
            log.info("Utilisateur {} supprimé.", userToDelete.getUserId());
            return new ResponseEntity<Integer>(userToDelete.getUserId(), HttpStatus.OK);
        }
        catch(UserNotFoundException unfE){
            log.error(unfE.getMessage());
            return ResponseEntity.notFound().build();
        }
        catch(Exception ex){
            log.error("Erreur inconnnue lors de la suppression d'un utilisateur.");
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value="/{user_id}/{tome_id}")
    public ResponseEntity addTomeToUserLibrary(@PathVariable Integer user_id, @PathVariable Integer tome_id) {

        try{
            userService.addTomeToLibrary(user_id, tome_id);
            log.info("Tome {} ajouté à la bibliothèque de l'utilisateur {}", tome_id, user_id);
            return ResponseEntity.ok().build();
        }
        catch(UserNotFoundException unfEx){
            log.error(unfEx.getMessage());
            return ResponseEntity.notFound().build();
        }
        catch(Exception e){
            log.error("Erreur inconnue lors de l'ajout du tome {} à la bibliothèque de l'utilisateur {}", tome_id, user_id);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Resources permettant la mise à jours des information d'un utilisateur
     * @param userDetails
     * @return
     */
    @PatchMapping(value="/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDetails){
        try{
            Long debut = new Date().getTime(); //Sert à timer le processus d'update
            User fullUser = userService.getModifiedUserFromDto(userDetails);

            userService.updateUser(fullUser);

            Long fin = new Date().getTime(); //Fin du timer

            log.info("Utilisateur {} mis à jour en {} ms.", fullUser.getUserId(), fin - debut);
            return ResponseEntity.ok().body(userDetails);
        }
        catch(UserNotFoundException unfE){
            log.error(unfE.getMessage());
            return ResponseEntity.notFound().build();
        }
        catch(Exception ex){
            log.error("Erreur inconnue lors de la mise à jour de l'utilisateur {}. Détails : {}", userDetails.getId(), ex.getMessage());
            return ResponseEntity.badRequest().build();
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
