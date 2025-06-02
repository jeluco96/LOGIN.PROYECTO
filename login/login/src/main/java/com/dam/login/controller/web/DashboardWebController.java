package com.dam.login.controller.web;

import com.dam.login.model.Usuario;
import com.dam.login.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class DashboardWebController {

    private final UsuarioService usuarioService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @PostMapping("/auth/login-process")
    public String processLogin(@RequestParam("email") String email, 
                              @RequestParam("clave") String clave,
                              HttpSession session, 
                              Model model) {
        try {
            System.out.println("WebController: Procesando login para " + email);

            // Usar el servicio existente para autenticar
            Usuario usuario = usuarioService.login(email, clave);

            System.out.println("WebController: Login exitoso, usuario ID: " + usuario.getId());

            // Si la autenticación fue exitosa, almacenar información del usuario en la sesión
            session.setAttribute("userInfo", usuario);
            return "redirect:/dashboard";

        } catch (Exception e) {
            System.out.println("WebController: Error en login: " + e.getMessage());
            // Redirigir con el mensaje de error específico para depuración
            return "redirect:/login?error=" + e.getMessage();
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Panel Principal");
        model.addAttribute("sectionName", "Panel Principal");
        return "dashboard/home";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("title", "Gestión de Usuarios");
        model.addAttribute("sectionName", "Gestión de Usuarios");
        // Aquí podrías añadir la lista de usuarios
        // model.addAttribute("usuarios", usuarioService.findAll());
        return "usuarios/lista";
    }

    @GetMapping("/modulos")
    public String modulos(Model model) {
        model.addAttribute("title", "Gestión de Módulos");
        model.addAttribute("sectionName", "Gestión de Módulos");
        return "modulos/lista";
    }

    @GetMapping("/roles")
    public String roles(Model model) {
        model.addAttribute("title", "Gestión de Roles");
        model.addAttribute("sectionName", "Gestión de Roles");
        return "roles/lista";
    }

    @GetMapping("/configuracion")
    public String configuracion(Model model) {
        model.addAttribute("title", "Configuración");
        model.addAttribute("sectionName", "Configuración del Sistema");
        return "configuracion/index";
    }
}
