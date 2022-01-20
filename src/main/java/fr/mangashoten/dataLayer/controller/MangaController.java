package fr.mangashoten.dataLayer.controller;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import fr.mangashoten.dataLayer.deserializer.ListMangaDeserializer;
import fr.mangashoten.dataLayer.deserializer.MangaDeserializer;
import fr.mangashoten.dataLayer.model.Author;
import fr.mangashoten.dataLayer.model.Manga;
import fr.mangashoten.dataLayer.model.Tome;
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

    @GetMapping(value = "/all")
    public List<Manga> getAllManga() throws IOException, ParseException {

        String url = "https://api.mangadex.org/manga?includes[]=author&includes[]=artist&includes[]=cover_art&limit=20";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        List<Manga> mangaList = new ArrayList<>();

        JsonNode rootNode = mapper.readTree(response.getBody());
        JsonNode dataNode = rootNode.get("data");
        if(dataNode.isArray()) {
            for (JsonNode node : dataNode) {
                Manga manga = new Manga();
                manga.setMangadexId(node.get("id").textValue());
                manga.setTitleEn(node.get("attributes").get("title").get("en").textValue());
                if(node.get("attributes").get("altTitles").asToken() == JsonToken.START_ARRAY) {
                    JsonNode altTitleNode = node.get("attributes").get("altTitles");
                    for (JsonNode elemNode : (ArrayNode) altTitleNode) {
                        if(elemNode.get("ja") != null) {
                            manga.setTitleJp(elemNode.get("ja").textValue());
                            break;
                        }
                        if(elemNode.get("zh") != null) {
                            manga.setTitleJp(elemNode.get("zh").textValue());
                            break;
                        }
                    }
                }
                manga.setStatus(node.get("attributes").get("status").textValue());
                manga.setSynopsis(node.get("attributes").get("description").get("en").textValue());
                manga.setReleaseDate(node.get("attributes").get("year").asText());
                if(node.get("relationships").asToken() == JsonToken.START_ARRAY) {
                    JsonNode relationshipsNode = node.get("relationships");
                    for (JsonNode elemNode : (ArrayNode) relationshipsNode) {
                        if(elemNode.get("type").textValue().equals("cover_art")) {
                            manga.setCover(elemNode.get("attributes").get("fileName").textValue());
                        }
                        if(elemNode.get("type").textValue().equals("author")) {
                            manga.setAuthor(new Author(elemNode.get("attributes").get("name").textValue()));
                        }
                    }
                }

                List<Tome> mangaListTome = new ArrayList<>();
                //Add Tome to the manga
                String urlTome = "https://api.mangadex.org/manga/" + manga.getMangadexId() + "/aggregate";
                ResponseEntity<String> responseTome
                        = restTemplate.getForEntity(urlTome, String.class);
                ObjectMapper mapperTome = new ObjectMapper();
                JsonNode rootNodeTome = mapperTome.readTree(responseTome.getBody());
                JsonNode volumesNode = rootNodeTome.get("volumes");
                volumesNode.forEach(volume -> {
                    if(volume.get("volume").asInt() >= 1) {
                        Tome tome = new Tome();
                        tome.setTomeNumber(volume.get("volume").asInt());
                        tome.setChapterNumber(volume.get("count").asInt());
                        mangaListTome.add(tome);
                    }
                });

                List<String> chapterList = new ArrayList<>();
                volumesNode.get("none").get("chapters").forEach(chapter -> chapterList.add(chapter.get("chapter").textValue()));
                manga.setLastChapter(chapterList.get(0));

                //Add cover for each tome
                String urlTomeCover = "https://api.mangadex.org/cover?manga[]=" + manga.getMangadexId() + "&limit=100";
                ResponseEntity<String> responseTomeCover
                        = restTemplate.getForEntity(urlTomeCover, String.class);
                ObjectMapper mapperTomeCover = new ObjectMapper();
                JsonNode rootNodeTomeCover = mapperTomeCover.readTree(responseTomeCover.getBody());
                JsonNode dataNodeTomeCover = rootNodeTomeCover.get("data");
                if(dataNodeTomeCover.isArray()) {
                    for (JsonNode nodeTomeCover : dataNodeTomeCover) {
                        for (Tome tome : mangaListTome) {
                            if(tome.getTomeNumber() == nodeTomeCover.get("attributes").get("volume").asInt()) {
                                tome.setCover(nodeTomeCover.get("attributes").get("fileName").textValue());
                            }
                        }
                    }
                }

                manga.setLastVolume(Integer.toString(mangaListTome.size()));
                manga.setTomes(mangaListTome);
                mangaList.add(manga);
            }
        }

        return mangaList;
    }

    @GetMapping(value = "/{manga_id}")
    public Manga getMangaById(@PathVariable Integer manga_id) {
        return mangaService.getMangaById(manga_id);
    }

    @GetMapping(value = "/title/{manga_name}")
    public Manga getMangaByTitle(@PathVariable String manga_name) throws JsonProcessingException {
        //A terminer !
        //String url = "https://api.mangadex.org/manga/a1c7c817-4e59-43b7-9365-09675a149a6f";
        String url = "https://api.mangadex.org/manga";
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
