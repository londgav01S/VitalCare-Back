package co.edu.uniquindio.vitalcareback.Config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret:U7t8C34aqstQ6soj1Bub9JYk7Y4BnNT3}")
    private String SECRET_KEY;

    @Value("${jwt.expirationMs:3600000}") // 1 hora por defecto
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${jwt.refreshExpirationMs:604800000}") // 7 días por defecto
    private long REFRESH_TOKEN_EXPIRATION;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /* ------------------ Extracción de Claims ------------------ */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception ex) {
            return true; // si no se puede parsear, tratar como expirado/no válido
        }
    }

    /* ------------------ Generación de Tokens ------------------ */

    /**
     * Genera un access token (expiración corta) con subject = username.
     */
    public String generateToken(String username) {
        return generateToken(Map.of(), username, ACCESS_TOKEN_EXPIRATION);
    }

    /**
     * Genera un refresh token (expiración larga) con subject = username.
     */
    public String generateRefreshToken(String username) {
        return generateToken(Map.of("type", "refresh"), username, REFRESH_TOKEN_EXPIRATION);
    }

    /**
     * Generador genérico que permite pasar claims extra y duración.
     */
    public String generateToken(Map<String, Object> extraClaims, String subject, long expirationMillis) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Syntactic sugar: generar con custom claims y usando access expiration por defecto.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return generateToken(extraClaims, userDetails.getUsername(), ACCESS_TOKEN_EXPIRATION);
    }

    /* ------------------ Validación de Tokens ------------------ */

    /**
     * Valida token comparando username y comprobando expiración.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Alternativa práctica: validar token comparando con un username (String).
     * Útil cuando no tienes UserDetails en mano (p. ej. refresh workflow).
     */
    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername != null && tokenUsername.equals(username)) && !isTokenExpired(token);
    }

}

