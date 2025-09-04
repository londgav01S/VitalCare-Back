package co.edu.uniquindio.vitalcareback.Utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class PasswordUtil {

    private final PasswordEncoder passwordEncoder;

    // Regex: al menos 8 caracteres, una mayúscula, una minúscula y un dígito
    private static final Pattern STRONG_PWD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*()-_+=<>?";

    /**
     * Codifica (hashea) la contraseña usando el PasswordEncoder (BCrypt).
     */
    public String encodePassword(String rawPassword) {
        if (rawPassword == null) return null;
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Comprueba si el rawPassword coincide con el hash almacenado.
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) return false;
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Valida la fortaleza de la contraseña según la política (mínimo 8, 1 mayúscula, 1 minúscula, 1 dígito).
     */
    public boolean isStrongPassword(String rawPassword) {
        if (rawPassword == null) return false;
        return STRONG_PWD_PATTERN.matcher(rawPassword).matches();
    }

    /**
     * Genera una contraseña aleatoria segura de la longitud solicitada.
     * No menor a 8 por seguridad (se fuerza a 8 si se pasa un valor menor).
     */
    public String generateRandomPassword(int length) {
        int len = Math.max(length, 8);
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int idx = secureRandom.nextInt(CHARSET.length());
            sb.append(CHARSET.charAt(idx));
        }
        // opcional: codificar para facilitar transporte (no es necesario)
        return sb.toString();
    }

    /**
     * Genera un token seguro en Base64 para usos adicionales (p. ej. password reset token seed).
     */
    public String generateSecureToken(int numBytes) {
        byte[] bytes = new byte[Math.max(32, numBytes)];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}

