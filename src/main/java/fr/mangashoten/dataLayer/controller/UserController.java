package fr.mangashoten.dataLayer.controller;

import fr.mangashoten.dataLayer.dto.JsonWebToken;
import fr.mangashoten.dataLayer.dto.UserDto;
import fr.mangashoten.dataLayer.exception.*;
import fr.mangashoten.dataLayer.model.Manga;
import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.service.MangaService;
import fr.mangashoten.dataLayer.service.TomeService;
import fr.mangashoten.dataLayer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import fr.mangashoten.dataLayer.dto.Mapper;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController()
@CrossOrigin("*")
@RequestMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MangaService mangaService;
    @Autowired
    private TomeService tomeService;

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


    /**
     * Resources permettant la mise à jours des information d'un utilisateur
     * @param userDetails
     * @return
     */
    @PatchMapping(value="/update")
    @PutMapping(value="/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDetails){
        try{
            Long debut = new Date().getTime(); //Sert à timer le processus d'update
            User fullUser = userService.getModifiedUserFromDto(userDetails);

            userService.updateUser(fullUser);

            Long fin = new Date().getTime(); //Fin du timer

            log.info("Utilisateur {} mis à jour en {} ms.", fullUser.getUserId(), fin - debut);
            return ResponseEntity.ok().body(userDetails);
        }
        catch(UsernameNotFoundException | UserNotFoundException unnfE){
            log.error(unnfE.getMessage());
            return ResponseEntity.notFound().build();
        } catch(Exception ex){
            log.error("Erreur inconnue lors de la mise à jour de l'utilisateur {}. Détails : {}", userDetails.getId(), ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Methode pour enregistrer un nouvel utilisateur dans la BD.
     * @param user utiliateur.
     * @return un User si la connection est OK sinon une mauvaise réponse
     */
    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUp(@RequestBody User user) {
        try {
            return ResponseEntity.ok(this.mapper.toDto(userService.signup(user)));
        }
        catch (ExistingUsernameOrMailException ex) {
            log.warn(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
        catch(Exception e){
            log.error(e.getMessage());
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

    /**
     * Ajoute un manga dans la bibliothèque d'un utilisateur
     * @param userId
     * @param mangaId
     * @return
     */
    @PostMapping("/manga/add/{userId}/{mangaId}")
    public ResponseEntity<Manga> addMangaToLibrary(@PathVariable int userId, @PathVariable String mangaId){
        try{
            this.userService.addMangaToLibrary(userService.getUserById(userId).getUserId(), mangaId);
            return ResponseEntity.ok(mangaService.getMangaById(mangaId));
        }
        catch(UserNotFoundException e){
            log.error("L'utilisateur {} introuvable.", userId);
            return ResponseEntity.notFound().build();
        }
        catch(Exception e){
            log.error("Une erreur est survenue lors de l'ajout du manga {} à la bibliothèque de l'utilisateur {}", mangaId, userId);
            return ResponseEntity.badRequest().build();
        }
    }



    /**
     * Ajoute un tome à la librairie d'un utilisateur
     * @param tomeNumber
     * @param mangaId
     * @param userId
     * @return
     */
    @PostMapping(value="/tome/add/{userId}/{tomeNumber}/manga/{mangaId}")
    public ResponseEntity<Tome> addTomeToUserLibraryFromTomeNumberInManga(@PathVariable int tomeNumber, @PathVariable String mangaId, @PathVariable int userId) {
        try{
            Tome tomeToAdd = tomeService.getTomeByMangaIdAndNumber(mangaId, tomeNumber);
            userService.addTomeToLibrary(userId, tomeToAdd.getTomeId());
            log.info("Tome {} ajouté à la bibliothèque de l'utilisateur {}", tomeToAdd.getTomeId(), userId);
            return ResponseEntity.ok(tomeToAdd);
        }
        catch(UserNotFoundException | TomeNotFoundException | MangaNotFoundException e){
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        catch(Exception e){
            log.error("Erreur inconnue lors de l'ajout du tome n°{} du manga {} à la bibliothèque de l'utilisateur {}", tomeNumber, mangaId, userId);
            return ResponseEntity.badRequest().build();
        }
    }


    /**
     * Retire un tome de la bilibothèque de l'utilisateur
     * @param user_id
     * @param tome_id
     * @return
     */
    @DeleteMapping(value="/tome/remove/{user_id}/{tome_id}")
    public ResponseEntity removeTomeFromUserLibrary(@PathVariable int user_id, @PathVariable int tome_id){
        try{
            userService.removeTomeFromLibrary(user_id, tome_id);
            log.info("Tome {} retiré de la bibliothèque de l'utilisateur {}", tome_id, user_id);
            return ResponseEntity.ok().build();
        }
        catch(UserNotFoundException | TomeNotFoundException e){
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        catch(Exception e){
            log.error("Erreur inconnue lors du retrait du tome {} de la bibliothèque de l'utilisateur {}", tome_id, user_id);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value="/manga/remove/{userId}/{mangaId}")
    public ResponseEntity removeMangaFormUserLibrary(@PathVariable int userId, @PathVariable String mangaId){
        try{
            userService.removeMangaFromLibrary(mangaId, userId);
            log.info("Manga {} retiré de la bibliothèque de l'utilisateur {}", mangaId, userId);
            return ResponseEntity.ok().build();
        }
        catch(MangaNotFoundException | UserNotFoundException e){
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        catch(HttpClientErrorException e){
            log.error("Problème de liaison avec MangeDex concernant le manga : {}", mangaId);
            return ResponseEntity.badRequest().build();
        }
        catch(Exception e){
            log.error("Erreur inconnue lors du retrait du manga {} de la bibliothèque de l'utilisateur {}", mangaId, userId);
            return ResponseEntity.badRequest().build();
        }
    }
}
