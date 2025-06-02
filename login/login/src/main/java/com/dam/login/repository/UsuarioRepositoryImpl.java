package com.dam.login.repository;

import com.dam.login.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Implementación de las operaciones personalizadas definidas en UsuarioRepositoryCustom.
 * Utiliza EntityManager para ejecutar consultas JPQL personalizadas.
 */
@Repository
public class UsuarioRepositoryImpl implements UsuarioRepositoryCustom {

    /**
     * EntityManager para ejecutar consultas personalizadas contra la base de datos.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     * Implementa la búsqueda de usuario por nombre y apellidos mediante JPQL.
     */
    @Override
    public Optional<Usuario> buscarPorNombreYApellido(String nombre, String apellidos) {
        String jpql = "SELECT u FROM Usuario u WHERE u.nombre = :nombre AND u.apellidos = :apellidos";
        return entityManager.createQuery(jpql, Usuario.class)
                .setParameter("nombre", nombre)
                .setParameter("apellidos", apellidos)
                .getResultList()
                .stream()
                .findFirst();
    }
}