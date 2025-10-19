package co.edu.uniquindio.vitalcareback.Config.jwt;

import co.edu.uniquindio.vitalcareback.Services.auth.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter
 *
 * Este filtro se ejecuta en cada petición HTTP entrante y se encarga de:
 * 1. Leer el header "Authorization" y extraer el token JWT.
 * 2. Validar el token JWT.
 * 3. Autenticar al usuario en el contexto de seguridad de Spring Security.
 *
 * Hereda de OncePerRequestFilter para garantizar que se ejecute una sola vez por petición.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * JwtUtil
     * Clase de utilidad para manejar operaciones con JWT como extracción de email
     * y validación de token.
     */
    private final JwtUtil jwtUtil;

    /**
     * CustomUserDetailsService
     * Servicio que carga los detalles de usuario desde la base de datos
     * usando el email del usuario.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * doFilterInternal
     *
     * Método principal del filtro que se ejecuta por cada petición HTTP.
     *
     * @param request  Objeto HttpServletRequest de la petición entrante
     * @param response Objeto HttpServletResponse de la respuesta
     * @param filterChain Cadena de filtros que permite continuar con la ejecución de la petición
     * @throws ServletException
     * @throws IOException
     *
     * Pasos que realiza:
     * 1. Obtiene el header "Authorization".
     * 2. Verifica que el header exista y comience con "Bearer ".
     * 3. Extrae el JWT del header.
     * 4. Extrae el email del token.
     * 5. Si no hay autenticación previa, carga el usuario y valida el token.
     * 6. Crea un objeto UsernamePasswordAuthenticationToken y lo guarda en SecurityContext.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Obtener el header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        // Si no existe el header o no empieza con "Bearer ", continuar con la cadena de filtros
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer token JWT del header
        jwt = authHeader.substring(7);

        // Proteger contra valores literales inválidos que algunos frontends pueden enviar
        if (jwt == null || jwt.isBlank() || "null".equalsIgnoreCase(jwt) || "undefined".equalsIgnoreCase(jwt)) {
            // No hay token válido -> continuar sin autenticar
            filterChain.doFilter(request, response);
            return;
        }

        String email = null;
        try {
            email = jwtUtil.extractEmail(jwt);
        } catch (Exception ex) {
            // Si el token es inválido/corrupto, no debemos bloquear la petición globalmente.
            // Simplemente seguimos la cadena de filtros y dejamos que el endpoint maneje la ausencia de auth
            System.out.println("JWT parse error: " + ex.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // Si se obtuvo un email y no hay autenticación en el contexto
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Cargar detalles del usuario desde la base de datos
            var userDetails = userDetailsService.loadUserByUsername(email);

            // Validar token
            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                // Crear token de autenticación y setear en el contexto de Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Logging de debug (puede eliminarse en producción)
        System.out.println("Authorization Header: " + authHeader);
        System.out.println("Token: " + jwt);

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
