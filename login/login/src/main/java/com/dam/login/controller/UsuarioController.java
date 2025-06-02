package com.dam.login.controller;

import com.dam.login.dto.UsuarioDTO;
import com.dam.login.model.Modulo;
import com.dam.login.model.Usuario;
import com.dam.login.model.Rol;
import com.dam.login.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST que expone endpoints para la gestión de usuarios.
 * Implementa los casos de uso relacionados con la autenticación, creación,
 * modificación y gestión de permisos de usuarios.
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    /**
     * Servicio que proporciona la lógica de negocio para la gestión de usuarios.
     */
    private final UsuarioService usuarioService;

    /**
     * Autentica a un usuario mediante sus credenciales.
     * Implementa el caso de uso CU01: Login de Usuario.
     * 
     * @param credentials Mapa con las credenciales (email y clave)
     * @return ResponseEntity con el usuario autenticado o error 401 si las credenciales son inválidas
     */
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Map<String, String> credentials) {
        try {
            Usuario usuario = usuarioService.login(credentials.get("email"), credentials.get("clave"));
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * Implementa el caso de uso CU02: Alta de Usuario.
     * 
     * @param usuario Datos del usuario a crear
     * @return ResponseEntity con el usuario creado o error 400 si hay datos inválidos
     */
    @PostMapping
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
            UsuarioDTO usuarioDTO = UsuarioDTO.fromEntity(nuevoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Modifica los datos de un usuario existente.
     * Implementa el caso de uso CU03: Modificación de Usuario.
     * 
     * @param id Identificador del usuario a modificar
     * @param usuario Nuevos datos del usuario
     * @return ResponseEntity con el usuario modificado o error 400 si hay datos inválidos
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> modificarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioModificado = usuarioService.modificarUsuario(id, usuario);
            UsuarioDTO usuarioDTO = UsuarioDTO.fromEntity(usuarioModificado);
            return ResponseEntity.ok(usuarioDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Resetea la contraseña de un usuario.
     * Implementa el caso de uso CU04: Reseteo de Clave.
     * 
     * @param id Identificador del usuario
     * @param resetData Mapa con la nueva clave
     * @return ResponseEntity vacío con éxito o error 400 si hay problemas
     */
    @PostMapping("/{id}/resetClave")
    public ResponseEntity<Void> resetearClave(@PathVariable Long id, @RequestBody Map<String, String> resetData) {
        try {
            usuarioService.resetearClave(id, resetData.get("nuevaClave"));
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Desactiva un usuario impidiendo su acceso al sistema.
     * Implementa el caso de uso CU05: Desactivar Usuario.
     * 
     * @param id Identificador del usuario a desactivar
     * @return ResponseEntity vacío con éxito o error 400 si hay problemas
     */
    @PostMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable Long id) {
        try {
            usuarioService.desactivarUsuario(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // CU06: Activar Usuario
    @PostMapping("/{id}/activar")
    public ResponseEntity<Void> activarUsuario(@PathVariable Long id) {
        try {
            usuarioService.activarUsuario(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // CU07: Gestión de Grupos
    @PostMapping("/{id}/grupo")
    public ResponseEntity<Void> asignarGrupo(@PathVariable Long id, @RequestBody Map<String, String> grupoData) {
        try {
            usuarioService.asignarGrupo(id, grupoData.get("grupo"));
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // CU08: Gestión de Permisos sobre Módulos
    @PostMapping("/{idUsuario}/modulos/{idModulo}")
    public ResponseEntity<Void> asignarModulo(@PathVariable Long idUsuario, @PathVariable Long idModulo) {
        try {
            usuarioService.asignarModulo(idUsuario, idModulo);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{idUsuario}/modulos/{idModulo}")
    public ResponseEntity<Void> quitarModulo(@PathVariable Long idUsuario, @PathVariable Long idModulo) {
        try {
            usuarioService.quitarModulo(idUsuario, idModulo);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // CU10: Listado de Acceso a Módulos
    @GetMapping("/{id}/modulos")
    public ResponseEntity<List<Modulo>> obtenerModulosUsuario(@PathVariable Long id) {
        try {
            List<Modulo> modulos = usuarioService.obtenerModulosUsuario(id);
            return ResponseEntity.ok(modulos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // CU11: Validar Usuario con Permiso a Módulo
    @GetMapping("/{id}/permiso/{nombreModulo}")
    public ResponseEntity<Map<String, Boolean>> validarPermisoModulo(
            @PathVariable Long id, 
            @PathVariable String nombreModulo) {
        try {
            boolean tienePermiso = usuarioService.tienePermisoModulo(id, nombreModulo);
            return ResponseEntity.ok(Map.of("tienePermiso", tienePermiso));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Endpoints adicionales
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        List<UsuarioDTO> usuarioDTOs = usuarios.stream()
                .map(UsuarioDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(usuarioDTOs);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioDTO>> listarUsuariosActivos() {
        List<Usuario> usuarios = usuarioService.listarUsuariosActivos();
        List<UsuarioDTO> usuarioDTOs = usuarios.stream()
                .map(UsuarioDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(usuarioDTOs);
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<UsuarioDTO>> listarUsuariosPorRol(@PathVariable Rol rol) {
        List<Usuario> usuarios = usuarioService.listarUsuariosPorRol(rol);
        List<UsuarioDTO> usuarioDTOs = usuarios.stream()
                .map(UsuarioDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(usuarioDTOs);
    }

    @GetMapping("/grupo/{grupo}")
    public ResponseEntity<List<UsuarioDTO>> listarUsuariosPorGrupo(@PathVariable String grupo) {
        List<Usuario> usuarios = usuarioService.listarUsuariosPorGrupo(grupo);
        List<UsuarioDTO> usuarioDTOs = usuarios.stream()
                .map(UsuarioDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(usuarioDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
            UsuarioDTO usuarioDTO = UsuarioDTO.fromEntity(usuario);
            return ResponseEntity.ok(usuarioDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}