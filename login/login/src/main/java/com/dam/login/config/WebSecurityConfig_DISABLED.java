package com.dam.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * DESACTIVADO: Esta configuración está en conflicto con SecurityConfig.java
 * Esta clase ha sido completamente desactivada y está aquí solo como referencia.
 */
public class WebSecurityConfig_DISABLED {

    /**
     * Configura la cadena de filtros de seguridad.
     * NOTA: Esta configuración está desactivada. Ver SecurityConfig.java para la configuración activa.
     */
    // @Bean
    public SecurityFilterChain securityFilterChainLegacy(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Desactivar CSRF para simplificar durante el desarrollo
            .authorizeHttpRequests(authorize -> authorize
                // Permitir acceso a recursos estáticos y páginas públicas
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                .requestMatchers("/login", "/auth/login-process").permitAll()
                // Otras rutas requieren autenticación
                .anyRequest().permitAll()  // Temporalmente permitir todo para depuración
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/auth/login-process")
                .defaultSuccessUrl("/dashboard")
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // Para debugging: añade un usuario en memoria para pruebas
            .httpBasic(basic -> {});

        return http.build();
    }

    /**
     * Bean para el codificador de contraseñas.
     * NOTA: Desactivado para evitar conflicto con SecurityConfig
     */
    // @Bean
    public PasswordEncoder passwordEncoderLegacy() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración temporal de usuario en memoria para pruebas.
     * NOTA: Desactivado para evitar conflicto con SecurityConfig
     */
    // @Bean
    public InMemoryUserDetailsManager userDetailsServiceLegacy(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin@guzpasen.edu")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}
