package com.dam.login.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para verificar el estado de la aplicación.
 * Útil para diagnóstico y monitoreo.
 */
@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
@Slf4j
public class HealthController {

    /**
     * Endpoint para verificar el estado general de la aplicación.
     *
     * @return Estado de la aplicación
     */
    @GetMapping
    public ResponseEntity<?> health() {
        log.debug("Verificando estado de la aplicación");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        response.put("version", "1.0.0");

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para verificar el estado de la API REST.
     *
     * @return Estado específico de la API
     */
    @GetMapping("/api")
    public ResponseEntity<?> apiStatus() {
        log.debug("Verificando estado de la API");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("api", "GUZPASEN API REST");
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }
}
