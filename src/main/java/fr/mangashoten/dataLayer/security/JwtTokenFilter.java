package fr.mangashoten.dataLayer.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import fr.mangashoten.dataLayer.exception.InvalidJWTException;

/**
 * Filtre specifique en charge d'analyser la requête HTTP qui arrive vers notre Serveur et qui doit
 * contenir un JWT valide.
 */
public class JwtTokenFilter extends OncePerRequestFilter {
    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (InvalidJWTException ex) {
            // permet de garantir que le AppUser n'est pas authentifié
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(), "JWT invalide !");
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
