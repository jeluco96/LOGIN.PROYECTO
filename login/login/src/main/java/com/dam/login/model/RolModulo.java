package com.dam.login.model;

import jakarta.persistence.*;
import lombok.*;
/**
 * Entidad que representa la relación muchos a muchos entre roles y módulos.
 * Esta clase implementa una tabla de unión que permite asignar módulos a roles,
 * facilitando la gestión de permisos basados en roles en el sistema.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rol_modulo")
public class RolModulo {

    /**
     * Identificador único de la relación rol-módulo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Rol asociado a un módulo.
     * Establece la relación muchos a uno con la entidad de roles del sistema.
     */
    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    /**
     * Módulo asociado a un rol.
     * Establece la relación muchos a uno con la entidad de módulos del sistema.
     */
    @ManyToOne
    @JoinColumn(name = "modulo_id")
    private Modulo modulo;
}
