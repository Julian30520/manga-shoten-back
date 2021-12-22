package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.DataLayerApplication;
import fr.mangashoten.dataLayer.model.*;
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
class MangaServiceTest {

    @Autowired
    MangaRepository mangaRepository;

    @Autowired
    MangaService mangaService;

    @Test
    void get_AllMangaTest() {
        // GIVEN
        List<Manga> initMangas = new ArrayList<>();

        for (int index = 1; index <= 2; index++) {
            Manga mangaToSend = new Manga();
            mangaToSend.setMangaId(index);
            mangaToSend.setTitleEn("TestTitleEn" + index);
            mangaToSend.setTitleJp("TestTitleJp" + index);
            mangaToSend.setSynopsis("TestLoremIpsum");
            mangaToSend.setReleaseDate(new Date());
            mangaToSend.setAuthor(new Author());

            Manga genericManga = mangaRepository.save(mangaToSend);
            initMangas.add(mangaToSend);
        }

        // WHEN
        Iterable<Manga> foundMangas = mangaService.getAllManga();
        List<Manga> result = new ArrayList<>();
        foundMangas.forEach(manga -> result.add(manga));

        // THEN
        assertEquals(initMangas.get(0).getMangaId(), result.get(0).getMangaId());
        assertEquals(initMangas.get(1).getMangaId(), result.get(1).getMangaId());
    }

    @Test
    void get_MangaByIdTest() {
        // GIVEN
        Manga mangaToSend = new Manga();
        mangaToSend.setMangaId(1);
        mangaToSend.setTitleEn("TestTitleEn");
        mangaToSend.setTitleJp("TestTitleJp");
        mangaToSend.setSynopsis("TestLoremIpsum");
        mangaToSend.setReleaseDate(new Date());
        mangaToSend.setAuthor(new Author());

        Manga genericManga = mangaRepository.save(mangaToSend);

        // WHEN
        Manga foundManga = mangaService.getMangaById(genericManga.getMangaId());

        // THEN
        assertEquals(genericManga.getMangaId(), foundManga.getMangaId());
    }

    @Test
    void add_MangaTest() {
        // GIVEN
        Manga mangaToSend = new Manga();
        mangaToSend.setMangaId(1);
        mangaToSend.setTitleEn("TestTitleEn");
        mangaToSend.setTitleJp("TestTitleJp");
        mangaToSend.setSynopsis("TestLoremIpsum");
        mangaToSend.setReleaseDate(new Date());
        mangaToSend.setAuthor(new Author());

        // WHEN
        Manga genericManga = mangaService.addManga(mangaToSend);

        // THEN
        assertEquals(mangaToSend.getMangaId(), genericManga.getMangaId());
    }

    @Test
    void delete_MangaByIdTest() {
        // GIVEN
        Manga mangaToSend = new Manga();
        mangaToSend.setMangaId(1);
        mangaToSend.setTitleEn("TestTitleEn");
        mangaToSend.setTitleJp("TestTitleJp");
        mangaToSend.setSynopsis("TestLoremIpsum");
        mangaToSend.setReleaseDate(new Date());
        mangaToSend.setAuthor(new Author());

        Manga genericManga = mangaRepository.save(mangaToSend);

        // WHEN
        mangaService.deleteMangaById(genericManga.getMangaId());

        // THEN
        assertEquals(1, genericManga.getMangaId());
        assertEquals(Optional.empty(), mangaRepository.findById(1));
    }

    @Test
    void find_ByTitleEnTest() {
        // GIVEN
        Manga mangaToSend = new Manga();
        mangaToSend.setMangaId(1);
        mangaToSend.setTitleEn("TestTitleEn");
        mangaToSend.setTitleJp("TestTitleJp");
        mangaToSend.setSynopsis("TestLoremIpsum");
        mangaToSend.setReleaseDate(new Date());
        mangaToSend.setAuthor(new Author());

        Manga genericManga = mangaRepository.save(mangaToSend);

        // WHEN
        Manga result = mangaService.findByTitleEn(genericManga.getTitleEn());

        // THEN
        assertEquals(genericManga.getTitleEn(), result.getTitleEn());
    }
}