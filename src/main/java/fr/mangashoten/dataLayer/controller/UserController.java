package fr.mangashoten.dataLayer.controller;

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

    @GetMapping(value="/{name}")
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

    @PostMapping(value="/add")
    public ResponseEntity<User> addUser(@RequestBody User userDetails) throws ServerException {
        User createdUser = userService.createUser(userDetails);
        if(createdUser == null) throw new ServerException(String.format("Impossible de créer l'utilisateur %s", userDetails.getUsername()));
        else return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @DeleteMapping(value="/{user_id}/delete")
    public void deleteUser(@PathVariable Integer user_id){
        userService.deleteUser(userService.getUserById(user_id));
    }

    @PatchMapping(value="/{user_id}/{tome_id}")
    public void addTomeToUserLibrary(@PathVariable Integer user_id, @PathVariable Integer tome_id){

        userService.addTomeToLibrary(user_id, tome_id);

    }


}
