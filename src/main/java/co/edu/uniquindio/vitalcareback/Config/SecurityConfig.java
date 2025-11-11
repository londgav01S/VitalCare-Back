package co.edu.uniquindio.vitalcareback.Config;

import co.edu.uniquindio.vitalcareback.Config.jwt.JwtAuthenticationFilter;
import co.edu.uniquindio.vitalcareback.Services.auth.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * SecurityConfig
 *
 * Clase de configuración de seguridad de Spring Security para VitalCare.
 * Contiene la configuración de:
 * - JWT Filter
 * - CORS
 * - Password encoding
 * - AuthenticationProvider y AuthenticationManager
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Filtro de autenticación JWT que intercepta cada petición
     * y valida el token de acceso.
     */
    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Servicio personalizado de UserDetails que carga usuarios desde la DB.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Configuración principal de la cadena de filtros de seguridad.
     *
     * @param http HttpSecurity para configurar reglas de seguridad
     * @return SecurityFilterChain con la configuración de Spring Security
     * @throws Exception en caso de errores de configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configuración de CORS usando corsConfigurationSource()
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Deshabilita CSRF (no necesario para APIs REST con JWT)
                .csrf(csrf -> csrf.disable())

        // Configuración de autorización de endpoints
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**", "/actuator/**", "/api/payments/session", "/api/payments/webhook").permitAll() // Endpoints públicos específicos
            .requestMatchers("/api/**").authenticated() // El resto de /api requiere auth
            .anyRequest().authenticated()
        )

                // Añadir el filtro JWT antes del filtro de username/password
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración de CORS (Cross-Origin Resource Sharing).
     * Permite que el frontend pueda comunicarse con el backend
     * sin bloqueos de política de mismo origen.
     *
     * @return CorsConfigurationSource con la configuración de orígenes, métodos y headers
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Orígenes permitidos (frontend desplegado en Render y localhost para dev)
        configuration.setAllowedOriginPatterns(List.of(
                "https://vitalcare-jt3p.onrender.com",
                "http://localhost:*"
        ));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers permitidos en la petición
        configuration.setAllowedHeaders(List.of("*"));

        // Headers expuestos en la respuesta (JWT)
        configuration.setExposedHeaders(List.of("Authorization"));

        // Permitir envío de cookies y credenciales
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Bean de AuthenticationProvider.
     * Usa DaoAuthenticationProvider para autenticar usando UserDetailsService y password encoder.
     *
     * @return AuthenticationProvider configurado
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Bean de AuthenticationManager.
     * Proporciona el manager de autenticación de Spring Security.
     *
     * @param config AuthenticationConfiguration inyectado por Spring
     * @return AuthenticationManager
     * @throws Exception si falla la configuración
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean de PasswordEncoder.
     * Configura BCryptPasswordEncoder para almacenar contraseñas de forma segura.
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
