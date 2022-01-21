package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.model.Genre;
import fr.mangashoten.dataLayer.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenreService {
    @Autowired
    GenreRepository genreRepository;

    public Iterable<Genre> getGenres() {
        return genreRepository.findAll();
    }
    public Genre getGenreById(String idType){
        return genreRepository.findById(idType).get();
    }
    public Genre addGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public void deleteByGenre(String idType) {
        genreRepository.deleteById(idType);
    }


}
