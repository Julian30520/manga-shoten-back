package fr.mangashoten.dataLayer.repository;

import fr.mangashoten.dataLayer.model.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends CrudRepository<Genre,String> {

}
