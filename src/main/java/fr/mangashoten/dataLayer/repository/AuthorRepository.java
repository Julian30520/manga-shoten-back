package fr.mangashoten.dataLayer.repository;

import fr.mangashoten.dataLayer.model.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;

@Repository
public interface AuthorRepository extends CrudRepository<Author,Integer> {
}
