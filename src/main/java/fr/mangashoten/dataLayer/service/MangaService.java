package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.model.Manga;
import fr.mangashoten.dataLayer.repository.MangaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MangaService {
    @Autowired
    private MangaRepository mangaRepository;

    public ArrayList<Manga> getAllManga() {
        Iterable<Manga> mangas = mangaRepository.findAll();
        ArrayList<Manga> arrayListManga = new ArrayList<>();
        mangas.forEach(manga -> arrayListManga.add(manga));

        return arrayListManga;
    }

    public Manga getMangaById(int mangaId) {
        return mangaRepository.findById(mangaId).get();
    }

    public Manga addManga(Manga manga) {
        return  mangaRepository.save(manga);
    }

    public void deleteMangaById(int mangaId) {
        mangaRepository.deleteById(mangaId);
    }

    public Manga findByTitleEn(String title) {
        return mangaRepository.findByTitleEn(title).get();
    }


}
