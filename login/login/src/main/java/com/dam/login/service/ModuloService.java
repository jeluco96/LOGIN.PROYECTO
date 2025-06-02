package com.dam.login.service;

import com.dam.login.model.Modulo;
import com.dam.login.model.Rol;

import java.util.List;

/**
 * Servicio que define las operaciones de negocio relacionadas con módulos.
 */
public interface ModuloService {

    /**
     * Crea un nuevo módulo en el sistema.
     * 
     * @param modulo Datos del módulo a crear
     * @return El módulo creado
     * @throws RuntimeException Si ya existe un módulo con el mismo nombre
     */
    Modulo crearModulo(Modulo modulo);

    /**
     * Modifica los datos de un módulo existente.
     * 
     * @param id Identificador del módulo a modificar
     * @param modulo Nuevos datos del módulo
     * @return El módulo modificado
     * @throws RuntimeException Si el módulo no existe o si hay datos inválidos
     */
    Modulo modificarModulo(Long id, Modulo modulo);

    /**
     * Elimina un módulo del sistema.
     * 
     * @param id Identificador del módulo a eliminar
     * @throws RuntimeException Si el módulo no existe o si está en uso
     */
    void eliminarModulo(Long id);

    /**
     * Obtiene un módulo por su identificador.
     * 
     * @param id Identificador del módulo
     * @return El módulo encontrado
     * @throws RuntimeException Si el módulo no existe
     */
    Modulo obtenerModuloPorId(Long id);

    /**
     * Obtiene un módulo por su nombre exacto.
     * 
     * @param nombre Nombre del módulo
     * @return El módulo encontrado
     * @throws RuntimeException Si el módulo no existe
     */
    Modulo obtenerModuloPorNombre(String nombre);

    /**
     * Obtiene un listado de todos los módulos del sistema.
     * 
     * @return Lista de todos los módulos
     */
    List<Modulo> listarModulos();

    /**
     * Obtiene un listado de módulos filtrados por rol.
     * 
     * @param rol Rol por el cual filtrar
     * @return Lista de módulos asociados al rol especificado
     */
    List<Modulo> listarModulosPorRol(Rol rol);

    /**
     * Obtiene un listado de módulos activos.
     * 
     * @return Lista de módulos activos
     */
    List<Modulo> listarModulosActivos();
}

