package com.dam.login.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Filtro simple de seguridad para desarrollo.
 * SOLO PARA DESARROLLO - NO USAR EN PRODUCCIÓN.
 * 
 * Este filtro inyecta un usuario ficticio en cada sesión para permitir
 * la navegación sin necesidad de autenticación real durante el desarrollo.
 */
@Component
@Order(1) // Se ejecuta antes de otros filtros
public class SimpleDevelopmentSecurityFilter implements Filter {

    private static final Logger logger = Logger.getLogger(SimpleDevelopmentSecurityFilter.class.getName());
    private static final String USER_INFO_KEY = "userInfo";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(true);

        // Si no hay información de usuario en la sesión, establecer un usuario ficticio
        if (session.getAttribute(USER_INFO_KEY) == null) {
            // Crear un usuario ficticio para desarrollo
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", 1L);
            userInfo.put("nombre", "Administrador");
            userInfo.put("apellidos", "Sistema");
            userInfo.put("email", "admin@guzpasen.edu");
            userInfo.put("rol", "ADMINISTRADOR");

            // Guardar en la sesión
            session.setAttribute(USER_INFO_KEY, userInfo);
            logger.info("✓ Usuario de desarrollo creado automáticamente");
        }

        // Continuar con la cadena de filtros
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        logger.warning("⚠️ MODO DESARROLLO ACTIVADO - Seguridad desactivada y usuario ficticio creado automáticamente");
        logger.warning("⚠️ NO USAR ESTA CONFIGURACIÓN EN PRODUCCIÓN");
    }

    @Override
    public void destroy() {
        // No es necesario realizar ninguna acción
    }
}
