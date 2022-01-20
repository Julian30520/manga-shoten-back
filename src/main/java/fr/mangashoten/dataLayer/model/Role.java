package fr.mangashoten.dataLayer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "role")
@DynamicUpdate
@Getter @Setter @NoArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private int roleId;

    @Column(name = "code_role")
    private String codeRole;


    @Override
    public String getAuthority(){
        return this.getCodeRole();
    }
}
