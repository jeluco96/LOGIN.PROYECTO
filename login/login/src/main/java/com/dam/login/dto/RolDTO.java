package com.dam.login.dto;

import com.dam.login.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferir información básica de roles sin relaciones circulares.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolDTO {
    private Long id;
    private String nombre;
    private String descripcion;

    /**
     * Crea un DTO a partir de una entidad Rol.
     * 
     * @param rol Entidad de rol
     * @return DTO con información básica del rol
     */
    public static RolDTO fromEntity(Rol rol) {
        if (rol == null) {
            return null;
        }

        RolDTO dto = new RolDTO();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());
        dto.setDescripcion(rol.getDescripcion());

        return dto;
    }
}
