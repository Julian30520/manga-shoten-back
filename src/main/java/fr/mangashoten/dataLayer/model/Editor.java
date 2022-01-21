package fr.mangashoten.dataLayer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "id_editor")
    private String editorId;

    @Column(name = "name")
    private String name;

    @Column(name = "website")
    private String webUrl;

    public Editor(String id) {
        editorId = id;
    }

    @OneToMany(
            mappedBy = "editor",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference(value="editor_reference")
    private List<Tome> tomes = new ArrayList<>();
}
