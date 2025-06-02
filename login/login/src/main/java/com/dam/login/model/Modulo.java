package com.dam.login.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un módulo funcional del sistema.
 * Cada módulo puede ser asignado a múltiples usuarios y representa una funcionalidad 
 * o área específica de la aplicación a la que se puede dar acceso.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "modulo")
public class Modulo {
    /**
     * Logger para registrar eventos relacionados con la entidad Modulo.
     */
    private static final Logger logger = LogManager.getLogger(Modulo.class);
    /**
     * Identificador único del módulo generado automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modulo")
    private Long id;
    
    /**
     * Nombre único del módulo que lo identifica funcionalmente.
     * No puede ser nulo y debe ser único en el sistema.
     */
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    /**
     * Descripción detallada de la funcionalidad o propósito del módulo.
     * Es opcional y proporciona información adicional sobre lo que hace el módulo.
     */
    @Column(name = "descripcion")
    private String descripcion;
    
    /**
     * Conjunto de usuarios que tienen acceso a este módulo.
     * Relación bidireccional con la entidad Usuario.
     * Se excluye de los métodos toString() y equals()/hashCode() para evitar referencias circulares.
     */
    @ManyToMany(mappedBy = "modulos")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Usuario> usuarios = new HashSet<>();
    
    // Métodos de utilidad
    /**
     * Asigna este módulo a un usuario específico.
     * 
     * @param usuario El usuario al que se le asignará el módulo
     * @return true si el usuario fue añadido a la colección, false si ya estaba asignado
     */
    public boolean asignarAUsuario(Usuario usuario) {
        logger.info("Asignando módulo {} (ID: {}) al usuario {}", nombre, id, usuario.getEmail());
        boolean resultado = this.usuarios.add(usuario);
        logger.debug("Módulo {} {} asignado al usuario {}", 
                nombre, resultado ? "exitosamente" : "ya estaba", usuario.getEmail());
        return resultado;
    }
    
    /**
     * Revoca el acceso de un usuario a este módulo.
     * 
     * @param usuario El usuario al que se le revocará el acceso al módulo
     * @return true si el usuario fue eliminado de la colección, false si no estaba asignado
     */
    public boolean quitarDeUsuario(Usuario usuario) {
        logger.info("Quitando módulo {} (ID: {}) del usuario {}", nombre, id, usuario.getEmail());
        boolean resultado = this.usuarios.remove(usuario);
        logger.debug("Módulo {} {} quitado del usuario {}", 
                nombre, resultado ? "exitosamente" : "no estaba asignado a", usuario.getEmail());
        return resultado;
    }

    /**
     * Indica si el módulo está activo en el sistema.
     * Un módulo inactivo no es accesible para ningún usuario.
     * -- GETTER --
     *  Obtiene el estado activo del módulo.
     *

     */
    @Column(name = "activo")
    private Boolean activo = true;

    /**
     * Establece el estado activo del módulo.
     * 
     * @param activo el nuevo estado a establecer
     */
    public void setActivo(Boolean activo) {
        this.activo = activo;
        logger.info("Módulo {} (ID: {}) {} activo", nombre, id, 
                   activo ? "marcado como" : "marcado como NO");
    }


    public String getRoles() {
        StringBuilder rolesStr = new StringBuilder();
        for (Usuario usuario : usuarios) {
            if (usuario.getRol() != null) {
                rolesStr.append(usuario.getRol().getNombre()).append(", ");
            }
        }
        return !rolesStr.isEmpty() ? rolesStr.substring(0, rolesStr.length() - 2) : "ninguno";
    }
}
