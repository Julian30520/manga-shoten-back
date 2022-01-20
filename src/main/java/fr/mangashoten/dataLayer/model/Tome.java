package fr.mangashoten.dataLayer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tome")
@DynamicUpdate
@Getter @Setter @NoArgsConstructor
public class Tome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tome")
    private int tomeId;

    @Column(name = "number")
    private int tomeNumber;

    @Column(name = "chapter_number")
    private int chapterNumber;

    @Column(name = "cover")
    private String cover;

    @ManyToMany(
            mappedBy = "tomes",
            cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @ManyToOne(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinColumn(name = "id_editor")
    @JsonBackReference(value="editor_reference")
    private Editor editor;

    @ManyToOne(
            cascade = {
                    CascadeType.MERGE
            }
    )
    @JoinColumn(name = "id_manga")
    @JsonBackReference(value="manga_tome")
    private Manga manga;
}
