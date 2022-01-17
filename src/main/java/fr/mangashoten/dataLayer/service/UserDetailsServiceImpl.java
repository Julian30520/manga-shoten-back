package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        final Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("utilisateur '" + username + "' introuvable");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.get().getPassword())
                .authorities(user.get().getRole())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}