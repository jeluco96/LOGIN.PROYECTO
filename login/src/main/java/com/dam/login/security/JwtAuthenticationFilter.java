package com.dam.login.security;

import com.dam.login.model.Usuario;
import com.dam.login.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * Filtro para autenticar solicitudes mediante tokens JWT.
 * Intercepta cada solicitud HTTP y verifica si contiene un token JWT válido.
 * Si el token es válido, establece la autenticación en el contexto de seguridad.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;
    private final JwtTokenProvider tokenProvider;

    /**
     * Determina si este filtro debe aplicarse a la solicitud actual.
     * 
     * @param request La solicitud HTTP
     * @return true si el filtro no debe ejecutarse, false en caso contrario
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        log.debug("JwtAuthenticationFilter: Comprobando si filtrar ruta: {}", path);

        // No filtrar rutas públicas
        if (path.startsWith("/api/auth/") || path.startsWith("/api/health/") || 
            path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/") || 
            path.equals("/error") || path.startsWith("/h2-console") || 
            path.startsWith("/api-docs") || path.startsWith("/swagger-ui")) {
            log.debug("JwtAuthenticationFilter: No filtrando ruta pública: {}", path);
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        log.debug("JwtAuthenticationFilter: Procesando solicitud {}, método {}", uri, request.getMethod());

        // Imprimir todos los headers para diagnóstico
        logRequestHeaders(request);

        try {
            // Intentar obtener token de ambos proveedores
            String token = extractTokenFromRequest(request);

            if (token != null) {
                log.info("JwtAuthenticationFilter: Token encontrado (longitud: {})", token.length());

                boolean isValidJwtUtil = false;
                boolean isValidProvider = false;

                try {
                    isValidJwtUtil = jwtUtil.isTokenValid(token);
                    log.info("JwtAuthenticationFilter: Token validado con JwtUtil: {}", isValidJwtUtil);
                } catch (Exception e) {
                    log.warn("JwtAuthenticationFilter: Error al validar con JwtUtil: {}", e.getMessage());

                try {
                    isValidProvider = tokenProvider.validateToken(token);
                    log.info("JwtAuthenticationFilter: Token validado con TokenProvider: {}", isValidProvider);
                } catch (Exception e) {
                    log.warn("JwtAuthenticationFilter: Error al validar con TokenProvider: {}", e.getMessage());
                }

                log.debug("JwtAuthenticationFilter: Validación de token - JwtUtil: {}, TokenProvider: {}", 
                         isValidJwtUtil, isValidProvider);

                if (isValidJwtUtil || isValidProvider) {
                    String email = null;

                    // Intentar obtener email del token
                    if (isValidJwtUtil) {
                        try {
                            email = jwtUtil.getEmailFromToken(token);
                        } catch (Exception e) {
                            log.debug("Error al obtener email desde JwtUtil: {}", e.getMessage());
                        }
                    }

                    if (email == null && isValidProvider) {
                        try {
                            Long userId = tokenProvider.getUserIdFromToken(token);
                            Optional<Usuario> userOpt = usuarioService.findById(userId);
                            if (userOpt.isPresent()) {
                                email = userOpt.get().getEmail();
                            }
                        } catch (Exception e) {
                            log.debug("Error al obtener email desde TokenProvider: {}", e.getMessage());
                        }
                    }

                    if (email != null) {
                        log.debug("JwtAuthenticationFilter: Token válido para usuario {}", email);

                        // Buscar el usuario en la base de datos
                        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);

                        if (usuarioOpt.isPresent() && usuarioOpt.get().getActivo()) {
                            Usuario usuario = usuarioOpt.get();
                            log.debug("JwtAuthenticationFilter: Usuario encontrado: ID={}, activo={}, rol={}", 
                                     usuario.getId(), usuario.getActivo(), usuario.getRol());

                            // Crear lista de autoridades
                            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()));

                            // Crear autenticación con rol
                            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                authorities
                            );

                            // Establecer detalles
                            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            // Establecer la autenticación en el contexto de seguridad
                            SecurityContextHolder.getContext().setAuthentication(auth);
                            log.debug("JwtAuthenticationFilter: Autenticación establecida para {}", email);
                        } else {
                            log.warn("JwtAuthenticationFilter: Usuario no encontrado o inactivo: {}", email);
                        }
                    } else {
                        log.warn("JwtAuthenticationFilter: No se pudo obtener email del token");
                    }
                } else {
                    log.warn("JwtAuthenticationFilter: Token inválido o expirado");
                }
            } else {
                log.debug("JwtAuthenticationFilter: No se encontró token JWT en la solicitud");
            }
        } catch (Exception e) {
            log.error("JwtAuthenticationFilter: Error no controlado: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);

        // Log después de procesar la solicitud
        log.debug("JwtAuthenticationFilter: Solicitud {} procesada con código de estado {}", 
                uri, response.getStatus());
    }

    /**
     * Extrae el token JWT del header Authorization de la solicitud.
     *
     * @param request La solicitud HTTP
     * @return El token JWT o null si no se encuentra
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.debug("JwtAuthenticationFilter: Header Authorization: {}", bearerToken);

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Registra en el log todos los headers de la solicitud para diagnóstico.
     * 
     * @param request La solicitud HTTP
     */
    private void logRequestHeaders(HttpServletRequest request) {
        if (log.isDebugEnabled()) {
            Enumeration<String> headerNames = request.getHeaderNames();
            StringBuilder headers = new StringBuilder("Headers de la solicitud:\n");

            while (headerNames != null && headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = headerName.equalsIgnoreCase("authorization") ? 
                        "[PROTECTED]" : request.getHeader(headerName);
                headers.append(headerName).append(": ").append(headerValue).append("\n");
            }

            log.debug(headers.toString());
        }
    }
}
