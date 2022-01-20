package fr.mangashoten.dataLayer.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import fr.mangashoten.dataLayer.dto.Mapper;
import fr.mangashoten.dataLayer.dto.UserDto;
import fr.mangashoten.dataLayer.exception.ExistingUsernameException;
import fr.mangashoten.dataLayer.exception.InvalidCredentialsException;
import fr.mangashoten.dataLayer.exception.UserNotFoundException;
import fr.mangashoten.dataLayer.model.Role;
import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.repository.UserRepository;
import fr.mangashoten.dataLayer.security.JwtTokenProvider;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TomeService tomeService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // permet l'encodage du mot de passe


    @Autowired
    private JwtTokenProvider jwtTokenProvider;	// permet la fourniture du Jeton (Token)

    @Autowired
    private AuthenticationManager authenticationManager; // gestionnaire d'authentification

    private User user;

    private Mapper mapper = new Mapper();

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
    public User getUserByUsername(String username) throws UserNotFoundException {
        var optUser = userRepository.findByUsername(username);
        try{
            return optUser.get();
        }
        catch(NoSuchElementException ex){
            throw new UserNotFoundException(username);
        }
    }

    /**
     * Récupère un utilisateur à partir de son Id dans la base
     * @param id
     * @return
     */
    public User getUserById(Integer id) throws UserNotFoundException {
        var optUser = userRepository.findById(id);
        try{
            return optUser.get();
        }
        catch(NoSuchElementException ex){
            throw new UserNotFoundException(id);
        }
    }

    /**
     * Supprime un utilisateur de la base
     * @param user
     */
    public User deleteUser(User user) {
        userRepository.delete(user);
        return user;
    }

    /**
     * Récupère la liste des tomes possédés par un utilisateur
     * @param user l'utilisateur dont on veut la liste des tomes
     * @return Une liste de tomes
     */
    public ArrayList<Tome> getTomes(User user) throws UserNotFoundException {
        try {
            return new ArrayList<Tome>(user.getTomes());
        }
        catch(NoSuchElementException nseE){
            throw new UserNotFoundException(user.getUserId());
        }
    }

    /**
     * Ajoute un tome dans la librairie d'un utilisateur
     * @param user_id
     * @param tome_id
     */
    public void addTomeToLibrary(Integer user_id, Integer tome_id) throws UserNotFoundException {
        try{
            User user = this.getUserById(user_id);
            user.getTomes().add(tomeService.getTomeById(tome_id));
            userRepository.save(user);
        }
        catch(NoSuchElementException nseE){
            throw new UserNotFoundException(user_id);
        }
    }

    /**
     * Met à jour l'utilisateur dans la base de données
     * @param user
     */
    public void updateUser(User user) throws UserNotFoundException {
        User userToUpdate = this.getUserByUsername(user.getUsername());
        user.setUserId(userToUpdate.getUserId());
        userRepository.save(user);
    }


    public User getModifiedUserFromDto(UserDto updatedPartialUser) throws UserNotFoundException, InvocationTargetException, IllegalAccessException {
        User fullUser = this.getUserById(updatedPartialUser.getId());
        String userPassword = fullUser.getPassword();
        UserDto fullUserDto = mapper.toDto(fullUser);

        ArrayList<Method> getMethods = new ArrayList<>();
        ArrayList<Method> setMethods = new ArrayList<>();
        for (Method method : updatedPartialUser.getClass().getDeclaredMethods()){
            if(method.getName().startsWith("get")){
                getMethods.add(method);
            }
            if(method.getName().startsWith("set")){
                setMethods.add(method);
            }
        }
        getMethods.sort((a, b) -> {return a.getName().compareToIgnoreCase(b.getName());});
        setMethods.sort((a, b) -> {return a.getName().compareToIgnoreCase(b.getName());});
        for(int i = 0; i < getMethods.size(); i++){
            Object dtoInfo = getMethods.get(i).invoke(updatedPartialUser);
            Object fullUserInfo = getMethods.get(i).invoke(fullUserDto);
            if(dtoInfo != null && !dtoInfo.equals(fullUserInfo)){
                setMethods.get(i).invoke(fullUserDto, dtoInfo);
            }
        }
        fullUser = mapper.toUser(fullUserDto);
        fullUser.setPassword(userPassword);

        return fullUser;
    }

    /**
     * Permet de se connecter en encodant le mot de passe avec génération du token.
     */
    public String signin(String username, String password) throws InvalidCredentialsException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).get().getRole());
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException();
        }
    }

    public String signup(User user) throws ExistingUsernameException {
        if (!userRepository.existsByUsername(user.getUsername())) {
            User userToSave = new User(user.getUsername(), user.getMail(), passwordEncoder.encode(user.getPassword()), user.getRole());
            userRepository.save(userToSave);
            return jwtTokenProvider.createToken(user.getUsername(), user.getRole());
        } else {
            throw new ExistingUsernameException();
        }
    }

}
