package com.dam.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para verificar el estado de salud de la aplicación.
 * Proporciona endpoints para diagnóstico básico.
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    /**
     * Endpoint para verificar que la aplicación está funcionando correctamente.
     * 
     * @return Un objeto JSON con información sobre el estado de la aplicación
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "La aplicación está funcionando correctamente");
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }
}
