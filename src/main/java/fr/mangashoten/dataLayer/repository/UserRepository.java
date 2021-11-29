package fr.mangashoten.dataLayer.repository;

import fr.mangashoten.dataLayer.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    public Optional<User> findByUsername(String username);
}
