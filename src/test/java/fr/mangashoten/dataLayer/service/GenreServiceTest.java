package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.DataLayerApplication;
import fr.mangashoten.dataLayer.model.Genre;
import fr.mangashoten.dataLayer.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLayerApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GenreServiceTest {

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    GenreService genreService;

    @Test
    void get_GenresTest() {
        // GIVEN
        List<Genre> initGenres = new ArrayList<>();

        for (int index = 1; index <= 2; index++) {
            Genre genreToSend = new Genre();
            genreToSend.setIdGenre(Integer.toString(index));
            genreToSend.setName("TestGenreName" + index);
            genreToSend.setMangas(new ArrayList<>());

            Genre genericGenre = genreRepository.save(genreToSend);
            initGenres.add(genericGenre);
        }

        // WHEN
        Iterable<Genre> foundGenres = genreService.getGenres();
        List<Genre> result = new ArrayList<>();
        foundGenres.forEach(genre -> result.add(genre));

        // THEN
        assertEquals(initGenres.get(0).getIdGenre(), result.get(0).getIdGenre());
        assertEquals(initGenres.get(1).getIdGenre(), result.get(1).getIdGenre());
    }

    @Test
    void get_GenreByIdTest() {
        // GIVEN
        Genre genreToSend = new Genre();
        genreToSend.setIdGenre("1");
        genreToSend.setName("TestGenreName");
        genreToSend.setMangas(new ArrayList<>());

        Genre genericGenre = genreRepository.save(genreToSend);

        // WHEN
        Genre foundGenre = genreService.getGenreById(genericGenre.getIdGenre());

        // THEN
        assertEquals(genericGenre.getIdGenre(), foundGenre.getIdGenre());
    }

    @Test
    void add_GenreTest() {
        // GIVEN
        Genre genreToSend = new Genre();
        genreToSend.setIdGenre("1");
        genreToSend.setName("TestGenreName");
        genreToSend.setMangas(new ArrayList<>());

        // WHEN
        Genre genericGenre = genreService.addGenre(genreToSend);

        // THEN
        assertEquals(genreToSend.getIdGenre(), genericGenre.getIdGenre());
    }

    @Test
    void delete_ByGenreTest() {
        Genre genreToSend = new Genre();
        genreToSend.setIdGenre("1");
        genreToSend.setName("TestGenreName");
        genreToSend.setMangas(new ArrayList<>());

        Genre genericGenre = genreRepository.save(genreToSend);

        // WHEN
        genreService.deleteByGenre(genericGenre.getIdGenre());

        // THEN
        assertEquals("1", genericGenre.getIdGenre());
        assertEquals(Optional.empty(), genreRepository.findById("1"));
    }
}