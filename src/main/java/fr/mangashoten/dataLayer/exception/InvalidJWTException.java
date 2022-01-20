package fr.mangashoten.dataLayer.exception;

/**
 * Specific exception that should be thrown when a JWT has an invalid format.
 */
public class InvalidJWTException extends Exception {
	
	private static final long serialVersionUID = -6546999838071338632L;

	@Override
	public String getMessage()
	{
		return "Le format JWT est invalide !"; 
	}

}
