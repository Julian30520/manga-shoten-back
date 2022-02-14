package fr.mangashoten.dataLayer.repository;

import fr.mangashoten.dataLayer.model.Manga;
import fr.mangashoten.dataLayer.model.Tome;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TomeRepository  extends CrudRepository<Tome, String> {

    public Boolean existsByMangaAndTomeNumber(Manga manga, int tomeNumber);
    public Tome getByMangaAndTomeNumber(Manga manga, int tomeNumber);
}
