package fr.mangashoten.dataLayer.repository;

import fr.mangashoten.dataLayer.model.Manga;
import fr.mangashoten.dataLayer.model.Tome;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TomeRepository  extends CrudRepository<Tome, Integer> {

    public Boolean existsByMangaAndTomeNumber(Manga manga, int tomeNumber);
    public Tome getByMangaAndTomeNumber(Manga manga, int tomeNumber);


}
