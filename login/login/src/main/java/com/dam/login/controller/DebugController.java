package com.dam.login.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

/**
 * Controlador para funciones de depuración.
 * NO USAR EN PRODUCCIÓN.
 */
@Controller
public class DebugController {

    private static final Logger logger = Logger.getLogger(DebugController.class.getName());

    /**
     * Página de depuración principal
     */
    @GetMapping("/debug")
    public String debugPage(HttpSession session, Model model) {
        logger.warning("Accediendo a la página de depuración - NO USAR EN PRODUCCIÓN");
        return "debug";
    }

    /**
     * Redirección al dashboard sin login
     */
    @GetMapping("/")
    public String home() {
        logger.info("MODO DEBUG: Redireccionando directamente al dashboard");
        return "redirect:/dashboard";
    }
}
