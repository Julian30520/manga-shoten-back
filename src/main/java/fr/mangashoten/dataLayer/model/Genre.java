package fr.mangashoten.dataLayer.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type")
    public int idType;

    @Column(name = "name")
    public String name;


    @ManyToMany(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
    )
    @JoinTable(
            name = "genre_manga",
            joinColumns = @JoinColumn(name = "id_type"),
            inverseJoinColumns = @JoinColumn(name = "id_manga")
    )
    @JsonManagedReference
    private List<Manga> mangas = new ArrayList<>();

}

