package fr.mangashoten.dataLayer.dto;


import com.sun.istack.NotNull;
import fr.mangashoten.dataLayer.model.Role;

import javax.persistence.Column;
import java.util.Date;

/**
 * Specifique : AppUser DTO permet de renvoyer un User sans le mot de passe (REST response).
 */
public class UserDto {

    private int id;
    private String username;
    private Role role;

    private String mail;

    private String avatar;

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    public UserDto() { }

    public UserDto( String username) {
        this(username,null);
    }

    public UserDto( String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public UserDto(int id, String username, Role role, String mail, String avatar, String firstName, String lastName, Date dateOfBirth){
        this.id = id;
        this.username = username;
        this.role = role;
        this.mail = mail;
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}