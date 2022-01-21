package fr.mangashoten.dataLayer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter @NoArgsConstructor
public class MangaShort {

    private String mangaId;
    private String titleEn;
    private String lastVolume;
    private String lastChapter;
    private String cover;
}
