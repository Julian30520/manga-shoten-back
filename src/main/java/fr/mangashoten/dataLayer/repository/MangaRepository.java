package fr.mangashoten.dataLayer.repository;

import fr.mangashoten.dataLayer.model.Manga;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MangaRepository extends CrudRepository<Manga, Integer> {
    public Optional<Manga> findByTitleEn(String title);
}
