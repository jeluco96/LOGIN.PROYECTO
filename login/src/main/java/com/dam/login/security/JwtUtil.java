package com.dam.login.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utilidad para operaciones con tokens JWT.
 * Permite generar, validar y extraer información de tokens JWT.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret:defaultSecretKey123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 horas por defecto
    private long expiration;

    /**
     * Genera un token JWT para el usuario especificado.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Token JWT generado
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Genera un token JWT para el email del usuario con claims adicionales.
     *
     * @param email Email del usuario
     * @param claims Claims adicionales a incluir en el token
     * @return Token JWT generado
     */
    public String generateToken(String email, Map<String, Object> claims) {
        return createToken(claims, email);
    }

    /**
     * Crea un token JWT con los claims y subject proporcionados.
     *
     * @param claims Claims a incluir en el token
     * @param subject Sujeto del token (normalmente el email del usuario)
     * @return Token JWT creado
     */
    private String createToken(Map<String, Object> claims, String subject) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida un token JWT verificando la firma y que no haya expirado.
     *
     * @param token Token JWT a validar
     * @param userDetails Detalles del usuario para comparar con el token
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getEmailFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Verifica si un token es válido (firma correcta y no expirado).
     *
     * @param token Token JWT a verificar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean isTokenValid(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrae el email (subject) del token JWT.
     *
     * @param token Token JWT
     * @return Email extraído del token
     */
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     *
     * @param token Token JWT
     * @return Fecha de expiración
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Extrae un claim específico del token JWT usando la función proporcionada.
     *
     * @param token Token JWT
     * @param claimsResolver Función para extraer el claim deseado
     * @return El claim extraído
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token JWT.
     *
     * @param token Token JWT
     * @return Claims contenidos en el token
     */
    private Claims getAllClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verifica si un token JWT ha expirado.
     *
     * @param token Token JWT
     * @return true si el token ha expirado, false en caso contrario
     */
    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}
