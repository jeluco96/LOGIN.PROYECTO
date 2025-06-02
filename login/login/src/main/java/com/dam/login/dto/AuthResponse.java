package com.dam.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuestas de autenticación que contiene el token JWT y datos básicos del usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de autenticación con token JWT y datos del usuario")
public class AuthResponse {

    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "ID del usuario autenticado", example = "1")
    private Long userId;

    @Schema(description = "Nombre del usuario", example = "Administrador")
    private String nombre;

    @Schema(description = "Apellidos del usuario", example = "Del Sistema")
    private String apellidos;

    @Schema(description = "Correo electrónico del usuario", example = "admin@guzpasen.edu")
    private String email;

    @Schema(description = "Rol del usuario en el sistema", example = "ADMIN")
    private String rol;
}
