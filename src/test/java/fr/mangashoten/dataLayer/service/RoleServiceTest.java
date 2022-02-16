package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.DataLayerApplication;
import fr.mangashoten.dataLayer.model.Role;
import fr.mangashoten.dataLayer.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLayerApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoleServiceTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Test
    void get_RolesTest() {
        // GIVEN
        List<Role> initRoles = new ArrayList<>();

        for (int index = 1; index <= 2; index++) {
            Role roleToSend = new Role();
            roleToSend.setRoleId(index);
            roleToSend.setCodeRole("test");

            Role genericRole = roleRepository.save(roleToSend);
            initRoles.add(roleToSend);
        }

        // WHEN
        Iterable<Role> foundRoles = roleService.getRoles();
        List<Role> result = new ArrayList<>();
        foundRoles.forEach(role -> result.add(role));

        // THEN
        assertEquals(initRoles.get(0).getRoleId(), result.get(0).getRoleId());
        assertEquals(initRoles.get(1).getRoleId(), result.get(1).getRoleId());
    }

    @Test
    void get_roleByIdTest() {
        // GIVEN
        Role roleToSend = new Role();
        roleToSend.setRoleId(1);
        roleToSend.setCodeRole("test");

        Role genericRole = roleRepository.save(roleToSend);

        // WHEN
        Optional<Role> foundRole = roleService.getRoleById(genericRole.getRoleId());

        // THEN
        assertEquals(genericRole.getRoleId(), foundRole.get().getRoleId());
    }
}