package fr.mangashoten.dataLayer.dto;

/**
 * Classe spécifique DTO (Data Transfer Object) qui retourne un Jeton au format JSON (REST response)
 *
 */
public class JsonWebToken {
    private final String token;

    public JsonWebToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
