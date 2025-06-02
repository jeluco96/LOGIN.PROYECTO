package com.dam.login.config;

import com.dam.login.security.JwtAuthenticationFilter;
import com.dam.login.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuración de seguridad para la aplicación.
 * Define componentes básicos de seguridad y reglas de acceso.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Define un PasswordEncoder basado en BCrypt para usar en la aplicación.
     * BCrypt es un algoritmo de hashing recomendado para contraseñas.
     *
     * @return Instancia de BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Filtro de autenticación JWT que procesa los tokens en las peticiones
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    /**
     * Usuario de prueba en memoria para desarrollo
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin@guzpasen.edu")
                .password(passwordEncoder().encode("password"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    /**
     * Configuración de seguridad para API REST
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**") // Aplicar solo a rutas API
            // Desactivar CSRF para API REST
            .csrf(AbstractHttpConfigurer::disable)
            // Configuración sin estado para API REST
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Reglas de autorización
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/login").permitAll()
                .anyRequest().authenticated() // Requiere autenticación para el resto de rutas API
            )
            // Añadir filtro JWT
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración de seguridad para aplicación web - DESACTIVADA PARA DESARROLLO
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // No aplicar a rutas API, que ya están manejadas por apiSecurityFilterChain
            .securityMatcher("/**")
            // Desactivar CSRF para simplificar en desarrollo
            .csrf(AbstractHttpConfigurer::disable)
            // Permitir todas las solicitudes
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