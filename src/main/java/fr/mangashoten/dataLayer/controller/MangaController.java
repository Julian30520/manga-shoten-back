package fr.mangashoten.dataLayer.controller;

import fr.mangashoten.dataLayer.model.Manga;
import fr.mangashoten.dataLayer.service.MangaService;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/manga")
public class MangaController {

    @Autowired
    private MangaService mangaService;

    @GetMapping(value = "/all")
    public List<Object> getAllManga() {

        //return mangaService.getAllManga();

        //String url = "https://api.mangadex.org/manga";

        String url = "https://swapi.dev/api/films";
        RestTemplate restTemplate = new RestTemplate();

        Object[] listManga = restTemplate.getForObject(url, Object[].class);

        return Arrays.asList(listManga);
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
