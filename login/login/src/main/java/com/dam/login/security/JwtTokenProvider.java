package com.dam.login.security;

import com.dam.login.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Componente encargado de generar y validar tokens JWT.
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = Logger.getLogger(JwtTokenProvider.class.getName());

    @Value("${app.jwt.secret:secreto-por-defecto-que-debe-cambiarse-en-produccion}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long jwtExpirationMs; // 24 horas por defecto

    // Conjunto para almacenar tokens invalidados (en una implementación real se usaría Redis o similar)
    private Set<String> blacklistedTokens = new HashSet<>();

    /**
     * Genera un token JWT para un usuario.
     * 
     * @param usuario El usuario para el que se genera el token
     * @return String con el token JWT
     */
    public String generateToken(Usuario usuario) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", usuario.getId());
            claims.put("email", usuario.getEmail());

            // Manejo seguro de posibles nulos
            if (usuario.getRol() != null) {
                claims.put("rol", usuario.getRol().getNombre());
            } else {
                claims.put("rol", "DESCONOCIDO");
            }

            claims.put("nombre", usuario.getNombre());
            claims.put("apellidos", usuario.getApellidos());

            logger.info("Generando token JWT para usuario: " + usuario.getEmail());

            // CAMBIO IMPORTANTE: Usamos HS256
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(usuario.getId().toString())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            logger.severe("Error al generar token: " + e.getMessage());
            throw new RuntimeException("Error al generar token", e);
        }
    }

    /**
     * Obtiene el ID de usuario a partir de un token JWT.
     * 
     * @param token El token JWT
     * @return Long con el ID del usuario
     */
    public Long getUserIdFromToken(String token) {
        try {
            // Verificar si el token está en la lista negra
            if (blacklistedTokens.contains(token)) {
                throw new RuntimeException("Token inválido o expirado");
            }

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            logger.severe("Error al obtener ID de usuario del token: " + e.getMessage());
            throw new RuntimeException("Error al procesar token", e);
        }
    }

    /**
     * Invalida un token JWT agregándolo a la lista negra.
     * 
     * @param token El token a invalidar
     */
    public void invalidateToken(String token) {
        if (token != null && !token.isEmpty()) {
            logger.info("Invalidando token JWT: " + token.substring(0, Math.min(10, token.length())) + "...");
            blacklistedTokens.add(token);
        } else {
            logger.warning("Intento de invalidar un token nulo o vacío");
        }
    }

    /**
     * Valida si un token JWT es válido.
     * 
     * @param token El token a validar
     * @return boolean indicando si el token es válido
     */
    public boolean validateToken(String token) {
        // Verificar si el token está en la lista negra
        if (blacklistedTokens.contains(token)) {
            return false;
        }

        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.warning("Token inválido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene la clave para firmar el token.
     * 
     * @return Key con la clave de firma
     */
    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
