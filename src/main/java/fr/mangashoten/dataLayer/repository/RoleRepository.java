package fr.mangashoten.dataLayer.repository;

import fr.mangashoten.dataLayer.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
