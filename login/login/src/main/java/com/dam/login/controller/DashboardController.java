package com.dam.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la página principal del dashboard
 */
@Controller
public class DashboardController {

    /**
     * Muestra la página principal del dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Redirigir directamente a la plantilla dashboard/home
        return "dashboard/home";
    }
}
