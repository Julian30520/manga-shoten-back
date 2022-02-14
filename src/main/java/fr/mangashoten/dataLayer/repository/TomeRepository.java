package fr.mangashoten.dataLayer.repository;

import fr.mangashoten.dataLayer.model.Tome;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TomeRepository  extends CrudRepository<Tome, Integer> {

}
