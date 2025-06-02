package com.dam.login.controller;

import com.dam.login.dto.RolDTO;
import com.dam.login.model.Modulo;
import com.dam.login.model.Rol;
import com.dam.login.service.ModuloService;
import com.dam.login.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone endpoints para la gestión de roles en el sistema.
 * Implementa los casos de uso relacionados con la creación, modificación y consulta de roles,
 * así como la asignación de módulos a roles.
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    /**
     * Servicio que proporciona la lógica de negocio para la gestión de roles.
     */
    private final RolService rolService;

    /**
     * Servicio que proporciona la lógica de negocio para la gestión de módulos.
     */
    private final ModuloService moduloService;

    /**
     * Crea un nuevo rol en el sistema.
     * 
     * @param rol Datos del rol a crear
     * @return ResponseEntity con el rol creado o error 400 si hay datos inválidos
     */
    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol) {
        try {
            Rol nuevoRol = rolService.crearRol(rol);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRol);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Modifica los datos de un rol existente.
     * 
     * @param id Identificador del rol a modificar
     * @param rol Nuevos datos del rol
     * @return ResponseEntity con el rol modificado o error si hay problemas
     */
    @PutMapping("/{id}")
    public ResponseEntity<Rol> modificarRol(@PathVariable Long id, @RequestBody Rol rol) {
        try {
            Rol rolModificado = rolService.modificarRol(id, rol);
            return ResponseEntity.ok(rolModificado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Elimina un rol del sistema.
     * 
     * @param id Identificador del rol a eliminar
     * @return ResponseEntity vacío con éxito o error si hay problemas
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Long id) {
        try {
            rolService.eliminarRol(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Obtiene los datos de un rol específico.
     * 
     * @param id Identificador del rol a consultar
     * @return ResponseEntity con el rol o error 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> obtenerRol(@PathVariable Long id) {
        try {
            Rol rol = rolService.obtenerRolPorId(id);
            RolDTO rolDTO = RolDTO.fromEntity(rol);
            return ResponseEntity.ok(rolDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Obtiene un listado de todos los roles del sistema.
     * 
     * @return Lista de todos los roles disponibles
     */
    @GetMapping
    public ResponseEntity<List<RolDTO>> listarRoles() {
        List<Rol> roles = rolService.listarRoles();
        List<RolDTO> rolDTOs = roles.stream()
                .map(RolDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(rolDTOs);
    }

    /**
     * Asigna un módulo a un rol específico.
     * 
     * @param idRol Identificador del rol
     * @param idModulo Identificador del módulo a asignar
     * @return ResponseEntity vacío con éxito o error si hay problemas
     */
    @PostMapping("/{idRol}/modulos/{idModulo}")
    public ResponseEntity<Void> asignarModulo(@PathVariable Long idRol, @PathVariable Long idModulo) {
        try {
            Modulo modulo = moduloService.obtenerModuloPorId(idModulo);
            rolService.asignarModulo(idRol, modulo);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Quita un módulo de un rol específico.
     * 
     * @param idRol Identificador del rol
     * @param idModulo Identificador del módulo a quitar
     * @return ResponseEntity vacío con éxito o error si hay problemas
     */
    @DeleteMapping("/{idRol}/modulos/{idModulo}")
    public ResponseEntity<Void> quitarModulo(@PathVariable Long idRol, @PathVariable Long idModulo) {
        try {
            Modulo modulo = moduloService.obtenerModuloPorId(idModulo);
            rolService.quitarModulo(idRol, modulo);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Busca un rol por su nombre exacto.
     * 
     * @param nombre El nombre del rol a buscar
     * @return ResponseEntity con el rol o error 404 si no existe
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<RolDTO> obtenerRolPorNombre(@PathVariable String nombre) {
        try {
            Rol rol = rolService.obtenerRolPorNombre(nombre);
            RolDTO rolDTO = RolDTO.fromEntity(rol);
            return ResponseEntity.ok(rolDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
