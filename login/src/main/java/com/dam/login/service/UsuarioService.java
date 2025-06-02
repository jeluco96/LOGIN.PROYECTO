import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de usuarios.
 */
public interface UsuarioService {

    /**
     * Busca un usuario por su dirección de correo electrónico.
     *
     * @param email Dirección de correo electrónico
     * @return Usuario encontrado
     */
    Optional<Usuario> buscarPorEmail(String email);

    /**
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario
     * @return Usuario encontrado
     */
    Optional<Usuario> findById(Long id);

    /**
     * Realiza la autenticación de un usuario.
     *
     * @param email Dirección de correo electrónico
     * @param clave Contraseña
     * @return Usuario autenticado
     * @throws RuntimeException Si las credenciales son inválidas o el usuario está inactivo
     */
    Usuario login(String email, String clave);

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param usuario Datos del usuario a registrar
     * @return Usuario registrado
     */
    Usuario registrar(Usuario usuario);
