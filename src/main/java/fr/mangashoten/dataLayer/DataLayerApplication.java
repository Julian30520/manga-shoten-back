package fr.mangashoten.dataLayer;

import fr.mangashoten.dataLayer.model.User;
import fr.mangashoten.dataLayer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataLayerApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(DataLayerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		Iterable<User> users = userService.getUsers();
//		users.forEach(user -> System.out.println(user.toString()));
	}
}
