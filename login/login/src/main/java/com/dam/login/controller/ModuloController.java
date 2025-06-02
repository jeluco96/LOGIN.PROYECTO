package com.dam.login.controller;

import com.dam.login.dto.ModuloDTO;
import com.dam.login.model.Modulo;
import com.dam.login.model.Rol;
import com.dam.login.service.ModuloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone endpoints para la gestión de módulos del sistema.
 * Implementa los casos de uso relacionados con la creación, modificación y consulta de módulos,
 * así como la gestión de permisos de acceso basados en roles.
 */
@RestController
@RequestMapping("/api/modulos")
@RequiredArgsConstructor
public class ModuloController {

    /**
     * Servicio que proporciona la lógica de negocio para la gestión de módulos.
     */
    private final ModuloService moduloService;

    /**
     * Crea un nuevo módulo en el sistema.
     * Implementa el caso de uso CU09: Alta de Módulos Asociados a Roles.
     * 
     * @param modulo Datos del módulo a crear
     * @return ResponseEntity con el módulo creado o error 400 si hay datos inválidos
     */
    @PostMapping
    public ResponseEntity<Modulo> crearModulo(@RequestBody Modulo modulo) {
        try {
            Modulo nuevoModulo = moduloService.crearModulo(modulo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoModulo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Modifica los datos de un módulo existente.
     * 
     * @param id Identificador del módulo a modificar
     * @param modulo Nuevos datos del módulo
     * @return ResponseEntity con el módulo modificado o error si hay problemas
     */
    @PutMapping("/{id}")
    public ResponseEntity<Modulo> modificarModulo(@PathVariable Long id, @RequestBody Modulo modulo) {
        try {
            Modulo moduloModificado = moduloService.modificarModulo(id, modulo);
            return ResponseEntity.ok(moduloModificado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Elimina un módulo del sistema.
     * 
     * @param id Identificador del módulo a eliminar
     * @return ResponseEntity vacío con éxito o error si hay problemas
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarModulo(@PathVariable Long id) {
        try {
            moduloService.eliminarModulo(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Obtiene los datos de un módulo específico.
     * 
     * @param id Identificador del módulo a consultar
     * @return ResponseEntity con el módulo o error 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<ModuloDTO> obtenerModulo(@PathVariable Long id) {
        try {
            Modulo modulo = moduloService.obtenerModuloPorId(id);
            ModuloDTO moduloDTO = ModuloDTO.fromEntity(modulo);
            return ResponseEntity.ok(moduloDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Obtiene un listado de todos los módulos del sistema.
     * 
     * @return Lista de todos los módulos disponibles
     */
    @GetMapping
    public ResponseEntity<List<ModuloDTO>> listarModulos() {
        List<Modulo> modulos = moduloService.listarModulos();
        List<ModuloDTO> moduloDTOs = modulos.stream()
                .map(ModuloDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(moduloDTOs);
    }

    /**
     * Obtiene un listado de módulos filtrados por rol.
     * 
     * @param rol El rol por el cual filtrar los módulos
     * @return Lista de módulos asociados al rol especificado
     */
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Modulo>> listarModulosPorRol(@PathVariable Rol rol) {
        return ResponseEntity.ok(moduloService.listarModulosPorRol(rol));
    }

    /**
     * Busca un módulo por su nombre exacto.
     * 
     * @param nombre El nombre del módulo a buscar
     * @return ResponseEntity con el módulo o error 404 si no existe
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ModuloDTO> obtenerModuloPorNombre(@PathVariable String nombre) {
        try {
            Modulo modulo = moduloService.obtenerModuloPorNombre(nombre);
            ModuloDTO moduloDTO = ModuloDTO.fromEntity(modulo);
            return ResponseEntity.ok(moduloDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
