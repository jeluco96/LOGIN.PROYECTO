package com.dam.login.controller.auth;

import com.dam.login.security.JwtTokenProvider;
import com.dam.login.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para validar tokens JWT.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthValidationController {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;

    /**
     * Valida un token JWT.
     * 
     * @param token Token JWT a validar
     * @return Respuesta con el resultado de la validación
     */
    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        log.debug("Validando token: {}...", token.substring(0, Math.min(10, token.length())));

        Map<String, Object> response = new HashMap<>();

        boolean isValidJwtProvider = false;
        boolean isValidJwtUtil = false;

        try {
            isValidJwtProvider = jwtTokenProvider.validateToken(token);
        } catch (Exception e) {
            log.debug("Error al validar token con JwtTokenProvider: {}", e.getMessage());
        }

        try {
            isValidJwtUtil = jwtUtil.isTokenValid(token);
        } catch (Exception e) {
            log.debug("Error al validar token con JwtUtil: {}", e.getMessage());
        }

        boolean isValid = isValidJwtProvider || isValidJwtUtil;
        log.debug("Resultado de validación: JwtTokenProvider={}, JwtUtil={}, Final={}", 
                 isValidJwtProvider, isValidJwtUtil, isValid);

        response.put("valid", isValid);
        response.put("validJwtProvider", isValidJwtProvider);
        response.put("validJwtUtil", isValidJwtUtil);

        return ResponseEntity.ok(response);
    }
}
