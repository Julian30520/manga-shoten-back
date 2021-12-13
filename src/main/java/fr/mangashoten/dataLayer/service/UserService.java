package fr.mangashoten.dataLayer.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import fr.mangashoten.dataLayer.model.Role;
import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TomeService tomeService;

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
        var user = userRepository.findByUsername(username);
        if(user.isPresent()) return user.get();
        else return null;
    }

    /**
     * Récupère un utilisateur à partir de son Id dans la base
     * @param id
     * @return
     */
    public User getUserById(Integer id){
        var optUser = userRepository.findById(id);
        if(optUser.isPresent()) return optUser.get();
        else return null;
    }

    /**
     * Ajoute un nouvel utilisateur dans la base
     * @param user
     * @return
     */
    public User addUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Supprime un utilisateur de la base
     * @param user
     */
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    /**
     * Récupère la liste des tomes possédés par un utilisateur
     * @param user l'utilisateur dont on veut la liste des tomes
     * @return Une liste de tomes
     */
    public ArrayList<Tome> getTomes(User user){
        return new ArrayList<Tome>(user.getTomes());
    }

    public void addTomeToLibrary(Integer user_id, Integer tome_id){
        User user = this.getUserById(user_id);
        //user.getTomes().add(tomeService.getTomeById(tome_id));
        userRepository.save(user);
    }

}
