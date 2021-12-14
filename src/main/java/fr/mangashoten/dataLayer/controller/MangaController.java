package fr.mangashoten.dataLayer.controller;

import fr.mangashoten.dataLayer.model.Manga;
import fr.mangashoten.dataLayer.service.MangaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.ArrayList;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/manga")
public class MangaController {

    @Autowired
    private MangaService mangaService;

    @GetMapping(value = "/all")
    public ArrayList<Manga> getAllManga() {
        return mangaService.getAllManga();
    }

    @GetMapping(value = "/{manga_id}")
    public Manga getMangaById(@PathVariable Integer manga_id) {
        return mangaService.getMangaById(manga_id);
    }

    @GetMapping(value = "/title/{manga_name}")
    public Manga getMangaByTitle(@PathVariable String manga_name) {
        return mangaService.findByTitleEn(manga_name);
    }

    @RequestMapping(
            value = "/add",
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public ResponseEntity<String> createManga(@RequestBody Manga newManga) throws ServerException {
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Manga manga = mangaService.addManga(newManga);
        if (manga == null) {
            throw new ServerException("manga value null");
        } else {
            return new ResponseEntity<String>("{\"test\": \"Manga created !\"}", httpHeaders, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/delete/{manga_id}")
    public ResponseEntity<String> deleteMangaById(@PathVariable Integer manga_id) {
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
