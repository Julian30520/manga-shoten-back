package fr.mangashoten.dataLayer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tome")
@DynamicUpdate
@Getter @Setter @NoArgsConstructor
public class Tome {

    @Id
    @Column(name = "id_tome")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tomeId;

    @Column(name = "number")
    private int tomeNumber;

    @Column(name = "chapter_number")
    private int chapterNumber;

    @Column(name = "cover")
    private String cover;


    @ManyToOne(
            cascade = {
                    CascadeType.MERGE
            }
    )
    @JoinColumn(name = "id_manga")
    @JsonBackReference(value="manga_tome")
    private Manga manga;
}
