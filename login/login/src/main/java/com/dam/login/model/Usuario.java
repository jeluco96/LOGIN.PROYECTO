package com.dam.login.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un usuario del sistema.
 * Contiene información personal, credenciales y permisos de acceso.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuario")
public class Usuario {

    /**
     * Logger para registrar eventos relacionados con la entidad Usuario.
     */
    private static final Logger logger = LogManager.getLogger(Usuario.class);

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    /**
     * Nombre del usuario.
     */
    @Column(name = "nombre", nullable = false)
    private String nombre;

    /**
     * Apellidos del usuario.
     */
    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    /**
     * Correo electrónico del usuario. Debe ser único en el sistema.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Contraseña del usuario (debe almacenarse cifrada).
     */
    @Column(name = "clave", nullable = false)
    private String clave;

    /**
     * Indica si el usuario está activo en el sistema.
     * Por defecto es true hasta que se desactive específicamente.
     */
    @Column(name = "activo")
    private Boolean activo = true;
    
    /**
     * Rol principal asignado al usuario que determina sus permisos base en el sistema.
     * Este valor no puede ser nulo.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    /**
     * Indica si el usuario tiene acceso a la aplicación móvil.
     * Por defecto es false hasta que se active específicamente.
     */
    @Column(name = "usuario_movil")
    private Boolean usuarioMovil = false;

    /**
     * Grupo al que pertenece el usuario, permite agrupar usuarios para asignaciones
     * o filtrados específicos.
     */
    @Column(name = "grupo")
    private String grupo;

    /**
     * Conjunto de módulos a los que tiene acceso este usuario.
     * Establece una relación muchos a muchos con la entidad Modulo.
     * Se carga ansiosamente (EAGER) para facilitar las comprobaciones de permisos.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_modulo",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "modulo_id")
    )
    private Set<Modulo> modulos = new HashSet<>();

    /**
     * Roles adicionales asignados a este usuario.
     * Relación muchos a muchos con la entidad Rol.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_rol",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();
    
    // Métodos de utilidad para implementar los casos de uso
    
    /**
     * Verifica si el usuario tiene acceso a un módulo específico.
     * Los administradores tienen acceso automático a todos los módulos.
     * Los usuarios inactivos no tienen acceso a ningún módulo.
     * 
     * @param nombreModulo El nombre del módulo a verificar
     * @return true si el usuario tiene acceso al módulo, false en caso contrario
     */
    public boolean tieneAccesoAModulo(String nombreModulo) {
        logger.debug("Verificando acceso del usuario ID {} al módulo: {}", id, nombreModulo);
        
        if (activo == null || !activo) {
            logger.debug("Usuario ID {} inactivo, acceso denegado", id);
            return false;
        }
        
        if (esAdministrador()) {
            logger.debug("Usuario ID {} es administrador, acceso concedido", id);
            return true;
        }



        // Verificar acceso a través del rol
        if (this.rol != null && this.rol.tieneAccesoAModulo(nombreModulo)) {
            logger.debug("Usuario ID {} tiene acceso al módulo {} a través de su rol", id, nombreModulo);
            return true;
        }

        logger.debug("Usuario ID {} no tiene acceso al módulo {}", id, nombreModulo);
        return false;
    }

    /**
     * Verifica si el usuario tiene el rol de ADMINISTRADOR.
     * 
     * @return true si el usuario es administrador, false en caso contrario
     */
    public boolean esAdministrador() {
        return this.rol != null && Rol.TipoRol.ADMINISTRADOR.equals(this.rol.getNombre());
    }

    /**
     * Verifica si el usuario tiene el rol de PROFESOR.
     * 
     * @return true si el usuario es profesor, false en caso contrario
     */
    public boolean esProfesor() {
        return this.rol != null && Rol.TipoRol.PROFESOR.equals(this.rol.getNombre());
    }

    /**
     * Verifica si el usuario tiene el rol de TECNICO.
     * 
     * @return true si el usuario es técnico, false en caso contrario
     */
    public boolean esTecnico() {
        return this.rol != null && Rol.TipoRol.TECNICO.equals(this.rol.getNombre());
    }

    /**
     * Verifica si el usuario tiene el rol de GESTOR_INCIDENCIAS.
     * 
     * @return true si el usuario es gestor de incidencias, false en caso contrario
     */
    public boolean esGestorIncidencias() {
        return this.rol != null && Rol.TipoRol.GESTOR_INCIDENCIAS.equals(this.rol.getNombre());
    }
    
    /**
     * Agrega un módulo a la lista de módulos a los que tiene acceso el usuario.
     * 
     * @param modulo El módulo a agregar
     */
    public void agregarModulo(Modulo modulo) {
        logger.info("Agregando módulo {} al usuario ID {}", modulo.getNombre(), id);
        this.modulos.add(modulo);
        logger.debug("Módulo {} agregado exitosamente al usuario ID {}", modulo.getNombre(), id);
    }
    
    /**
     * Elimina un módulo de la lista de módulos a los que tiene acceso el usuario.
     * 
     * @param modulo El módulo a eliminar
     */
    public void quitarModulo(Modulo modulo) {
        logger.info("Quitando módulo {} del usuario ID {}", modulo.getNombre(), id);
        boolean resultado = this.modulos.remove(modulo);
        logger.debug("Módulo {} {} del usuario ID {}", 
                modulo.getNombre(), resultado ? "quitado exitosamente" : "no encontrado", id);
    }
    
    /**
     * Desactiva al usuario, impidiendo su acceso al sistema.
     * Un usuario desactivado no podrá iniciar sesión ni acceder a ningún módulo.
     */
    public void desactivar() {
        logger.info("Desactivando usuario ID {}", id);
        this.activo = false;
        logger.debug("Usuario ID {} desactivado correctamente", id);
    }
    
    /**
     * Activa al usuario, permitiendo su acceso al sistema.
     * Solo los usuarios activos pueden iniciar sesión y acceder a módulos.
     */
    public void activar() {
        logger.info("Activando usuario ID {}", id);
        this.activo = true;
        logger.debug("Usuario ID {} activado exitosamente", id);
    }
    
    /**
     * Cambia la clave de acceso del usuario.
     * 
     * @param nuevaClave La nueva clave a establecer
     */
    public void cambiarClave(String nuevaClave) {
        logger.info("Cambiando clave del usuario ID {}", id);
        this.clave = nuevaClave;
        logger.debug("Clave del usuario ID {} cambiada exitosamente", id);
    }

    /**
     * Verifica si el usuario está activo en el sistema.
     * 
     * @return true si el usuario está activo, false en caso contrario
     */
    public boolean isActivo() {
        return activo;
    }
}

