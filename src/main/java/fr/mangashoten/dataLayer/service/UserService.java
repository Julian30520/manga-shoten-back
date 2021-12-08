package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(final Integer id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public void deteleUser(final Integer id) {
        userRepository.deleteById(id);
    }
}
