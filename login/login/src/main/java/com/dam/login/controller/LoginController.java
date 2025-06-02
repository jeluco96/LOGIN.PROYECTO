package com.dam.login.controller;

import com.dam.login.dto.AuthResponse;
import com.dam.login.dto.LoginRequest;
import com.dam.login.model.Usuario;
import com.dam.login.security.JwtTokenProvider;
import com.dam.login.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Controlador para gestionar la autenticación de usuarios.
 * Maneja tanto solicitudes web como API.
 */
@Controller
@RequiredArgsConstructor
public class LoginController {

    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    private final UsuarioService usuarioService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Muestra la página de login para el acceso web
     */
    @GetMapping("/login")
    public String loginPage(Model model, 
                          @RequestParam(value = "error", required = false) String error,
                          @RequestParam(value = "logout", required = false) String logout) {

        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");
        }

        if (logout != null) {
            model.addAttribute("message", "Se ha cerrado la sesión correctamente");
        }

        return "auth/login";
    }

    /**
     * Procesa el formulario de login web
     */
    @PostMapping("/auth/login-process")
    public String processLogin(@RequestParam String email, 
                             @RequestParam String clave,
                             HttpSession session,
                             Model model) {
        try {
            // Intentar autenticar al usuario
            Usuario usuario = usuarioService.login(email, clave);

            // Crear información básica para la sesión
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", usuario.getId());
            userInfo.put("nombre", usuario.getNombre());
            userInfo.put("apellidos", usuario.getApellidos());
            userInfo.put("email", usuario.getEmail());
            userInfo.put("rol", usuario.getRol() != null ? usuario.getRol().getNombre() : "DESCONOCIDO");

            // Guardar en la sesión
            session.setAttribute("userInfo", userInfo);

            logger.info("Login web exitoso para: " + email);
            return "redirect:/dashboard";

        } catch (Exception e) {
            logger.warning("Error en login web: " + e.getMessage());
            model.addAttribute("error", "Error de autenticación: " + e.getMessage());
            return "auth/login";
        }
    }

    /**
     * Cierra la sesión web
     */
    @GetMapping("/auth/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    // Los endpoints API se han movido al AuthController
}
