package com.dam.login.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * ¡¡¡SOLO PARA DESARROLLO!!!
 * Filtro que simula un usuario logueado automáticamente en cada sesión.
 * Esto permite navegar por toda la aplicación sin necesidad de iniciar sesión.
 */
@Component
public class AutoLoginFilter implements Filter {

    private static final Logger logger = Logger.getLogger(AutoLoginFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(true);

        // Si no hay información de usuario en la sesión, establecer un usuario de prueba
        if (session.getAttribute("userInfo") == null) {
            logger.info("MODO DEBUG: Creando sesión automática para usuario de prueba");

            // Crear información básica para la sesión simulando un usuario administrador
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", 1L);
            userInfo.put("nombre", "Administrador");
            userInfo.put("apellidos", "Sistema");
            userInfo.put("email", "admin@guzpasen.edu");
            userInfo.put("rol", "ADMINISTRADOR");

            // Guardar en la sesión
            session.setAttribute("userInfo", userInfo);

            logger.warning("MODO DEBUG: Usuario automático establecido. Esta funcionalidad NO debe usarse en producción.");
        }

        // Continuar con la cadena de filtros
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        logger.warning("¡¡¡MODO DEBUG ACTIVADO!!! Inicio de sesión automático habilitado. NO USAR EN PRODUCCIÓN.");
    }

    @Override
    public void destroy() {
        // No es necesario hacer nada aquí
    }
}
