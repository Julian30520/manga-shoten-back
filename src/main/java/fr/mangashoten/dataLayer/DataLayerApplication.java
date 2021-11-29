package fr.mangashoten.dataLayer;

import fr.mangashoten.dataLayer.model.Role;
import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.service.RoleService;
import fr.mangashoten.dataLayer.service.TomeService;
import fr.mangashoten.dataLayer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class DataLayerApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private TomeService tomeService;

	public static void main(String[] args) {
		SpringApplication.run(DataLayerApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		//Add or delete tome from user
		/*Optional<User> user = userService.getUserByUsername("cknatt3");
		User userRes = user.get();
		System.out.println((userRes.getUsername()));
		System.out.println((userRes.getUserId()));
		Optional<Tome> tome = tomeService.getTomeById(8);
		Tome tomeRes = tome.get();
		//userRes.addTome(tomeRes);
		//userRes.removeTome(tomeRes);*/

		//Content of user_tome table here (find all tomes of all users)
		/*Iterable<User> users = userService.getUsers();
		users.forEach(user -> user.getTomes().forEach(tome -> System.out.println(tome.getTomeId())));*/
		// ==
		/*for (User user : users) {
			System.out.print(user.getUserId() + " | ");
			user.getTomes().forEach(tome -> System.out.println(tome.getTomeId()));
			System.out.println("------------------------");
		}*/

		//Find all tome from a username
		/*Optional<User> user = userService.getUserByUsername("cknatt3");
		User userRes = user.get();
		System.out.println((userRes.getUsername()));
		System.out.println((userRes.getUserId()));
		Iterable<Tome> tomes = userRes.getTomes();
		tomes.forEach(tome -> System.out.println((tome.getTomeId())));*/

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
