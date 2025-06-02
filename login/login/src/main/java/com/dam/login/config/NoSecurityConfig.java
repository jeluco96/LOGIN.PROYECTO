package com.dam.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ¡¡¡SOLO PARA DESARROLLO!!!
 * Configuración que desactiva completamente la seguridad.
 * Esta configuración tiene prioridad sobre cualquier otra configuración de seguridad.
 */
@Configuration
@EnableWebSecurity
public class NoSecurityConfig {

    /**
     * Configuración principal que desactiva todas las restricciones de seguridad.
     */
    @Bean
    @Primary // Esta configuración tiene prioridad sobre cualquier otra
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactivar CSRF
            .csrf(AbstractHttpConfigurer::disable)
            // Desactivar todas las restricciones de acceso
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll()
            )
            // Desactivar login form
            .formLogin(AbstractHttpConfigurer::disable)
            // Desactivar logout
            .logout(AbstractHttpConfigurer::disable)
            // Desactivar httpBasic
            .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
