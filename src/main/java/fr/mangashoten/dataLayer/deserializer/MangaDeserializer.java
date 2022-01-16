package fr.mangashoten.dataLayer.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.mangashoten.dataLayer.model.Author;
import fr.mangashoten.dataLayer.model.Manga;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MangaDeserializer extends StdDeserializer<Manga> {

    public MangaDeserializer() {
        this(null);
    }

    public MangaDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Manga deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {

        JsonNode mangaNode = jsonParser.getCodec().readTree(jsonParser);
            Manga manga = new Manga();
            manga.setMangaId(1);
            manga.setTitleEn(mangaNode.get("data").get("attributes").get("title").get("en").textValue());
            if(mangaNode.get("data").get("attributes").get("altTitles").asToken() == JsonToken.START_ARRAY) {
                JsonNode altTitleNode = mangaNode.get("data").get("attributes").get("altTitles");
                for (JsonNode node : (ArrayNode) altTitleNode) {
                    if(node.get("ja") != null) {
                        manga.setTitleJp(node.get("ja").textValue());
                        break;
                    }
                    if(node.get("zh") != null) {
                        manga.setTitleJp(node.get("zh").textValue());
                        break;
                    }
                }
            }
            manga.setSynopsis(mangaNode.get("data").get("attributes").get("description").get("en").textValue());
            manga.setReleaseDate(Integer.toString((Integer) mangaNode.get("data").get("attributes").get("year").numberValue()));
            manga.setAuthor(new Author());

            return manga;
    }
}
