package com.dam.login.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de Spring MVC para manejar vistas y redirecciones simples.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Configura controladores de vista para rutas que no requieren lógica adicional
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Manejar redirecciones simples
        registry.addViewController("/").setViewName("redirect:/dashboard");

        // Redirecciones a páginas básicas (sin necesidad de controladores específicos)
        registry.addViewController("/usuarios").setViewName("dashboard/usuarios");
        registry.addViewController("/roles").setViewName("dashboard/roles");
        registry.addViewController("/modulos").setViewName("dashboard/modulos");
        registry.addViewController("/configuracion").setViewName("dashboard/configuracion");
    }
}
