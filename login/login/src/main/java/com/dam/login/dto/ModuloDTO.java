package com.dam.login.dto;

import com.dam.login.model.Modulo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferir información básica de módulos sin relaciones circulares.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuloDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean activo;

    /**
     * Crea un DTO a partir de una entidad Modulo.
     * 
     * @param modulo Entidad de módulo
     * @return DTO con información básica del módulo
     */
    public static ModuloDTO fromEntity(Modulo modulo) {
        if (modulo == null) {
            return null;
        }

        ModuloDTO dto = new ModuloDTO();
        dto.setId(modulo.getId());
        dto.setNombre(modulo.getNombre());
        dto.setDescripcion(modulo.getDescripcion());
        dto.setActivo(modulo.getActivo());

        return dto;
    }
}
