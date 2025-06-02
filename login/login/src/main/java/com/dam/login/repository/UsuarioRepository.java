package com.dam.login.repository;

import com.dam.login.model.Rol;
import com.dam.login.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceder a los datos de usuarios en la base de datos.
 * Proporciona operaciones CRUD básicas y métodos de consulta específicos.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su dirección de correo electrónico.
     * 
     * @param email Correo electrónico del usuario
     * @return Optional con el usuario si existe, o vacío si no existe
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el correo electrónico dado.
     * 
     * @param email Correo electrónico a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Busca todos los usuarios activos.
     * 
     * @return Lista de usuarios activos
     */
    List<Usuario> findByActivoTrue();

    /**
     * Busca todos los usuarios con un rol específico.
     * 
     * @param rol Rol a buscar
     * @return Lista de usuarios con el rol especificado
     */
    List<Usuario> findByRol(Rol rol);

    /**
     * Busca todos los usuarios que pertenecen a un grupo específico.
     * 
     * @param grupo Nombre del grupo a buscar
     * @return Lista de usuarios del grupo especificado
     */
    List<Usuario> findByGrupo(String grupo);

    /**
     * Busca usuarios que tienen acceso a un módulo específico por su nombre.
     * Utiliza una consulta JPQL para encontrar usuarios relacionados con el módulo.
     * 
     * @param nombreModulo El nombre del módulo
     * @return Lista de usuarios que tienen acceso al módulo especificado
     */
    @Query("SELECT u FROM Usuario u JOIN u.modulos m WHERE m.nombre = :nombreModulo")
    List<Usuario> findByModuloNombre(@Param("nombreModulo") String nombreModulo);
}