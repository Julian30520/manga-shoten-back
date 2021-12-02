package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.DataLayerApplication;
import fr.mangashoten.dataLayer.model.Editor;
import fr.mangashoten.dataLayer.model.Role;
import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLayerApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    void get_usersTest() {
        // GIVEN
        List<User> initUsers = new ArrayList<>();

        for (int index = 1; index <= 2; index++) {
            User userToSend = new User();
            userToSend.setUserId(index);
            userToSend.setUsername("TestUsername" + index);
            userToSend.setMail("testing" + index + "@mail.com");
            userToSend.setAvatar("urlImage");
            userToSend.setFirstName("firstNameTest");
            userToSend.setLastName("lastNameTest");
            userToSend.setPassword("passwordTest");
            userToSend.setDateOfBirth(new Date());
            userToSend.setRole(new Role());

            User genericUser = userRepository.save(userToSend);
            initUsers.add(userToSend);
        }

        // WHEN
        Iterable<User> foundUsers = userService.getUsers();
        List<User> result = new ArrayList<>();
        foundUsers.forEach(user -> result.add(user));

        // THEN
        assertEquals(initUsers.get(0).getUserId(), result.get(0).getUserId());
        assertEquals(initUsers.get(1).getUserId(), result.get(1).getUserId());
    }

    @Test
    void get_userByUsernameTest() {
        // GIVEN
        User userToSend = new User();
        userToSend.setUserId(1);
        userToSend.setUsername("TestUsername");
        userToSend.setMail("testing@mail.com");
        userToSend.setAvatar("urlImage");
        userToSend.setFirstName("firstNameTest");
        userToSend.setLastName("lastNameTest");
        userToSend.setPassword("passwordTest");
        userToSend.setDateOfBirth(new Date());
        userToSend.setRole(new Role());

        User genericUser = userRepository.save(userToSend);

        // WHEN
        Optional<User> result = userService.getUserByUsername(genericUser.getUsername());

        // THEN
        assertEquals(genericUser.getUsername(), result.get().getUsername());
    }

    @Test
    void get_userByUsernameTest_notFound() {
        // GIVEN
        User userToSend = new User();
        userToSend.setUserId(1);
        userToSend.setUsername("TestUsername");
        userToSend.setMail("testing@mail.com");
        userToSend.setAvatar("urlImage");
        userToSend.setFirstName("firstNameTest");
        userToSend.setLastName("lastNameTest");
        userToSend.setPassword("passwordTest");
        userToSend.setDateOfBirth(new Date());
        userToSend.setRole(new Role());

        User genericUser = userRepository.save(userToSend);

        // WHEN
        Optional<User> result = userService.getUserByUsername("AnotherUsername");

        // THEN
        assertFalse(result.isPresent());
    }

    @Test
    void add_userTest() {
        // GIVEN
        User userToSend = new User();
        userToSend.setUserId(1);
        userToSend.setUsername("TestUsername");
        userToSend.setMail("testing@mail.com");
        userToSend.setAvatar("urlImage");
        userToSend.setFirstName("firstNameTest");
        userToSend.setLastName("lastNameTest");
        userToSend.setPassword("passwordTest");
        userToSend.setDateOfBirth(new Date());
        userToSend.setRole(new Role());

        // WHEN
        User genericUser = userService.addUser(userToSend);

        // THEN
        assertEquals(userToSend.getUserId(), genericUser.getUserId());
    }

    @Test
    void delete_userTest() {
        // GIVEN
        User userToSend = new User();
        userToSend.setUserId(1);
        userToSend.setUsername("TestUsername");
        userToSend.setMail("testing@mail.com");
        userToSend.setAvatar("urlImage");
        userToSend.setFirstName("firstNameTest");
        userToSend.setLastName("lastNameTest");
        userToSend.setPassword("passwordTest");
        userToSend.setDateOfBirth(new Date());
        userToSend.setRole(new Role());

        User genericUser = userRepository.save(userToSend);

        // WHEN
        userService.deleteUser(genericUser);

        // THEN
        assertEquals(1, genericUser.getUserId());
        assertEquals(Optional.empty(), userRepository.findById(1));
    }
}