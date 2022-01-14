package fr.mangashoten.dataLayer.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import fr.mangashoten.dataLayer.model.Role;
import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.repository.UserRepository;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TomeService tomeService;
    private User user;

    /**
     * Va chercher la liste de utilisateurs
     * @return
     */
    public ArrayList<User> getUsers() {
        Iterable<User> iteUsers = userRepository.findAll();
        ArrayList<User> arrayUsers = new ArrayList<>();
        iteUsers.iterator().forEachRemaining(arrayUsers::add);

        return arrayUsers;
    }

    /**
     * Va chercher un utilisateur à partir de son nom
     * @param username
     * @return
     */
    public User getUserByUsername(String username) {
        var optUser = userRepository.findByUsername(username);
        try{
            return optUser.get();
        }
        catch(NoSuchElementException ex){
            System.out.println("Impossible de trouver l'utilisateur " + username);
            throw ex;
        }
    }

    /**
     * Récupère un utilisateur à partir de son Id dans la base
     * @param id
     * @return
     */
    public User getUserById(Integer id){
        var optUser = userRepository.findById(id);
        try{
            return optUser.get();
        }
        catch(NoSuchElementException ex){
            System.out.println("Impossible de trouver l'utilisateur " + id);
            throw ex;
        }
    }

    /**
     * Ajoute un nouvel utilisateur dans la base
     * @param user
     */
    public User createUser(User user){
        return userRepository.save(user);
    }

    /**
     * Supprime un utilisateur de la base
     * @param user
     */
    public User deleteUser(User user) {
        try {
            userRepository.delete(user);
        }
        catch(Exception e){
            throw e;
        }
        return user;
    }

    /**
     * Récupère la liste des tomes possédés par un utilisateur
     * @param user l'utilisateur dont on veut la liste des tomes
     * @return Une liste de tomes
     */
    public ArrayList<Tome> getTomes(User user){
        return new ArrayList<Tome>(user.getTomes());
    }

    /**
     * Ajoute un tome dans la librairie d'un utilisateur
     * @param user_id
     * @param tome_id
     */
    public void addTomeToLibrary(Integer user_id, Integer tome_id){
        try{
            User user = this.getUserById(user_id);
            user.getTomes().add(tomeService.getTomeById(tome_id));
            userRepository.save(user);
        }
        catch(Exception ex){
            System.out.println("Une erreur est survenue lors de l'ajout du tome dans la bibliothèque.");
            throw ex;
        }
    }

    /**
     * Met à jour l'utilisateur dans la base de données
     * @param user
     */
    public void updateUser(User user){
        User userToUpdate = this.getUserByUsername(user.getUsername());
        user.setUserId(userToUpdate.getUserId());
        userRepository.save(user);
    }

}
