package fr.mangashoten.dataLayer.controller;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import fr.mangashoten.dataLayer.deserializer.ListMangaDeserializer;
import fr.mangashoten.dataLayer.deserializer.MangaDeserializer;
import fr.mangashoten.dataLayer.model.*;
import fr.mangashoten.dataLayer.service.MangaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.rmi.ServerException;
import java.text.ParseException;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/manga")
public class MangaController {

    @Autowired
    private MangaService mangaService;

    @Autowired
    private MangaDeserializer mangaDeserializer;

    @GetMapping(value = "/all/{limit}")
    public List<MangaShort> getAllManga(@PathVariable String limit) throws IOException {
        return this.mangaService.getAllMangaFromApi(limit);
    }

    @GetMapping(value = "/{manga_id}")
    public Manga getMangaById(@PathVariable Integer manga_id) {
        return mangaService.getMangaById(manga_id);
    }

    @GetMapping(value = "/title/{manga_name}")
    public Manga getMangaByTitle(@PathVariable String manga_name) throws JsonProcessingException {
        String url = "https://api.mangadex.org/manga?title=";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Manga.class, new MangaDeserializer());
        mapper.registerModule(module);

        Manga manga = mapper.readValue(response.getBody(), Manga.class);

        return manga;
        //return mangaService.findByTitleEn(manga_name);
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
