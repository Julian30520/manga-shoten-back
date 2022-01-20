package fr.mangashoten.dataLayer.dto;

import fr.mangashoten.dataLayer.model.Role;
import fr.mangashoten.dataLayer.model.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Mapper {
    public UserDto toDto(User user) {
        int id = user.getUserId();
        String name = user.getUsername();
        Role role = user.getRole();
        String mail = user.getMail();
        String avatar = user.getAvatar();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        Date dateOfBirth = user.getDateOfBirth();

        return new UserDto(id, name, role, mail, avatar, firstName, lastName, dateOfBirth);
    }

    public User toUser(UserDto userDTO) {
        return new User(userDTO.getId(), userDTO.getUsername(), userDTO.getMail(), userDTO.getAvatar(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getDateOfBirth(), userDTO.getRole());
    }
}