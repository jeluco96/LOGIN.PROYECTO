package com.dam.login.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que define los diferentes roles disponibles para los usuarios en el sistema.
 * Cada rol determina un conjunto de permisos y funciones que un usuario puede realizar.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rol")
public class Rol {

    /**
     * Logger para registrar eventos relacionados con la entidad Rol.
     */
    private static final Logger logger = LogManager.getLogger(Rol.class);

    /**
     * Identificador único del rol generado automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long id;

    /**
     * Nombre único del rol que lo identifica funcionalmente.
     * No puede ser nulo y debe ser único en el sistema.
     */
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    /**
     * Descripción detallada del rol y sus funciones en el sistema.
     */
    @Column(name = "descripcion")
    private String descripcion;

    /**
     * Conjunto de relaciones entre roles y módulos.
     */
    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RolModulo> modulosRol = new HashSet<>();

    /**
     * Usuarios que tienen asignado este rol como rol principal.
     */
    @OneToMany(mappedBy = "rol")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Usuario> usuariosPrincipales = new HashSet<>();

    /**
     * Usuarios que tienen asignado este rol como rol adicional.
     */
    @ManyToMany(mappedBy = "roles")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Usuario> usuarios = new HashSet<>();

    /**
     * Verifica si el rol tiene acceso a un módulo específico.
     *
     * @param nombreModulo Nombre del módulo a verificar
     * @return true si tiene acceso, false en caso contrario
     */
    public boolean tieneAccesoAModulo(String nombreModulo) {
        logger.debug("Verificando acceso al módulo {} para el rol {}", nombreModulo, this.nombre);

        // Los roles de tipo ADMINISTRADOR tienen acceso a todos los módulos
        if (TipoRol.ADMINISTRADOR.equals(this.nombre)) {
            logger.debug("Rol ADMINISTRADOR: acceso concedido automáticamente al módulo {}", nombreModulo);
            return true;
        }

        boolean tieneAcceso = this.modulosRol.stream()
                .anyMatch(rm -> rm.getModulo().getNombre().equals(nombreModulo) &&
                           rm.getModulo().getActivo());

        logger.debug("Rol {}: {}tiene acceso al módulo {}",
                this.nombre, tieneAcceso ? "" : "no ", nombreModulo);

        return tieneAcceso;
    }

    /**
     * Asigna un módulo a este rol a través de la relación RolModulo.
     * 
     * @param modulo El módulo a asignar al rol
     * @return La entidad RolModulo creada
     */
    public RolModulo asignarModulo(Modulo modulo) {
        logger.info("Asignando módulo {} al rol {}", modulo.getNombre(), this.nombre);

        // Verificar si el módulo ya está asignado
        boolean yaAsignado = this.modulosRol.stream()
                .anyMatch(rm -> rm.getModulo().equals(modulo));

        if (yaAsignado) {
            logger.debug("Módulo {} ya estaba asignado al rol {}", modulo.getNombre(), this.nombre);
            return this.modulosRol.stream()
                    .filter(rm -> rm.getModulo().equals(modulo))
                    .findFirst()
                    .orElse(null);
        }

        RolModulo rolModulo = RolModulo.builder()
                .rol(this)
                .modulo(modulo)
                .build();

        this.modulosRol.add(rolModulo);
        logger.debug("Módulo {} asignado exitosamente al rol {}", modulo.getNombre(), this.nombre);

        return rolModulo;
    }

    /**
     * Quita un módulo de este rol.
     * 
     * @param modulo El módulo a quitar del rol
     * @return true si el módulo fue eliminado, false si no estaba asignado
     */
    public boolean quitarModulo(Modulo modulo) {
        logger.info("Quitando módulo {} del rol {}", modulo.getNombre(), this.nombre);

        boolean resultado = this.modulosRol.removeIf(rm -> rm.getModulo().equals(modulo));

        logger.debug("Módulo {} {} del rol {}", 
                modulo.getNombre(), 
                resultado ? "eliminado exitosamente" : "no estaba asignado", 
                this.nombre);

        return resultado;
    }

    /**
     * Verifica si este rol es de tipo administrador.
     * 
     * @return true si es un rol de administrador, false en caso contrario
     */
    public boolean esAdministrador() {
        return TipoRol.ADMINISTRADOR.equals(this.nombre);
    }

    /**
     * Verifica si este rol es de tipo profesor.
     * 
     * @return true si es un rol de profesor, false en caso contrario
     */
    public boolean esProfesor() {
        return TipoRol.PROFESOR.equals(this.nombre);
    }

    /**
     * Verifica si este rol es de tipo técnico.
     * 
     * @return true si es un rol de técnico, false en caso contrario
     */
    public boolean esTecnico() {
        return TipoRol.TECNICO.equals(this.nombre);
    }

    /**
     * Verifica si este rol es de tipo gestor de incidencias.
     * 
     * @return true si es un rol de gestor de incidencias, false en caso contrario
     */
    public boolean esGestorIncidencias() {
        return TipoRol.GESTOR_INCIDENCIAS.equals(this.nombre);
    }

    /**
     * Tipos predefinidos de roles en el sistema.
     */
    public static class TipoRol {
        public static final String ADMINISTRADOR = "ADMINISTRADOR";
        public static final String PROFESOR = "PROFESOR";
        public static final String GESTOR_INCIDENCIAS = "GESTOR_INCIDENCIAS";
        public static final String TECNICO = "TECNICO";
    }
    }
