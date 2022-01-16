package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.DataLayerApplication;
import fr.mangashoten.dataLayer.model.Author;
import fr.mangashoten.dataLayer.model.Manga;
import fr.mangashoten.dataLayer.repository.AuthorRepository;
import fr.mangashoten.dataLayer.repository.MangaRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLayerApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthorServiceTest {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    MangaRepository mangaRepository;

    @Autowired
    AuthorService authorService;

    @Test
    void get_AllAuthorTest() {
        // GIVEN
        List<Author> initAuthors = new ArrayList<>();

        for (int index = 1; index <= 2; index++) {
            Author authorToSend = new Author();
            authorToSend.setAuthorId(index);
            authorToSend.setFirstName("TestFirstName" + index);
            authorToSend.setLastName("TestLastName" + index);

            Author genericAuthor = authorRepository.save(authorToSend);
            initAuthors.add(authorToSend);
        }

        // WHEN
        Iterable<Author> foundAuthors = authorService.getAllAuthor();
        List<Author> result = new ArrayList<>();
        foundAuthors.forEach(author -> result.add(author));

        // THEN
        assertEquals(initAuthors.get(0).getAuthorId(), result.get(0).getAuthorId());
        assertEquals(initAuthors.get(1).getAuthorId(), result.get(1).getAuthorId());
    }

    @Test
    void get_AuthorByIdTest() {
        // GIVEN
        Author authorToSend = new Author();
        authorToSend.setAuthorId(1);
        authorToSend.setFirstName("TestFirstName");
        authorToSend.setLastName("TestLastName");

        Author genericAuthor = authorRepository.save(authorToSend);

        // WHEN
        Author foundAuthor = authorService.getAuthorById(genericAuthor.getAuthorId());

        // THEN
        assertEquals(genericAuthor.getAuthorId(), foundAuthor.getAuthorId());
    }

    @Test
    void add_AuthorTest() {
        // GIVEN
        Author authorToSend = new Author();
        authorToSend.setAuthorId(1);
        authorToSend.setFirstName("TestFirstName");
        authorToSend.setLastName("TestLastName");

        // WHEN
        Author genericAuthor = authorService.addAuthor(authorToSend);

        // THEN
        assertEquals(authorToSend.getAuthorId(), genericAuthor.getAuthorId());
    }

    @Test
    void delete_AuthorTest() {
        // GIVEN
        Author authorToSend = new Author();
        authorToSend.setAuthorId(1);
        authorToSend.setFirstName("TestFirstName");
        authorToSend.setLastName("TestLastName");

        Author genericAuthor = authorRepository.save(authorToSend);

        // WHEN
        authorService.deleteAuthor(genericAuthor.getAuthorId());

        // THEN
        assertEquals(1, genericAuthor.getAuthorId());
        assertEquals(Optional.empty(), authorRepository.findById(1));
    }

    @Test
    void get_AuthorFullNameTest() {
        // GIVEN
        Author authorToSend = new Author();
        authorToSend.setAuthorId(1);
        authorToSend.setFirstName("TestFirstName");
        authorToSend.setLastName("TestLastName");

        Author genericAuthor = authorRepository.save(authorToSend);

        Manga mangaToSend = new Manga();
        mangaToSend.setMangaId(1);
        mangaToSend.setTitleEn("TestTitleEn");
        mangaToSend.setTitleJp("TestTitleJp");
        mangaToSend.setSynopsis("TestLoremIpsum");
        mangaToSend.setReleaseDate("2021");
        mangaToSend.setAuthor(genericAuthor);

        Manga genericManga = mangaRepository.save(mangaToSend);

        // WHEN
        String result = authorService.getAuthorFullName(genericManga.getTitleEn());

        // THEN
        assertEquals(genericAuthor.getLastName() + " " + genericAuthor.getFirstName(), result);
    }
}