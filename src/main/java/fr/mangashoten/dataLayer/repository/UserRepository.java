package fr.mangashoten.dataLayer.repository;

import fr.mangashoten.dataLayer.model.Role;
import fr.mangashoten.dataLayer.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);

}
