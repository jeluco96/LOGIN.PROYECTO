package com.dam.login.repository;
import com.dam.login.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestionar la persistencia de la entidad Rol.
 * Proporciona métodos para crear, leer, actualizar y eliminar roles en la base de datos.
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    /**
     * Busca un rol por su nombre exacto.
     * 
     * @param nombre El nombre del rol a buscar
     * @return Optional con el rol si existe, empty si no se encuentra
     */
    Optional<Rol> findByNombre(String nombre);

    /**
     * Verifica si existe un rol con el nombre especificado.
     * 
     * @param nombre El nombre del rol a verificar
     * @return true si el rol existe, false en caso contrario
     */
    boolean existsByNombre(String nombre);
}
