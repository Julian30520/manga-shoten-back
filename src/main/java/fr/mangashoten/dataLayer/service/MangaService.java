package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.model.Manga;
import fr.mangashoten.dataLayer.repository.MangaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MangaService {
    @Autowired
    private MangaRepository mangaRepository;

    public Iterable<Manga> getAllManga() {
        return mangaRepository.findAll();
    }

    public Optional<Manga> getMangaById(int mangaId) {
        return mangaRepository.findById(mangaId);
    }

    public Manga addManga(Manga manga) {
        return  mangaRepository.save(manga);
    }

    public void deleteMangaById(int mangaId) {
        mangaRepository.deleteById(mangaId);
    }

    public Optional<Manga> findByTitleEn(String title) {
        return mangaRepository.findByTitleEn(title);
    }
}
