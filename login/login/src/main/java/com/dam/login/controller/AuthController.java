package com.dam.login.controller;

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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Controlador REST que maneja la autenticación de usuarios y gestión de tokens.
 * Este controlador implementa los endpoints específicos para autenticación.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "API para la gestión de autenticación de usuarios")
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    private final UsuarioService usuarioService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Endpoint para iniciar sesión y obtener un token JWT.
     * 
     * @param loginRequest Objeto con las credenciales del usuario (email y clave)
     * @return ResponseEntity con token JWT y datos básicos del usuario autenticado
     */
    @Operation(
        summary = "Iniciar sesión de usuario",
        description = "Autentica a un usuario y devuelve un token JWT si las credenciales son válidas"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Autenticación exitosa",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))}
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Credenciales inválidas",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de solicitud inválidos",
            content = @Content
        )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Intento de login para: " + loginRequest.getEmail());

            if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getClave() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Se requiere email y contraseña");
            }

            // Autenticar usuario mediante el servicio
            Usuario usuario = usuarioService.login(loginRequest.getEmail(), loginRequest.getClave());

            if (usuario == null) {
                logger.warning("Usuario no encontrado: " + loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Credenciales inválidas");
            }

            // Verificar si el usuario está activo
            if (!usuario.isActivo()) {
                logger.warning("Intento de acceso de usuario inactivo: " + loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Usuario inactivo. Contacte al administrador.");
            }

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

            logger.info("Login exitoso para: " + loginRequest.getEmail());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.severe("Error en login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error de autenticación: " + e.getMessage());
        }
    }

    /**
     * Endpoint para cerrar sesión de usuario.
     * Como JWT es stateless, el cierre de sesión debe manejarse en el cliente eliminando el token.
     * Este endpoint existe para ofrecer una interfaz coherente y para posibles implementaciones futuras
     * como listas de tokens revocados.
     * 
     * @param request La solicitud HTTP que contiene la cabecera de autorización
     * @return ResponseEntity con mensaje de éxito
     */
    @Operation(
        summary = "Cerrar sesión de usuario",
        description = "Cierra la sesión del usuario actual invalidando su token JWT"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Sesión cerrada exitosamente",
            content = {@Content(mediaType = "application/json")}
        )
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            // Extraer el token de la cabecera de autorización
            String bearerToken = request.getHeader("Authorization");
            String token = null;

            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                token = bearerToken.substring(7);
                logger.info("Cerrando sesión para token: " + token.substring(0, Math.min(10, token.length())) + "...");

                // Invalidar el token añadiéndolo a la lista negra
                jwtTokenProvider.invalidateToken(token);
            }

            // Incluso si no hay token, devolver éxito para mantener la interfaz coherente
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Sesión cerrada exitosamente");
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.warning("Error en logout: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cerrar sesión: " + e.getMessage());
        }
    }
}