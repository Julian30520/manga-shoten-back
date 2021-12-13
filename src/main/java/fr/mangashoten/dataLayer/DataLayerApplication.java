package fr.mangashoten.dataLayer;

import fr.mangashoten.dataLayer.model.Author;
import fr.mangashoten.dataLayer.model.Role;
import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.service.AuthorService;
import fr.mangashoten.dataLayer.service.RoleService;
import fr.mangashoten.dataLayer.service.TomeService;
import fr.mangashoten.dataLayer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

	@Autowired
	private AuthorService authorService;

	public static void main(String[] args) {
		SpringApplication.run(DataLayerApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		/*Tome newTome = new Tome();
		newTome.setPageNumber(24);
		newTome.setTomeNumber(56);
		newTome.setCover("urlll");

		newTome = tomeService.addTome(newTome);*/

		//Delete user
		//userService.deleteUser(userService.getUserByUsername("Genevas").get());

		//Add new user
		/*User newUser = new User();
		newUser.setUsername("Genevas");
		newUser.setMail("juliantomczyk@gmail.com");
		newUser.setAvatar("urlAvatar");
		newUser.setFirstName("Julian");
		newUser.setLastName("Tomczyk");
		newUser.setPassword("password");
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse("23/03/1995");
		newUser.setDateOfBirth(date);
		newUser.setRole(roleService.getRoleById(3).get());
		newUser = userService.addUser(newUser);*/

		//Add or delete tome from user
		//userService.getUserByUsername("Genevas").get().addTome(tomeService.getTomeById(8).get());

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
		Iterable<Tome> tomes = userRes.getTomes();
		tomes.forEach(tome -> System.out.println((tome.getTomeId())));*/

		//Find user by username
		/*Optional<User> user = userService.getUserByUsername("dtebbut8");
		if(user.isPresent()) {
			System.out.println(user.get().getUsername());
			System.out.println(user.get().getRole().getCodeRole());
		}
		else System.out.println("Unknow username");*/

		//Find all users
		/*Iterable<User> users = userService.getUsers();
		users.forEach(user -> System.out.println(user.getFirstName()));*/

		//Find all roles
		/*Iterable<Role> roles = roleService.getRoles();
		roles.forEach((role -> System.out.println(role.getCodeRole())));*/

		 //Find All Author
//		Iterable<Author> authors = authorService.getAllAuthor();
//		authors.forEach((author -> System.out.println(author.getFirstName()+ " "+ author.getLastName())));

		//Find Author By Id
//		Optional<Author> author = authorService.getAuthorById(10);
//		System.out.println(author.get().getFirstName()+author.get().getLastName());

		//Add Author
//		Author murata = new Author();
//		murata.setFirstName("Yusuke ");
//		murata.setLastName("MURATA");
//		murata.setAuthorId(11);
//
//		authorService.addAuthor(murata);

		//delete Author

//		authorService.deleteAuthor(11);

	}
}
