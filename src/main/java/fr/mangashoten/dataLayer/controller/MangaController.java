package fr.mangashoten.dataLayer.controller;

import com.fasterxml.jackson.core.*;
import fr.mangashoten.dataLayer.model.*;
import fr.mangashoten.dataLayer.service.MangaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/manga")
public class MangaController {

    @Autowired
    private MangaService mangaService;

    private static final Logger log = LoggerFactory.getLogger(MangaController.class);

    /**
     * Obtient la liste des mangas de MangaDex en prenant en gérant la pagination (Ne procède pas à l'extraction de ceux-ci)
     * @param limit
     * @param offset
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/all")
    public List<MangaShort> getAllManga(
            @RequestParam(defaultValue = "30") String limit,
            @RequestParam(defaultValue = "0") String offset
            )
            throws IOException {
        return this.mangaService.getAllMangaFromApi(limit, offset);
    }

    /**
     * Récupère les information d'un manga sur MangaDex
     * @param manga_id L'id MangaDex du manga à récupérer
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping(value = "/{manga_id}")
    public Manga getMangaById(@PathVariable String manga_id) throws JsonProcessingException {
        return mangaService.getMangaByIdFromApi(manga_id);
    }

    @GetMapping(value = "/title/{manga_name}")
    public List<MangaShort> getMangaByTitle(@PathVariable String manga_name) throws IOException {
        return mangaService.getMangaByNameFromApi(manga_name);
    }

}
