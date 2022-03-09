package fr.mangashoten.dataLayer.repository;

import fr.mangashoten.dataLayer.model.Role;
import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByMail(String email);

    void deleteByUsername(String username);

    @Query(value = "SELECT id_tome From user_tome u where u.id_user = ?1", nativeQuery = true)
    Iterable<Integer> findTomeByUserId(int userId);

}
