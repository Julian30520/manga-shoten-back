package fr.mangashoten.dataLayer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genre")
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class Genre {

    @Id
    @Column(name = "id_genre")
    public String idGenre;

    @Column(name = "name")
    public String name;

    public Genre(String name, String idGenre) {
        this.name = name;
        this.idGenre = idGenre;
    }


    @ManyToMany(
            mappedBy = "mangaGenre",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
    )
    private List<Manga> mangas = new ArrayList<>();

}

