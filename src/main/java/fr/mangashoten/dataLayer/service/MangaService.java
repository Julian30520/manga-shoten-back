package fr.mangashoten.dataLayer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.mangashoten.dataLayer.model.*;
import fr.mangashoten.dataLayer.repository.MangaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    public List<MangaShort> getAllMangaFromApi(String limit) throws IOException {
        String url = "https://api.mangadex.org/manga?includes[]=cover_art&limit=" + limit;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        List<MangaShort> mangaList = new ArrayList<>();

        JsonNode rootNode = mapper.readTree(response.getBody());
        JsonNode dataNode = rootNode.get("data");
        if(dataNode.isArray()) {
            for (JsonNode node : dataNode) {
                MangaShort manga = new MangaShort();
                manga.setMangadexId(node.get("id").textValue());
                manga.setTitleEn(node.get("attributes").get("title").get("en").textValue());
                if(node.get("relationships").asToken() == JsonToken.START_ARRAY) {
                    JsonNode relationshipsNode = node.get("relationships");
                    for (JsonNode elemNode : relationshipsNode) {
                        if(elemNode.get("type").textValue().equals("cover_art")) {
                            manga.setCover(elemNode.get("attributes").get("fileName").textValue());
                        }
                    }
                }

                //Add Tome to the manga
                String urlTome = "https://api.mangadex.org/manga/" + manga.getMangadexId() + "/aggregate";
                ResponseEntity<String> responseTome
                        = restTemplate.getForEntity(urlTome, String.class);
                ObjectMapper mapperTome = new ObjectMapper();
                JsonNode rootNodeTome = mapperTome.readTree(responseTome.getBody());
                JsonNode volumesNode = rootNodeTome.get("volumes");
                AtomicInteger volumeCount = new AtomicInteger();
                volumesNode.forEach(volume -> {
                    if(volume.get("volume").asInt() >= 1) {
                        volumeCount.getAndIncrement();
                    }
                });
                manga.setLastVolume(volumeCount.toString());

                List<String> chapterList = new ArrayList<>();
                volumesNode.get("none").get("chapters").forEach(chapter -> chapterList.add(chapter.get("chapter").textValue()));
                manga.setLastChapter(chapterList.get(0));

                mangaList.add(manga);
            }
        }

        return mangaList;
    }

    public Manga getMangaByIdFromApi(String mangadexId) throws JsonProcessingException {
        String url = "https://api.mangadex.org/manga?ids[]=" + mangadexId + "&includes[]=author&includes[]=artist&includes[]=cover_art";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        Manga manga = new Manga();

        JsonNode rootNode = mapper.readTree(response.getBody());
        JsonNode dataNode = rootNode.get("data");
        if(dataNode.isArray()) {
            for (JsonNode node : dataNode) {
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
                manga.setPubDemographic(node.get("attributes").get("publicationDemographic").asText());
                if(node.get("relationships").asToken() == JsonToken.START_ARRAY) {
                    JsonNode relationshipsNode = node.get("relationships");
                    for (JsonNode elemNode : relationshipsNode) {
                        if(elemNode.get("type").textValue().equals("cover_art")) {
                            manga.setCover(elemNode.get("attributes").get("fileName").textValue());
                        }
                        if(elemNode.get("type").textValue().equals("author")) {
                            manga.setAuthor(new Author(elemNode.get("attributes").get("name").textValue()));
                        }
                    }
                }

                List<Genre> genreList = new ArrayList<>();
                if(node.get("attributes").get("tags").asToken() == JsonToken.START_ARRAY) {
                    JsonNode tagsNode = node.get("attributes").get("tags");
                    for (JsonNode tag : tagsNode) {
                        genreList.add(new Genre(tag.get("attributes").get("name").get("en").textValue()));
                    }
                    manga.setGenres(genreList);
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
            }
        }

        return manga;
    }

    public List<Manga> getAllMangaFromApiSearchbar() throws IOException {
        return null;
    }

    /*
    * String url = "https://api.mangadex.org/manga?includes[]=author&includes[]=artist&includes[]=cover_art&limit=20";
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
                    for (JsonNode elemNode : relationshipsNode) {
                        if(elemNode.get("type").textValue().equals("cover_art")) {
                            manga.setCover(elemNode.get("attributes").get("fileName").textValue());
                        }
                        if(elemNode.get("type").textValue().equals("author")) {
                            manga.setAuthor(new Author(elemNode.get("attributes").get("name").textValue()));
                        }
                    }
                }

                List<Genre> genreList = new ArrayList<>();
                if(node.get("attributes").get("tags").asToken() == JsonToken.START_ARRAY) {
                    JsonNode tagsNode = node.get("attributes").get("tags");
                    for (JsonNode tag : tagsNode) {
                        genreList.add(new Genre(tag.get("attributes").get("name").get("en").textValue()));
                    }
                    manga.setGenres(genreList);
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
    * */

}
