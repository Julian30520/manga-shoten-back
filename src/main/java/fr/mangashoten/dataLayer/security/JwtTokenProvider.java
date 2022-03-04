package fr.mangashoten.dataLayer.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import fr.mangashoten.dataLayer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.mangashoten.dataLayer.exception.InvalidJWTException;
import fr.mangashoten.dataLayer.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

/**
 * JWT : classe utilitaire chargée de fournir le Jeton (Token) et les vérifications
 */
@Component
public class JwtTokenProvider {

    // on récupère le secret dans notre fichier application.properties
    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    // ici on met la valeur par défaut
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h pour être pénard

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Cette méthode d'initialisation s'exécute avant le constructeur
     * Elle encode notre code secret en base64 pour la transmission dans le header
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * Methode qui crée le Token avec :
     * username comme un champ "sub",
     * User Role comme champ "auth"
     * "iat" comme date du jour ,
     * "exp" as now date + validity time.
     * claims = les droits
     struture :
     HEADER : Algo + Type de Token
     {
     "alg": "HS256",
     "typ": "JWT"
     }

     PAYLOAD : data
     {
     "sub": "pbouget",
     "auth": [
     "ROLE_ADMIN",
     "ROLE_CREATOR",
     "ROLE_READER"
     ],
     "iat": 1589817421,
     "exp": 1589821021
     }

     Signature :

     Signature avec code secret :

     HMACSHA256(
     base64UrlEncode(header) + "." +
     base64UrlEncode(payload),
     03888dd6ceb88c3fee410a70802fb93d483fd52d70349d8f7e7581ae346cf658
     )

     JWT génèrer avec cette info :
     header =   	eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
     payload =  	eyJzdWIiOiJwYm91Z2V0IiwiYXV0aCI6WyJST0xFX0FETUlOIiwiUk9MRV9DUkVBVE9SIiwiUk9MRV9SRUFERVIiXSwiaWF0IjoxNTg5ODE3NDIxLCJleHAiOjE1ODk4MjEwMjF9.
     signature = lrKQIkrCzNMwzTN-hs_EdoYYxrb59sAlku7nmaml0vk

     vérifier sur https://jwt.io

     * @param username the user username.
     * @param roles the user roles.
     * @return the created JWT as String.
     * @throws JsonProcessingException
     */
    public String createToken(String username, Role roles){

        Claims claims = Jwts.claims().setSubject(username);

        claims.put("userId", userRepository.findByUsername(username).get().getUserId());


//       public String createToken(String email, List<Role> roles){
//
//        Claims claims = Jwts.claims().setSubject(email);
//        claims.put("userId", membreRepo.findByEmail(email).get().getId());
//        if(membreRepo.findByEmail(email).get().getTeam() != null){
//            claims.put("teamId", membreRepo.findByEmail(email).get().getTeam().getId());
//        }
        //claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));
        claims.put("auth", new SimpleGrantedAuthority(roles.getAuthority()) );

        System.out.println("claims = "+claims);
        // claims = {sub=pbouget, auth=[ROLE_ADMIN, ROLE_CREATOR, ROLE_READER]}
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        String leToken = Jwts.builder()//
                .setClaims(claims)// le username avec les roles ou setPayload()
                .setIssuedAt(now)// 1589817421  pour le 18 mai 2020 à 17 heure 57
                .setExpiration(validity)// 1589821021 même date avec 1 heure de plus
                .signWith(SignatureAlgorithm.HS256, secretKey) // la signature avec la clef secrête.
                .compact();		// concatène l'ensemble pour construire une chaîne
        System.out.println(leToken); // pour test cela donne ceci
  /*
   		site pour convertir une date en millisecondes : http://timestamp.fr/?
   		site structure du jeton : https://www.vaadata.com/blog/fr/jetons-jwt-et-securite-principes-et-cas-dutilisation/
   		site jwt encoder / décoder : https://jwt.io/
       eyJhbGciOiJIUzI1NiJ9.
       eyJzdWIiOiJwYm91Z2V0IiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9LHsiYXV0aG9yaXR5IjoiUk9MRV9DUkVBVE9SIn0seyJhdXRob3JpdHkiOiJST0xFX1JFQURFUiJ9XSwiaWF0IjoxNTg5ODE2OTIyLCJleHAiOjE1ODk4MjA1MjJ9.
       Cn4_UTjZ2UpJ32FVT3Bd1-VN8K62DVBHQbWiK6MNZ04

  */
        // https://www.codeflow.site/fr/article/java__how-to-convert-java-object-to-from-json-jackson

        return leToken;
    }

    /**
     * Methode qui retourne un objet Authentication basé sur JWT.
     * @param token : le token pour l'authentification.
     * @return the authentication si Username est trouvé.
     */
    public Authentication getAuthentication(String token) throws UsernameNotFoundException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Methode qui extrait le userName du JWT.
     * @param token : Token a analyser.
     * @return le UserName comme chaîne de caractères.
     */
    public String getUsername(String token) {

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Méthode qui récupère la requete HTTP.
     * L'entête doit contenir un champ d'autorisation ou JWT ajoute le token après le mot clef Bearer.
     * @param requeteHttp : la requête à tester.
     * @return le JWT depuis l'entête HTTP.
     */
    public String resolveToken(HttpServletRequest requeteHttp) {
        String bearerToken = requeteHttp.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    /**
     * Methode qui v�rifie que JWT est valide.
     * La signature doit �tre correcte et la dur�e de validit� du Token doit �tre apr�s "now" (maintenant)
     * @param token : Token � valider
     * @return True si le Token est valide sinon on lance l'exception InvalidJWTException.
     * @throws InvalidJWTException
     */
    public boolean validateToken(String token) throws InvalidJWTException {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJWTException();
        }
    }
}
