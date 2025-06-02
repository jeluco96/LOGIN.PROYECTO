package com.dam.login.controller;

import com.dam.login.dto.AuthResponse;
import com.dam.login.dto.LoginRequest;
import com.dam.login.model.Usuario;
import com.dam.login.security.JwtUtil;
import com.dam.login.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para gestionar la autenticación de usuarios.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticación", description = "API para gestión de autenticación")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    /**
     * Endpoint para iniciar sesión y obtener un token JWT.
     *
     * @param loginRequest Datos de inicio de sesión
     * @return Respuesta con el token JWT y datos del usuario
     */
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa", 
                     content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "403", description = "Usuario desactivado")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        log.info("Intento de login para usuario: {}", loginRequest.getEmail());

        try {
            // Autenticar usuario con el servicio existente
            Usuario usuario = usuarioService.login(loginRequest.getEmail(), loginRequest.getClave());
            log.debug("Usuario autenticado correctamente: id={}, nombre={}, rol={}",
                    usuario.getId(), usuario.getNombre(), usuario.getRol());

            // Generar token JWT
            String token = jwtUtil.generateToken(usuario.getEmail());
            log.debug("Token JWT generado, longitud: {}", token.length());

            // Construir respuesta
            AuthResponse authResponse = new AuthResponse(
                token,
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getEmail(),
                usuario.getRol().toString()
            );

            log.info("Login exitoso para usuario: {}", loginRequest.getEmail());
            return ResponseEntity.ok(authResponse);

        } catch (RuntimeException e) {
            log.warn("Error en login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error de autenticación: " + e.getMessage());
        }
    }

    /**
     * Endpoint para validar un token JWT.
     * Útil para diagnosticar problemas con tokens.
     *
     * @param token Token JWT a validar
     * @return Información sobre el token
     */
    @GetMapping("/validate-token")
    @Operation(summary = "Validar token", description = "Valida un token JWT y devuelve información sobre él")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        log.info("Solicitud para validar token, longitud: {}", token.length());

        Map<String, Object> response = new HashMap<>();

        try {
            boolean isValid = jwtUtil.isTokenValid(token);
            response.put("valid", isValid);

            if (isValid) {
                String email = jwtUtil.getEmailFromToken(token);
                response.put("email", email);
                response.put("expirationDate", jwtUtil.getExpirationDateFromToken(token));

                usuarioService.buscarPorEmail(email).ifPresent(usuario -> {
                    response.put("userId", usuario.getId());
                    response.put("userActive", usuario.getActivo());
                    response.put("userRole", usuario.getRol());
                });
            }

            log.info("Token validado: {}", isValid);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al validar token: {}", e.getMessage());
            response.put("valid", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    /**
     * Endpoint para verificar el estado de autenticación.
     * Utilizado principalmente para diagnóstico.
     *
     * @return Estado de la autenticación actual
     */
    @GetMapping("/check")
    @Operation(summary = "Verificar autenticación", description = "Comprueba si la solicitud está autenticada")
    public ResponseEntity<?> checkAuth() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", "UP");
        return ResponseEntity.ok(response);
    }
}
