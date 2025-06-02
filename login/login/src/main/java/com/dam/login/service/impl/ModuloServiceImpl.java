package com.dam.login.service.impl;

import com.dam.login.model.Modulo;
import com.dam.login.model.Rol;
import com.dam.login.repository.ModuloRepository;
import com.dam.login.service.ModuloService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de módulo con la lógica de negocio relacionada con módulos.
 */
@Service
@RequiredArgsConstructor
public class ModuloServiceImpl implements ModuloService {

    private final ModuloRepository moduloRepository;

    @Override
    @Transactional
    public Modulo crearModulo(Modulo modulo) {
        // Verificar si ya existe un módulo con el mismo nombre
        if (moduloRepository.findByNombre(modulo.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe un módulo con ese nombre");
        }

        // Establecer activo por defecto si es null
        if (modulo.getActivo() == null) {
            modulo.setActivo(true);
        }

        return moduloRepository.save(modulo);
    }

    @Override
    @Transactional
    public Modulo modificarModulo(Long id, Modulo moduloActualizado) {
        // Buscar el módulo a modificar
        Modulo modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        // Verificar si el nombre nuevo ya existe en otro módulo
        if (!modulo.getNombre().equals(moduloActualizado.getNombre()) && 
            moduloRepository.findByNombre(moduloActualizado.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe otro módulo con ese nombre");
        }

        // Actualizar campos permitidos
        modulo.setNombre(moduloActualizado.getNombre());
        modulo.setDescripcion(moduloActualizado.getDescripcion());


        return moduloRepository.save(modulo);
    }

    @Override
    @Transactional
    public void eliminarModulo(Long id) {
        // Verificar si el módulo existe
        Modulo modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        // Verificar si el módulo está en uso por usuarios
        if (!modulo.getUsuarios().isEmpty()) {
            throw new RuntimeException("No se puede eliminar un módulo que está en uso por usuarios");
        }

        moduloRepository.delete(modulo);
    }

    @Override
    public Modulo obtenerModuloPorId(Long id) {
        return moduloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));
    }

    @Override
    public Modulo obtenerModuloPorNombre(String nombre) {
        return moduloRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));
    }

    @Override
    public List<Modulo> listarModulos() {
        return moduloRepository.findAll();
    }

    @Override
    public List<Modulo> listarModulosPorRol(Rol rol) {
        if (rol == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }
        return moduloRepository.findByRol(rol);
    }

    @Override
    public List<Modulo> listarModulosActivos() {
        return moduloRepository.findByActivoTrue();
    }
}
