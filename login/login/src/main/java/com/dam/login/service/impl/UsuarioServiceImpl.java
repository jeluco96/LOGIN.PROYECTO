package com.dam.login.service.impl;

import com.dam.login.model.Modulo;
import com.dam.login.model.Usuario;
import com.dam.login.model.Rol;
import com.dam.login.repository.UsuarioRepository;
import com.dam.login.repository.ModuloRepository;
import com.dam.login.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de usuario con la lógica de negocio relacionada con usuarios.
 */
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ModuloRepository moduloRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario login(String email, String clave) {
        // Buscar el usuario por email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        // Verificar si el usuario está activo
        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario desactivado");
        }

        // Verificar la contraseña: compara directamente si es admin@guzpasen.edu y "password"
        // o si es otro usuario, verifica con passwordEncoder
        if ("admin@guzpasen.edu".equals(email) && "password".equals(clave)) {
            return usuario;
        } else if (passwordEncoder.matches(clave, usuario.getClave())) {
            return usuario;
        } else {
            throw new RuntimeException("Credenciales inválidas");
        }
    }

    @Override
    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        // Verificar si ya existe un usuario con el mismo email
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }

        // Codificar la contraseña antes de guardarla
        usuario.setClave(passwordEncoder.encode(usuario.getClave()));

        // Establecer como activo por defecto
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario modificarUsuario(Long id, Usuario usuarioActualizado) {
        // Buscar el usuario a modificar
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar campos permitidos
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setApellidos(usuarioActualizado.getApellidos());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setRol(usuarioActualizado.getRol());
        usuario.setGrupo(usuarioActualizado.getGrupo());
        usuario.setUsuarioMovil(usuarioActualizado.getUsuarioMovil());

        // No actualizar la contraseña aquí, hay un método específico para eso

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void resetearClave(Long id, String nuevaClave) {
        // Buscar el usuario
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si el usuario está activo
        if (!usuario.getActivo()) {
            throw new RuntimeException("No se puede resetear la clave de un usuario inactivo");
        }

        // Codificar y establecer la nueva contraseña
        usuario.setClave(passwordEncoder.encode(nuevaClave));

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void desactivarUsuario(Long id) {
        // Buscar el usuario
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Desactivar usuario
        usuario.desactivar();

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void activarUsuario(Long id) {
        // Buscar el usuario
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Activar usuario
        usuario.activar();

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void asignarGrupo(Long id, String grupo) {
        // Buscar el usuario
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Asignar grupo
        usuario.setGrupo(grupo);

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void asignarModulo(Long idUsuario, Long idModulo) {
        // Buscar el usuario y el módulo
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Modulo modulo = moduloRepository.findById(idModulo)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        // Asignar el módulo al usuario
        usuario.agregarModulo(modulo);

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void quitarModulo(Long idUsuario, Long idModulo) {
        // Buscar el usuario y el módulo
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Modulo modulo = moduloRepository.findById(idModulo)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        // Quitar el módulo del usuario
        usuario.quitarModulo(modulo);

        usuarioRepository.save(usuario);
    }

    @Override
    public List<Modulo> obtenerModulosUsuario(Long id) {
        // Buscar el usuario
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Devolver sus módulos
        return usuario.getModulos().stream().toList();
    }

    @Override
    public boolean tienePermisoModulo(Long id, String nombreModulo) {
        // Buscar el usuario
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si el usuario está activo
        if (!usuario.getActivo()) {
            return false;
        }

        // Administradores tienen acceso a todo
        if (usuario.esAdministrador()) {
            return true;
        }

        // Verificar si tiene acceso al módulo
        return usuario.tieneAccesoAModulo(nombreModulo);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public List<Usuario> listarUsuariosActivos() {
        return usuarioRepository.findByActivoTrue();
    }

    @Override
    public List<Usuario> listarUsuariosPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    @Override
    public List<Usuario> listarUsuariosPorGrupo(String grupo) {
        return usuarioRepository.findByGrupo(grupo);
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el email: " + email));
    }
}
