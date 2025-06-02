package com.dam.login.repository;

import com.dam.login.model.Usuario;
import java.util.Optional;

/**
 * Interfaz que define operaciones personalizadas para el repositorio de usuarios.
 * Extiende las capacidades básicas de JpaRepository con consultas específicas.
 */
public interface UsuarioRepositoryCustom {
    /**
     * Busca un usuario por su nombre y apellidos exactos.
     * 
     * @param nombre El nombre del usuario a buscar
     * @param apellidos Los apellidos del usuario a buscar
     * @return Un Optional que contiene el usuario si se encuentra
     */
    Optional<Usuario> buscarPorNombreYApellido(String nombre, String apellidos);
}