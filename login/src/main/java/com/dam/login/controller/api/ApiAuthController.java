package com.dam.login.controller.api;

import com.dam.login.dto.LoginRequest;
import com.dam.login.dto.AuthResponse;
import com.dam.login.model.Usuario;
import com.dam.login.security.JwtTokenProvider;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para la API de autenticación.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "API Autenticación", description = "Endpoints para autenticación de usuarios")
public class ApiAuthController {

    private final UsuarioService usuarioService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Endpoint para iniciar sesión y obtener un token JWT.
     */
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y retorna token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            log.debug("Intento de login API para: {}", loginRequest.getEmail());

            // Autenticar usuario mediante el servicio
            Usuario usuario = usuarioService.login(loginRequest.getEmail(), loginRequest.getClave());

            // Generar token JWT
            String token = jwtTokenProvider.generateToken(usuario);

            // Obtener el nombre del rol como String
            String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombre() : "DESCONOCIDO";

            // Crear respuesta con token y datos básicos del usuario
            AuthResponse response = new AuthResponse(
                token,
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getEmail(),
                rolNombre
            );

            log.info("Login API exitoso para: {}", loginRequest.getEmail());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.warn("Error en login API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                        "error", "Credenciales inválidas", 
                        "mensaje", e.getMessage(),
                        "timestamp", LocalDateTime.now().toString()
                    ));
        }
    }

    /**
     * Endpoint para validar un token JWT.
     */
    @Operation(summary = "Validar token JWT", description = "Verifica si un token JWT es válido")
    @GetMapping("/validar-token")
    public ResponseEntity<?> validarToken(@RequestParam String token) {
        log.debug("Validando token API: {}...", token.substring(0, Math.min(10, token.length())));

        boolean isValid = jwtTokenProvider.validateToken(token);

        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);

        if (isValid) {
            try {
                Long userId = jwtTokenProvider.getUserIdFromToken(token);
                response.put("userId", userId);
            } catch (Exception e) {
                log.warn("No se pudo extraer userId del token: {}", e.getMessage());
            }
        }

        return ResponseEntity.ok(response);
    }
}
