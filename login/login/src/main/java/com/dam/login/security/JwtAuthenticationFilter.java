package com.dam.login.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Filtro para autenticar solicitudes mediante tokens JWT.
 * Intercepta cada solicitud HTTP y verifica si contiene un token JWT válido.
 * Si el token es válido, establece la autenticación en el contexto de seguridad.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Determina si este filtro debe aplicarse a la solicitud actual.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        logger.info("Comprobando si filtrar ruta: " + path);

        // Aplicar filtro solo a rutas API que no sean de autenticación
        if (!path.startsWith("/api/")) {
            return true; // No filtrar rutas web normales
        }

        // No filtrar rutas de autenticación API
        return path.startsWith("/api/auth/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            // Extraer token de la solicitud
            String token = extractTokenFromRequest(request);

            // Si hay token y es válido, establecer autenticación
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Long userId = jwtTokenProvider.getUserIdFromToken(token);

                // Crear autenticación simple para permitir acceso
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userId.toString(), null, Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_USER")
                )
                );

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);

                logger.info("Usuario autenticado correctamente con ID: " + userId);
            }
        } catch (Exception e) {
            logger.warning("Error en filtro JWT: " + e.getMessage());
            // No establecer autenticación si hay error
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT del header Authorization de la solicitud.
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}