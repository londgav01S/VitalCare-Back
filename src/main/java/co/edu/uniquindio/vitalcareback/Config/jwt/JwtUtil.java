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
import java.util.Map;
import java.util.function.Function;

/**
 * JwtUtil
 *
 * Clase utilitaria para manejar JSON Web Tokens (JWT).
 * Proporciona métodos para:
 * - Generar tokens (access token y refresh token).
 * - Extraer información (claims) de un token.
 * - Validar tokens.
 *
 * Utiliza jjwt (io.jsonwebtoken) y Spring Security.
 */
@Component
public class JwtUtil {

    /**
     * Clave secreta usada para firmar los tokens.
     * Se puede configurar desde application.properties o usar valor por defecto.
     */
    @Value("${jwt.secret:U7t8C34aqstQ6soj1Bub9JYk7Y4BnNT3}")
    private String SECRET_KEY;

    /**
     * Tiempo de expiración de los access tokens en milisegundos (por defecto 1 hora)
     */
    @Value("${jwt.expirationMs:3600000}")
    private long ACCESS_TOKEN_EXPIRATION;

    /**
     * Tiempo de expiración de los refresh tokens en milisegundos (por defecto 7 días)
     */
    @Value("${jwt.refreshExpirationMs:604800000}")
    private long REFRESH_TOKEN_EXPIRATION;

    /**
     * Obtiene la clave de firma a partir de SECRET_KEY.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /* ------------------ Extracción de Claims ------------------ */

    /**
     * Extrae el username (subject) del token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiración del token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae un claim específico usando una función lambda.
     *
     * @param token Token JWT
     * @param claimsResolver Función para extraer un claim de Claims
     * @param <T> Tipo de dato del claim
     * @return Claim solicitado
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims de un token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verifica si un token ha expirado.
     * Retorna true si está expirado o no puede ser parseado.
     */
    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception ex) {
            return true; // token inválido o corrupto se considera expirado
        }
    }

    /* ------------------ Generación de Tokens ------------------ */

    /**
     * Genera un access token con subject = email y expiración corta (ACCESS_TOKEN_EXPIRATION)
     */
    public String generateToken(String email) {
        return generateToken(Map.of(), email, ACCESS_TOKEN_EXPIRATION);
    }

    /**
     * Genera un refresh token con subject = email y expiración larga (REFRESH_TOKEN_EXPIRATION)
     */
    public String generateRefreshToken(String email) {
        return generateToken(Map.of("type", "refresh"), email, REFRESH_TOKEN_EXPIRATION);
    }

    /**
     * Genera un token con claims adicionales y duración personalizada.
     *
     * @param extraClaims Claims extra a incluir
     * @param subject Subject del token (generalmente el username/email)
     * @param expirationMillis Tiempo de expiración en milisegundos
     * @return Token JWT firmado
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
     * Genera token usando UserDetails y claims personalizados.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return generateToken(extraClaims, userDetails.getUsername(), ACCESS_TOKEN_EXPIRATION);
    }

    /**
     * Extrae email del token (alias de extractUsername)
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /* ------------------ Validación de Tokens ------------------ */

    /**
     * Valida token comparando el username de UserDetails y comprobando expiración.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Valida token comparando con un username en String.
     * Útil para flujos de refresh token donde no hay UserDetails.
     */
    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername != null && tokenUsername.equals(username)) && !isTokenExpired(token);
    }
}
