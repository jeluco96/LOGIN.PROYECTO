package com.dam.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para las opciones de configuración del sistema
 */
@Controller
@RequestMapping("/configuracion")
public class ConfiguracionController {

    /**
     * Muestra la página principal de configuración
     */
    @GetMapping
    public String configuracion(Model model) {
        // Redirigir directamente a la plantilla configuracion/index
        return "configuracion/index";
    }
}
