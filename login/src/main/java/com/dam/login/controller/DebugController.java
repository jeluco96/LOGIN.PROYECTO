package com.dam.login.controller;

import com.dam.login.model.Usuario;
import com.dam.login.security.JwtUtil;
import com.dam.login.security.JwtTokenProvider;
import com.dam.login.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador para depuración de problemas de autenticación y seguridad.
 * Solo disponible en perfiles de desarrollo y test.
 */
@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@Slf4j
@Profile({"dev", "test"}) // Asegurar que solo esté disponible en entornos de desarrollo y pruebas
public class DebugController {

    private final JwtUtil jwtUtil;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioService usuarioService;

    /**
     * Endpoint para analizar un token JWT y mostrar información detallada.
     * 
     * @param token Token JWT a analizar
     * @return Información detallada del token
     */
    @GetMapping("/token")
    public ResponseEntity<?> analyzeToken(@RequestParam String token) {
        log.debug("Analizando token: {}", token.substring(0, Math.min(10, token.length())) + "...");

        Map<String, Object> result = new HashMap<>();
        result.put("token", token.substring(0, Math.min(10, token.length())) + "...");
        result.put("length", token.length());

        // Validar con JwtUtil
        try {
            boolean isValidJwtUtil = jwtUtil.isTokenValid(token);
            result.put("isValidJwtUtil", isValidJwtUtil);

            if (isValidJwtUtil) {
                String email = jwtUtil.getEmailFromToken(token);
                result.put("email_from_jwtUtil", email);
                result.put("expiration_from_jwtUtil", jwtUtil.getExpirationDateFromToken(token));
            }
        } catch (Exception e) {
            result.put("jwtUtil_error", e.getMessage());
        }

        // Validar con TokenProvider
        try {
            boolean isValidProvider = tokenProvider.validateToken(token);
            result.put("isValidTokenProvider", isValidProvider);

            if (isValidProvider) {
                Long userId = tokenProvider.getUserIdFromToken(token);
                result.put("userId_from_provider", userId);

                Optional<Usuario> userOpt = usuarioService.findById(userId);
                if (userOpt.isPresent()) {
                    Usuario user = userOpt.get();
                    result.put("user_from_provider", Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "nombre", user.getNombre(),
                        "activo", user.getActivo(),
                        "rol", user.getRol()
                    ));
                }
            }
        } catch (Exception e) {
            result.put("tokenProvider_error", e.getMessage());
        }

        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint para obtener información sobre la autenticación actual.
     * 
     * @return Información del contexto de seguridad actual
     */
    @GetMapping("/auth")
    public ResponseEntity<?> getAuthInfo() {
        Map<String, Object> result = new HashMap<>();

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            result.put("authenticated", authentication.isAuthenticated());
            result.put("principal", authentication.getPrincipal());
            result.put("authorities", authentication.getAuthorities());
            result.put("details", authentication.getDetails());
            result.put("type", authentication.getClass().getName());
        } else {
            result.put("authenticated", false);
            result.put("message", "No hay autenticación en el contexto de seguridad");
        }

        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint para forzar la autenticación de un usuario.
     * Solo para propósitos de depuración.
     * 
     * @param email Email del usuario a autenticar
     * @return Resultado de la operación
     */
    @PostMapping("/force-auth")
    public ResponseEntity<?> forceAuthentication(@RequestParam String email) {
        Map<String, Object> result = new HashMap<>();

        Optional<Usuario> userOpt = usuarioService.buscarPorEmail(email);
        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            if (user.getActivo()) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRol()))
                );

                SecurityContextHolder.getContext().setAuthentication(auth);

                result.put("success", true);
                result.put("message", "Autenticación forzada para usuario: " + email);
                result.put("user", user.getNombre());
                result.put("role", user.getRol());
            } else {
                result.put("success", false);
                result.put("message", "Usuario inactivo: " + email);
            }
        } else {
            result.put("success", false);
            result.put("message", "Usuario no encontrado: " + email);
        }

        return ResponseEntity.ok(result);
    }
}
