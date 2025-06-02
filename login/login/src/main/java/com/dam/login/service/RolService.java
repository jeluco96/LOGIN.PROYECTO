package com.dam.login.service;


import com.dam.login.model.Modulo;
import com.dam.login.model.Rol;

import java.util.List;

/**
 * Servicio que define las operaciones de negocio relacionadas con roles.
 */
public interface RolService {

    /**
     * Crea un nuevo rol en el sistema.
     * 
     * @param rol Datos del rol a crear
     * @return El rol creado
     * @throws RuntimeException Si ya existe un rol con el mismo nombre
     */
    Rol crearRol(Rol rol);

    /**
     * Modifica los datos de un rol existente.
     * 
     * @param id Identificador del rol a modificar
     * @param rol Nuevos datos del rol
     * @return El rol modificado
     * @throws RuntimeException Si el rol no existe o si hay datos inválidos
     */
    Rol modificarRol(Long id, Rol rol);

    /**
     * Elimina un rol del sistema.
     * 
     * @param id Identificador del rol a eliminar
     * @throws RuntimeException Si el rol no existe o si está en uso
     */
    void eliminarRol(Long id);

    /**
     * Obtiene un rol por su identificador.
     * 
     * @param id Identificador del rol
     * @return El rol encontrado
     * @throws RuntimeException Si el rol no existe
     */
    Rol obtenerRolPorId(Long id);

    /**
     * Obtiene un rol por su nombre exacto.
     * 
     * @param nombre Nombre del rol
     * @return El rol encontrado
     * @throws RuntimeException Si el rol no existe
     */
    Rol obtenerRolPorNombre(String nombre);

    /**
     * Obtiene un listado de todos los roles del sistema.
     * 
     * @return Lista de todos los roles
     */
    List<Rol> listarRoles();

    /**
     * Asigna un módulo a un rol.
     * 
     * @param idRol Identificador del rol
     * @param modulo Módulo a asignar
     * @throws RuntimeException Si el rol no existe
     */
    void asignarModulo(Long idRol, Modulo modulo);

    /**
     * Quita un módulo de un rol.
     * 
     * @param idRol Identificador del rol
     * @param modulo Módulo a quitar
     * @throws RuntimeException Si el rol no existe o si no tiene el módulo asignado
     */
    void quitarModulo(Long idRol, Modulo modulo);
}