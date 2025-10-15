package co.edu.uniquindio.vitalcareback.Controller;

import co.edu.uniquindio.vitalcareback.Services.auth.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
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
        passwordResetService.resetPassword(payload.get("email"),
                payload.get("newPassword"),
                payload.get("refreshToken"));
        return ResponseEntity.ok("Contraseña actualizada exitosamente");
    }
}

