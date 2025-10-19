package co.edu.uniquindio.vitalcareback.Controller;

import co.edu.uniquindio.vitalcareback.Services.auth.PasswordResetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody Map<String, String> payload) throws IOException {
        passwordResetService.requestPasswordReset(payload.get("email"));
        return ResponseEntity.ok("Código enviado al correo electrónico");
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<?> verifyResetCode(@RequestBody Map<String, String> payload) {
        String token = passwordResetService.verifyResetCode(payload.get("email"), payload.get("code"));
        return ResponseEntity.ok(Map.of("refreshToken", token));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String newPassword = payload.get("newPassword");
        String refreshToken = payload.get("refreshToken");

        // Log básico (no imprimir el token completo en producción)
        log.info("[RESET PASSWORD] Attempt for email={}, newPasswordLen={}, refreshTokenPresent={}",
                email,
                newPassword == null ? 0 : newPassword.length(),
                refreshToken != null && !refreshToken.isBlank());

        try {
            passwordResetService.resetPassword(email, newPassword, refreshToken);
            return ResponseEntity.ok("Contraseña actualizada exitosamente");
        } catch (ResponseStatusException ex) {
            log.warn("[RESET PASSWORD] ResponseStatusException: {} - {}", ex.getStatusCode(), ex.getReason());
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", ex.getReason()));
        } catch (Exception ex) {
            log.error("[RESET PASSWORD] Error inesperado al intentar restablecer contraseña", ex);
            // Temporalmente devolver el mensaje de excepción para depuración del cliente
            return ResponseEntity.status(500).body(Map.of("message", "Error interno", "error", ex.getMessage()));
        }
    }
}

