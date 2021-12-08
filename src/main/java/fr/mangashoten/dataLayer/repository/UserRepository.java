package fr.mangashoten.dataLayer.repository;

import fr.mangashoten.dataLayer.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}
