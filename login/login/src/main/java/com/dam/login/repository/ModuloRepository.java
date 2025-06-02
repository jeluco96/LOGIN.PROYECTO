package com.dam.login.repository;

import com.dam.login.model.Modulo;
import com.dam.login.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Modulo que proporciona operaciones CRUD básicas
 * y métodos de consulta específicos para gestionar módulos en el sistema.
 */
public interface ModuloRepository extends JpaRepository<Modulo, Long> {

    /**
     * Busca un módulo por su nombre exacto.
     *
     * @param nombre El nombre del módulo a buscar
     * @return Un Optional que contiene el módulo si existe
     */
    Optional<Modulo> findByNombre(String nombre);

    /**
     * Busca módulos que contengan el texto especificado en su nombre.
     *
     * @param texto El texto a buscar en el nombre del módulo
     * @return Lista de módulos cuyo nombre contiene el texto
     */
    List<Modulo> findByNombreContaining(String texto);

    /**
     * Verifica si existe un módulo con el nombre especificado.
     *
     * @param nombre El nombre a verificar
     * @return true si existe un módulo con ese nombre, false en caso contrario
     */
    boolean existsByNombre(String nombre);

    /**
     * Busca módulos asociados a un rol específico.
     *
     * @param rolId El ID del rol
     * @return Lista de módulos asociados al rol
     */
    @Query("SELECT m FROM Modulo m JOIN RolModulo rm ON m.id = rm.modulo.id WHERE rm.rol.id = :rolId")
    List<Modulo> findByRolId(@Param("rolId") Long rolId);

    /**
     * Busca módulos asociados a un rol específico.
     *
     * @param rol El rol para el cual buscar módulos
     * @return Lista de módulos asociados al rol
     */
    @Query("SELECT m FROM Modulo m JOIN RolModulo rm ON m.id = rm.modulo.id WHERE rm.rol = :rol")
    List<Modulo> findByRol(@Param("rol") Rol rol);

    List<Modulo> findByActivoTrue();
}