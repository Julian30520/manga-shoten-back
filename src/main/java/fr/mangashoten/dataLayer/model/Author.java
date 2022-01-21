package fr.mangashoten.dataLayer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "author")
@DynamicUpdate
@Getter @Setter @NoArgsConstructor
public class Author {

    @Id
    @Column(name = "id_author")
    private String authorId;

    private String name;

    public Author(String name) {
        this.name = name;
    }

    public Author(String name, String id) {
        this.name = name;
        authorId = id;
    }

}
