package com.dam.login.repository;

import com.dam.login.model.Grupo;
import com.dam.login.model.TipoGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Grupo que proporciona operaciones CRUD básicas
 * y métodos de consulta específicos para gestionar grupos en el sistema.
 */
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    /**
     * Busca un grupo por su nombre exacto.
     * 
     * @param nombre El nombre del grupo a buscar
     * @return Un Optional que contiene el grupo si existe
     */
    Optional<Grupo> findByNombre(String nombre);

    /**
     * Busca grupos por el tipo al que pertenecen.
     * 
     * @param tipo El tipo de grupo a buscar
     * @return Lista de grupos del tipo especificado
     */
    List<Grupo> findByTipo(TipoGrupo tipo);

    /**
     * Busca grupos que contengan el texto especificado en su nombre.
     * 
     * @param texto El texto a buscar en el nombre del grupo
     * @return Lista de grupos cuyo nombre contiene el texto
     */
    List<Grupo> findByNombreContaining(String texto);

    /**
     * Verifica si existe un grupo con el nombre especificado.
     * 
     * @param nombre El nombre a verificar
     * @return true si existe un grupo con ese nombre, false en caso contrario
     */
    boolean existsByNombre(String nombre);

    /**
     * Busca grupos a los que pertenece un usuario específico.
     * 
     * @param usuarioId El ID del usuario
     * @return Lista de grupos a los que pertenece el usuario
     */
    @Query("SELECT g FROM Grupo g JOIN g.usuariosGrupo ug WHERE ug.usuario.id = :usuarioId")
    List<Grupo> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}