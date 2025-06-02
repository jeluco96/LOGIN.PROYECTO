package com.dam.login.service.impl;
import com.dam.login.model.Rol;
import com.dam.login.model.RolModulo;
import com.dam.login.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.dam.login.model.Modulo;
import com.dam.login.repository.RolModuloRepository;
import com.dam.login.service.RolService;


/**
 * Implementación del servicio de rol con la lógica de negocio relacionada con roles.
 */
@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private final RolModuloRepository rolModuloRepository;

    @Override
    @Transactional
    public Rol crearRol(Rol rol) {
        // Verificar si ya existe un rol con el mismo nombre
        if (rolRepository.findByNombre(rol.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe un rol con ese nombre");
        }

        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    public Rol modificarRol(Long id, Rol rolActualizado) {
        // Buscar el rol a modificar
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Verificar si el nombre nuevo ya existe en otro rol
        if (!rol.getNombre().equals(rolActualizado.getNombre()) && 
            rolRepository.findByNombre(rolActualizado.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe otro rol con ese nombre");
        }

        // Actualizar campos permitidos
        rol.setNombre(rolActualizado.getNombre());
        rol.setDescripcion(rolActualizado.getDescripcion());

        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    public void eliminarRol(Long id) {
        // Verificar si el rol existe
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Verificar si el rol está en uso por usuarios
        if (!rol.getUsuarios().isEmpty()) {
            throw new RuntimeException("No se puede eliminar un rol que está asignado a usuarios");
        }

        // Eliminar las relaciones con módulos
        rolModuloRepository.deleteByRol(rol);

        rolRepository.delete(rol);
    }

    @Override
    public Rol obtenerRolPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }

    @Override
    public Rol obtenerRolPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }

    @Override
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    @Override
    @Transactional
    public void asignarModulo(Long idRol, Modulo modulo) {
        // Buscar el rol
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Verificar si ya existe la relación
        if (rolModuloRepository.findByRolAndModulo(rol, modulo).isPresent()) {
            // Si ya existe, no hacer nada y devolver éxito
            return;
        }

        // Crear nueva relación
        RolModulo rolModulo = new RolModulo();
        rolModulo.setRol(rol);
        rolModulo.setModulo(modulo);

        rolModuloRepository.save(rolModulo);
    }

    @Override
    @Transactional
    public void quitarModulo(Long idRol, Modulo modulo) {
        // Buscar el rol
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Buscar la relación
        RolModulo rolModulo = (RolModulo) rolModuloRepository.findByRolAndModulo(rol, modulo)
                .orElseThrow(() -> new RuntimeException("El módulo no está asignado a este rol"));

        rolModuloRepository.delete(rolModulo);
    }
}
