package fr.mangashoten.dataLayer.model;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tome")
    private int tomeId;

    @Column(name = "number")
    private int tomeNumber;

    @Column(name = "page_number")
    private int pageNumber;

    @Column(name = "cover")
    private String cover;

}
