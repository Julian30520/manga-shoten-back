package fr.mangashoten.dataLayer.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
@DynamicUpdate
@Getter @Setter @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private int userId;

    @Column(name = "username")
    private String username;

    @Column(name = "mail")
    private String mail;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @ManyToOne(
            cascade = {
                    CascadeType.MERGE
            })
    @JoinColumn(name = "id_role")
    private Role role;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            })
    @JoinTable(
            name = "user_tome",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_tome")
    )
    private List<Tome> tomes = new ArrayList<>();


    public User(String username, String mail, String password, Role role){
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.role = role;
    }

    public User(int userId, String username, String mail, String avatar, String firstName, String lastName, Date dateOfBirth, Role role) {
        this.userId = userId;
        this.username = username;
        this.mail = mail;
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }

    public void addTome(Tome tome) {
        tomes.add(tome);
    }

    public void removeTome(Tome tome) {
        tomes.remove(tome);
    }

}
