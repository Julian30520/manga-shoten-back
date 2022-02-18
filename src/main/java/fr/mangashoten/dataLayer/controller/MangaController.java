package fr.mangashoten.dataLayer.controller;

import com.fasterxml.jackson.core.*;
import fr.mangashoten.dataLayer.exception.MangaNotFoundException;
import fr.mangashoten.dataLayer.model.*;
import fr.mangashoten.dataLayer.service.MangaService;
import fr.mangashoten.dataLayer.service.TomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/manga")
public class MangaController {

    @Autowired
    private MangaService mangaService;

    private static final Logger log = LoggerFactory.getLogger(MangaController.class);

    @GetMapping(value = "/all")
    public List<MangaShort> getAllManga(
            @RequestParam(defaultValue = "30") String limit,
            @RequestParam(defaultValue = "0") String offset
            )
            throws IOException {
        return this.mangaService.getAllMangaFromApi(limit, offset);
    }

    @GetMapping(value = "/{manga_id}")
    public Manga getMangaById(@PathVariable String manga_id) throws JsonProcessingException {
        return mangaService.getMangaByIdFromApi(manga_id);
    }

    @GetMapping(value = "/title/{manga_name}")
    public List<MangaShort> getMangaByTitle(@PathVariable String manga_name) throws IOException {
        return mangaService.getMangaByNameFromApi(manga_name);
    }

    @DeleteMapping(value = "/delete/{manga_id}")
    public ResponseEntity<String> deleteMangaById(@PathVariable String manga_id) {
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        try {
            mangaService.deleteMangaById(manga_id);
            return new ResponseEntity<String>("{\"test\": \"Manga deleted !\"}", httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error occur when trying delete manga");
            return new ResponseEntity<String>("{\"test\": \"MError occur when trying delete manga\"}", httpHeaders, HttpStatus.I_AM_A_TEAPOT);
        }

    }
}
