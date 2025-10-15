package co.edu.uniquindio.vitalcareback.Controller;

import co.edu.uniquindio.vitalcareback.Dto.auth.UserDTO;
import co.edu.uniquindio.vitalcareback.Services.auth.AuthService;
import co.edu.uniquindio.vitalcareback.Utils.JwtResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthenticationController
 *
 * Controlador REST encargado de manejar la autenticación y registro de usuarios.
 * Proporciona endpoints para:
 * - Registro de nuevos usuarios
 * - Login y generación de JWT
 * - Refresh de tokens
 * - Obtener información del usuario autenticado
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    /**
     * Servicio que contiene la lógica de autenticación y manejo de usuarios.
     */
    private final AuthService authService;

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param userDTO DTO con la información del usuario a registrar
     * @return ResponseEntity con el usuario registrado
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authService.register(userDTO));
    }

    /**
     * Endpoint para iniciar sesión.
     *
     * @param userDTO DTO con el email y password del usuario
     * @return ResponseEntity con un JwtResponse que contiene access y refresh tokens
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authService.login(userDTO));
    }

    /**
     * Endpoint para refrescar un token de acceso usando un refresh token válido.
     *
     * @param refreshToken Token de refresco
     * @return ResponseEntity con un nuevo JwtResponse que contiene access token
     */
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestParam String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    /**
     * Endpoint para obtener información del usuario actualmente autenticado.
     *
     * @param request HttpServletRequest para leer el header Authorization
     * @return ResponseEntity con la información del usuario o error si el token es inválido
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        // Verifica que el header contenga un Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token no presente o inválido");
        }

        String token = authHeader.substring(7);
        try {
            // Extrae el email del token y obtiene el usuario desde la DB
            String email = authService.getEmailFromToken(token);
            var user = authService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (Exception ex) {
            // Token inválido o expirado
            return ResponseEntity.status(403).body("Token inválido o expirado");
        }
    }

}
