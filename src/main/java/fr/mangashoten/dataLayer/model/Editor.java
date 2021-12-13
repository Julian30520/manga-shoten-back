package fr.mangashoten.dataLayer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "editor")
@DynamicUpdate
@Getter @Setter @NoArgsConstructor
public class Editor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_editor")
    private int editorId;

    @Column(name = "name")
    private String name;

    @Column(name = "website")
    private String webUrl;

    @OneToMany(
            mappedBy = "editor",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonBackReference
    private List<Tome> tomes = new ArrayList<>();
}
