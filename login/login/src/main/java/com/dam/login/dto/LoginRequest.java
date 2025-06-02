package com.dam.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para recibir peticiones de inicio de sesión.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud de inicio de sesión")
public class LoginRequest {

    @Schema(description = "Correo electrónico del usuario", example = "admin@guzpasen.edu", required = true)
    private String email;

    @Schema(description = "Contraseña del usuario", example = "password", required = true)
    private String clave;
}
