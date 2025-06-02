package com.dam.login.dto;

import com.dam.login.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferir información básica de usuarios sin relaciones circulares.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String email;
    private Boolean activo;
    private String grupo;
    private String rol;
    private Boolean usuarioMovil;

    /**
     * Crea un DTO a partir de una entidad Usuario.
     * 
     * @param usuario Entidad de usuario
     * @return DTO con información básica del usuario
     */
    public static UsuarioDTO fromEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellidos(usuario.getApellidos());
        dto.setEmail(usuario.getEmail());
        dto.setActivo(usuario.getActivo());
        dto.setGrupo(usuario.getGrupo());
        dto.setUsuarioMovil(usuario.getUsuarioMovil());

        // Evitar NPE(nullPointerException) si el rol es nulo
        if (usuario.getRol() != null) {
            dto.setRol(usuario.getRol().getNombre());
        }

        return dto;
    }
}
