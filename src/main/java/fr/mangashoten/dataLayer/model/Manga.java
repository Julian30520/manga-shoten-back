package fr.mangashoten.dataLayer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="manga")
@DynamicUpdate
@Getter @Setter @NoArgsConstructor
public class Manga {

    @Id
    @Column(name = "id_manga")
    private String mangaId;

    @Column(name = "title_en")
    private String titleEn;

    @Column(name= "title_jp")
    private String titleJp;

    private String status;

    @Column(name= "publication_demographic")
    private String pubDemographic;

    @Column(name= "last_volume")
    private String lastVolume;

    @Column(name= "last_chapter")
    private String lastChapter;

    private String cover;
    private String synopsis;

    @Column(name = "release_date")
    private String releaseDate;

    public Manga(String id) {
        mangaId = id;
    }

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "manga"
    )
    //@JsonManagedReference(value = "manga_tome")
    @JsonIgnoreProperties({"titleJp", "status","pubDemographic","lastVolume","lastChapter","synopsis","releaseDate","tomes","author", "mangaGenre"})
    private List<Tome> tomes = new ArrayList<>();

    @ManyToOne(

            cascade = {
                    CascadeType.MERGE
            }
    )
    @JoinColumn(name = "id_author")
    private Author author;


    @ManyToMany(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
    )
    @JoinTable(
            name = "genre_manga",
            joinColumns = @JoinColumn(name = "id_manga"),
            inverseJoinColumns = @JoinColumn(name = "id_genre")
    )
    @JsonIgnoreProperties("mangaGenre")
    private List<Genre> mangaGenre = new ArrayList<>();


}
