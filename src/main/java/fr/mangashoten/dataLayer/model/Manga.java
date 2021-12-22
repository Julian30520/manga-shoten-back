package fr.mangashoten.dataLayer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="manga")
@DynamicUpdate
@Getter @Setter @NoArgsConstructor
public class Manga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_manga")
    private int mangaId;
    @Column(name="title_en")
    private String titleEn;
    @Column(name="title_jp")
    private String titleJp;
    private String synopsis;
    @Column(name = "release_date")
    private Date releaseDate;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "id_tome")
    @JsonManagedReference(value = "manga_tome")
    private List<Tome> tomes = new ArrayList<>();

    @ManyToOne(

            cascade = {
                    CascadeType.MERGE
            }
    )
    @JoinColumn(name = "id_author")
    private Author author;


    @ManyToMany(
            mappedBy = "mangas",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
    )
    @JsonIgnoreProperties("mangas")
    private List<Genre> genres = new ArrayList<>();


}
