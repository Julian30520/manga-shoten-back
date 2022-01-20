package fr.mangashoten.dataLayer;

import fr.mangashoten.dataLayer.model.*;
import fr.mangashoten.dataLayer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

	@Autowired
	private MangaService mangaService;

	@Autowired
	private GenreService genreService;
	public static void main(String[] args) {
		SpringApplication.run(DataLayerApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

	}

	/**
	 * Ceci est un Bean, un composant
	 * Méthode de Hachage
	 * Bcrypt est un algorithme de hachage considéré comme le plus sûr.
	 * bcrypt est un algorithme de hashage unidirectionnel,
	 * vous ne pourrez jamais retrouver le mot de passe sans connaitre à la fois le grain de sel,
	 * la clé et les différentes passes que l'algorithme à utiliser.
	 * Voir le <a href="https://bcrypt-generator.com/"> site pour effectuer un test</a>
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
