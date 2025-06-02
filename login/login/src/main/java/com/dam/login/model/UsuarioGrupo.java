package com.dam.login.model;

import com.dam.login.model.Grupo;
import com.dam.login.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa la relación muchos a muchos entre usuarios y grupos.
 * Esta clase implementa una tabla de unión que permite asignar usuarios a múltiples grupos
 * y grupos a múltiples usuarios, manteniendo la integridad referencial.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuario_grupo")
public class UsuarioGrupo {
    /**
     * Identificador único de la relación usuario-grupo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Usuario que pertenece al grupo.
     * Establece la relación muchos a uno con la entidad Usuario.
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    /**
     * Grupo al que pertenece el usuario.
     * Establece la relación muchos a uno con la entidad Grupo.
     */
    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;
}