package fr.mangashoten.dataLayer.exception;

/**
 * Classe personnalisée pour gérer un message si l'utilisateur (User) existe en Base de données
 */
public class ExistingUsernameOrMailException extends Exception {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage()
	{
		return "Désolé, l'utilisateur ou le mail existe déjà  !";
	}
}
