package fr.mangashoten.dataLayer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration de Sécurité globale pour notre REST API.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Methode qui configure la sécurité HTTP.
     * @param http the HttpSecurity object to configure.
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Disable CSRF (Cross Site Request Forgery comme votre Token sera stocké dans le session storage)
        http.cors();

        http.csrf().disable()
                .sessionManagement()
                // Les sessions sont sans états et non créés ni utilisées par Spring security
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // nos endpoints (points d'entrée de notre API)
                .authorizeRequests()
                .antMatchers("/users/sign-in").permitAll() // se connecter
                .antMatchers("/users/sign-up").permitAll() // s'inscrire
                .antMatchers("/manga/**").permitAll()
                .antMatchers("/users/**").hasAuthority("ROLE_USER")
                .antMatchers("/users/all").hasAuthority("ROLE_ADMIN")// que pour le rôle admin
                // on désactive le reste...
                .anyRequest().authenticated();
        // Appliquer JWT
        http.addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

    }

    /**
     * Methode qui configure la sécurité web.
     * Utilisé pour interdire l'accès à certains répertoires.
     * @param web : WebSecurity
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }
}


