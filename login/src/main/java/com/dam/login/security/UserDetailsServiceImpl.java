package com.dam.login.security;

import com.dam.login.model.Usuario;
import com.dam.login.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Implementación de UserDetailsService para cargar los detalles del usuario en Spring Security.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Cargando detalles de usuario para email: {}", email);

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);

        if (usuarioOpt.isEmpty()) {
            log.warn("Usuario no encontrado: {}", email);
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getActivo()) {
            log.warn("Usuario desactivado: {}", email);
            throw new UsernameNotFoundException("Usuario desactivado: " + email);
        }

        log.debug("Usuario encontrado: {}, rol: {}", usuario.getNombre(), usuario.getRol());

        return new User(
            usuario.getEmail(),
            usuario.getClave(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()))
        );
    }
}
