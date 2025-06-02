package com.dam.login.service;

import com.dam.login.model.Modulo;
import com.dam.login.model.Rol;
import com.dam.login.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que define las operaciones de negocio relacionadas con usuarios.
 */
public interface UsuarioService {

    /**
     * Autentica a un usuario con sus credenciales.
     * Implementa el caso de uso CU01: Login de Usuario.
     * 
     * @param email El correo electrónico del usuario
     * @param clave La contraseña del usuario
     * @return El usuario autenticado
     * @throws RuntimeException Si las credenciales son inválidas o si el usuario está desactivado
     */
    Usuario login(String email, String clave);

    /**
     * Crea un nuevo usuario en el sistema.
     * Implementa el caso de uso CU02: Alta de Usuario.
     * 
     * @param usuario Datos del usuario a crear
     * @return El usuario creado
     * @throws RuntimeException Si ya existe un usuario con el mismo email
     */
    Usuario crearUsuario(Usuario usuario);

    /**
     * Modifica los datos de un usuario existente.
     * Implementa el caso de uso CU03: Modificación de Usuario.
     * 
     * @param id Identificador del usuario a modificar
     * @param usuario Nuevos datos del usuario
     * @return El usuario modificado
     * @throws RuntimeException Si el usuario no existe o si hay datos inválidos
     */
    Usuario modificarUsuario(Long id, Usuario usuario);

    /**
     * Resetea la contraseña de un usuario.
     * Implementa el caso de uso CU04: Reseteo de Clave.
     * 
     * @param id Identificador del usuario
     * @param nuevaClave Nueva contraseña
     * @throws RuntimeException Si el usuario no existe
     */
    void resetearClave(Long id, String nuevaClave);

    /**
     * Desactiva un usuario impidiendo su acceso al sistema.
     * Implementa el caso de uso CU05: Desactivar Usuario.
     * 
     * @param id Identificador del usuario a desactivar
     * @throws RuntimeException Si el usuario no existe
     */
    void desactivarUsuario(Long id);

    /**
     * Activa un usuario permitiendo su acceso al sistema.
     * Implementa el caso de uso CU06: Activar Usuario.
     * 
     * @param id Identificador del usuario a activar
     * @throws RuntimeException Si el usuario no existe
     */
    void activarUsuario(Long id);

    /**
     * Asigna un grupo a un usuario.
     * Implementa el caso de uso CU07: Gestión de Grupos.
     * 
     * @param id Identificador del usuario
     * @param grupo Nombre del grupo a asignar
     * @throws RuntimeException Si el usuario no existe
     */
    void asignarGrupo(Long id, String grupo);

    /**
     * Asigna un módulo a un usuario.
     * Implementa el caso de uso CU08: Gestión de Permisos sobre Módulos.
     * 
     * @param idUsuario Identificador del usuario
     * @param idModulo Identificador del módulo a asignar
     * @throws RuntimeException Si el usuario o el módulo no existen
     */
    void asignarModulo(Long idUsuario, Long idModulo);

    /**
     * Quita un módulo asignado a un usuario.
     * Implementa el caso de uso CU08: Gestión de Permisos sobre Módulos.
     * 
     * @param idUsuario Identificador del usuario
     * @param idModulo Identificador del módulo a quitar
     * @throws RuntimeException Si el usuario o el módulo no existen
     */
    void quitarModulo(Long idUsuario, Long idModulo);

    /**
     * Obtiene los módulos a los que tiene acceso un usuario.
     * Implementa el caso de uso CU10: Listado de Acceso a Módulos.
     * 
     * @param id Identificador del usuario
     * @return Lista de módulos accesibles para el usuario
     * @throws RuntimeException Si el usuario no existe
     */
    List<Modulo> obtenerModulosUsuario(Long id);

    /**
     * Verifica si un usuario tiene permiso para acceder a un módulo específico.
     * Implementa el caso de uso CU11: Validar Usuario con Permiso a Módulo.
     * 
     * @param id Identificador del usuario
     * @param nombreModulo Nombre del módulo a verificar
     * @return true si tiene permiso, false en caso contrario
     * @throws RuntimeException Si el usuario no existe
     */
    boolean tienePermisoModulo(Long id, String nombreModulo);

    /**
     * Obtiene un listado de todos los usuarios del sistema.
     * 
     * @return Lista de todos los usuarios
     */
    List<Usuario> listarUsuarios();

    /**
     * Obtiene un listado de todos los usuarios activos del sistema.
     * 
     * @return Lista de usuarios activos
     */
    List<Usuario> listarUsuariosActivos();

    /**
     * Obtiene un listado de usuarios filtrados por rol.
     * 
     * @param rol Rol por el cual filtrar
     * @return Lista de usuarios con el rol especificado
     */
    List<Usuario> listarUsuariosPorRol(Rol rol);

    /**
     * Obtiene un listado de usuarios filtrados por grupo.
     * 
     * @param grupo Nombre del grupo por el cual filtrar
     * @return Lista de usuarios del grupo especificado
     */
    List<Usuario> listarUsuariosPorGrupo(String grupo);

    /**
     * Obtiene un usuario por su identificador.
     * 
     * @param id Identificador del usuario
     * @return El usuario encontrado
     * @throws RuntimeException Si el usuario no existe
     */
    Usuario obtenerUsuarioPorId(Long id);

    /**
     * Busca un usuario por su dirección de correo electrónico.
     * 
     * @param email Correo electrónico del usuario
     * @return Optional con el usuario si existe, o vacío si no existe
     */
    Optional<Usuario> buscarPorEmail(String email);

    /**
     * Busca un usuario por su dirección de correo electrónico.
     * A diferencia de buscarPorEmail, este método lanza una excepción si el usuario no existe.
     * 
     * @param email Correo electrónico del usuario
     * @return El usuario encontrado
     * @throws RuntimeException Si no se encuentra ningún usuario con el email proporcionado
     */
    Usuario findByEmail(String email);
}

