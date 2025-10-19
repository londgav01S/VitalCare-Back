package co.edu.uniquindio.vitalcareback.Services.auth;

import co.edu.uniquindio.vitalcareback.Config.jwt.JwtUtil;
import co.edu.uniquindio.vitalcareback.Model.auth.PasswordResetToken;
import co.edu.uniquindio.vitalcareback.Model.auth.User;
import co.edu.uniquindio.vitalcareback.Repositories.auth.PasswordResetTokenRepository;
import co.edu.uniquindio.vitalcareback.Repositories.auth.UserRepository;
import co.edu.uniquindio.vitalcareback.Services.notifications.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    // 1️⃣ Solicitar código
    public void requestPasswordReset(String email) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar token de 6 dígitos
        String code = String.format("%06d", new Random().nextInt(999999));
        PasswordResetToken token = tokenRepository.findByUser(user)
                .orElse(new PasswordResetToken());

        token.setUser(user);
        token.setToken(code);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(10)); // 10 minutos de validez
        tokenRepository.save(token);

        emailService.sendPasswordResetCode(email, code);

    }

    // 2️⃣ Verificar código
    public String verifyResetCode(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        PasswordResetToken token = tokenRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Código no solicitado"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El código ha expirado");
        }

        if (!token.getToken().equals(code)) {
            throw new RuntimeException("Código inválido");
        }

        // Si es válido → generar refresh token temporal
        return jwtUtil.generatePasswordResetToken(user.getEmail());
    }

    // 3️⃣ Cambiar contraseña con refresh token válido
    @Transactional
    public void resetPassword(String email, String newPassword, String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank() || !jwtUtil.validateToken(refreshToken)) {
            // Lanzar una excepción con código HTTP adecuado para que el cliente la interprete
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Log para depuración (no mostrar tokens)
        System.out.println("[DEBUG] Reset password for user=" + user.getEmail());

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.deleteByUser(user);
    }


}

