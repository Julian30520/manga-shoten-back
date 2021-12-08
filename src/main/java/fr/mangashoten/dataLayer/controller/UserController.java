package fr.mangashoten.dataLayer.controller;

import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // get all users
    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return userService.getUsers();
    }

    // create a user
    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
    // Get one employee
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") final Integer id) {
        Optional<User> user = userService.getUserById(id);
        if(user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

    // update an existing employee
    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable("id") final Integer id, @RequestBody User user) {
        Optional<User> el = userService.getUserById(id);
        if(el.isPresent()) {
            User currentUser = el.get();

            String username = user.getUsername();
            if(username != null) {
                currentUser.setUsername(username);
            }
            String firstName = user.getFirstName();
            if(firstName != null) {
                currentUser.setFirstName(firstName);
            }
            String lastName = user.getLastName();
            if(lastName != null) {
                currentUser.setLastName(lastName);
            }
            Date dateOfBirth = user.getDateOfBirth();
            if(dateOfBirth != null) {
                currentUser.setDateOfBirth(dateOfBirth);
            }
            String mail = user.getMail();
            if(mail != null) {
                currentUser.setMail(mail);
            }
            String password = user.getPassword();
            if(password != null) {
                currentUser.setPassword(password);
            }
            userService.saveUser(currentUser);
            return currentUser;
        } else {
            return null;
        }

    }

    // delete one user
    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable("id") final Integer id) {
        userService.deteleUser(id);
    }
}
