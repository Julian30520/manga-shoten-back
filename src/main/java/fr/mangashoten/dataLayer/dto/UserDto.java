package fr.mangashoten.dataLayer.dto;


import com.sun.istack.NotNull;
import fr.mangashoten.dataLayer.model.Role;

/**
 * Specifique : AppUser DTO permet de renvoyer un User sans le mot de passe (REST response).
 */
public class UserDto {

    private Long id;
    private String username;
    private Role role;

    public UserDto() { }

    public UserDto(@NotNull String username) {
        this(username,null);
    }

    public UserDto(@NotNull String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}