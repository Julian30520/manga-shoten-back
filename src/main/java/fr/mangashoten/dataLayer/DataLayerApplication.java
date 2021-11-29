package fr.mangashoten.dataLayer;

import fr.mangashoten.dataLayer.model.Role;
import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.service.RoleService;
import fr.mangashoten.dataLayer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class DataLayerApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	public static void main(String[] args) {
		SpringApplication.run(DataLayerApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		/*Optional<User> user = userService.getUserByUsername("dtebbut8");
		if(user.isPresent()) {
			System.out.println(user.get().getUsername());
			System.out.println(user.get().getRole().getCodeRole());
		}
		else System.out.println("Unknow username");*/

		/*Iterable<User> users = userService.getUsers();
		users.forEach(user -> System.out.println(user.getFirstName()));*/

		/*Iterable<Role> roles = roleService.getRoles();
		roles.forEach((role -> System.out.println(role.getCodeRole())));*/
	}
}
