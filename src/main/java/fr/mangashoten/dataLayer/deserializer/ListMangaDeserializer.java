package fr.mangashoten.dataLayer.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import fr.mangashoten.dataLayer.model.Manga;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ListMangaDeserializer extends StdDeserializer<List<Manga>> {

    public ListMangaDeserializer() {
        this(null);
    }

    public ListMangaDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<Manga> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        System.out.println("We are in deserializer of ListMangaDeserializer !");
        return null;
    }
}
