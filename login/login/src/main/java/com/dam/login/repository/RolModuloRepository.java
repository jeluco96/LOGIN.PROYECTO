
package com.dam.login.repository;

import com.dam.login.model.Modulo;
import com.dam.login.model.Rol;
import com.dam.login.model.RolModulo;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dam.login.model.RolModulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad RolModulo que proporciona operaciones CRUD básicas
 * y métodos de consulta específicos para gestionar la relación entre roles y módulos.
 */
public interface RolModuloRepository extends JpaRepository<RolModulo, Long> {

    /**
     * Busca las relaciones entre roles y módulos para un rol específico.
     * 
     * @param rolId El ID del rol
     * @return Lista de relaciones RolModulo asociadas al rol
     */
    List<RolModulo> findByRolId(Long rolId);

    /**
     * Busca las relaciones entre roles y módulos para un módulo específico.
     * 
     * @param moduloId El ID del módulo
     * @return Lista de relaciones RolModulo asociadas al módulo
     */
    List<RolModulo> findByModuloId(Long moduloId);

    /**
     * Verifica si existe una relación entre un rol y un módulo específicos.
     * 
     * @param rolId El ID del rol
     * @param moduloId El ID del módulo
     * @return true si existe la relación, false en caso contrario
     */
    boolean existsByRolIdAndModuloId(Long rolId, Long moduloId);

    /**
     * Elimina la relación entre un rol y un módulo específicos.
     * 
     * @param rolId El ID del rol
     * @param moduloId El ID del módulo
     */
    void deleteByRolIdAndModuloId(Long rolId, Long moduloId);

    /**
     * Obtiene los IDs de los módulos asociados a un rol específico.
     * 
     * @param rolId El ID del rol
     * @return Lista de IDs de módulos asociados al rol
     */
    @Query("SELECT rm.modulo.id FROM RolModulo rm WHERE rm.rol.id = :rolId")
    List<Long> findModuloIdsByRolId(@Param("rolId") Long rolId);

    void deleteByRol(Rol rol);

    Optional<Object> findByRolAndModulo(Rol rol, Modulo modulo);
}