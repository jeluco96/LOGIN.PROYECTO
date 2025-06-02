package com.dam.login.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un grupo o departamento en el sistema.
 * Los grupos permiten organizar a los usuarios por departamentos, equipos educativos
 * o equipos directivos para facilitar la gestión de permisos y accesos.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "grupo")
public class Grupo {
    /**
     * Logger para registrar eventos relacionados con la entidad Grupo.
     */
    private static final Logger logger = LogManager.getLogger(Grupo.class);

    /**
     * Identificador único del grupo generado automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo")
    private Long id;

    /**
     * Nombre único del grupo que lo identifica.
     * No puede ser nulo y debe ser único en el sistema.
     */
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    /**
     * Tipo de grupo (departamento, equipo directivo, etc.).
     * Se almacena como un valor del enum TipoGrupo.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoGrupo tipo;

    /**
     * Descripción opcional que proporciona más información sobre el propósito o ámbito del grupo.
     */
    @Column(name = "descripcion")
    private String descripcion;

    /**
     * Relación con la tabla intermedia que vincula usuarios con grupos.
     * Se excluye de los métodos toString() y equals()/hashCode() para evitar referencias circulares.
     */
    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<UsuarioGrupo> usuariosGrupo = new HashSet<>();

    /**
     * Añade un usuario al grupo estableciendo la relación bidireccional.
     *
     * @param usuario El usuario a añadir al grupo
     * @return La entidad UsuarioGrupo creada
     */
    public UsuarioGrupo agregarUsuario(Usuario usuario) {
        logger.info("Agregando usuario {} al grupo {}", usuario.getEmail(), this.nombre);

        UsuarioGrupo usuarioGrupo = UsuarioGrupo.builder()
                .usuario(usuario)
                .grupo(this)
                .build();

        this.usuariosGrupo.add(usuarioGrupo);
        logger.debug("Usuario {} agregado exitosamente al grupo {}", usuario.getEmail(), this.nombre);

        return usuarioGrupo;
    }

    /**
     * Elimina un usuario del grupo.
     *
     * @param usuario El usuario a eliminar del grupo
     * @return true si el usuario fue eliminado, false si no estaba en el grupo
     */
    public boolean quitarUsuario(Usuario usuario) {
        logger.info("Quitando usuario {} del grupo {}", usuario.getEmail(), this.nombre);

        boolean resultado = this.usuariosGrupo.removeIf(ug -> ug.getUsuario().equals(usuario));

        logger.debug("Usuario {} {} del grupo {}", 
                usuario.getEmail(), 
                resultado ? "eliminado exitosamente" : "no encontrado", 
                this.nombre);

        return resultado;
    }
}