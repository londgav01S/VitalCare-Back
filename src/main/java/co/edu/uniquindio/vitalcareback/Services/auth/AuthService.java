package co.edu.uniquindio.vitalcareback.Services.auth;

import co.edu.uniquindio.vitalcareback.Config.jwt.JwtUtil;
import co.edu.uniquindio.vitalcareback.Dto.auth.UserDTO;
import co.edu.uniquindio.vitalcareback.Model.auth.Role;
import co.edu.uniquindio.vitalcareback.Model.auth.User;
import co.edu.uniquindio.vitalcareback.Model.auth.UserRole;
import co.edu.uniquindio.vitalcareback.Repositories.auth.RoleRepository;
import co.edu.uniquindio.vitalcareback.Repositories.auth.UserRepository;
import co.edu.uniquindio.vitalcareback.Utils.JwtResponse;
import co.edu.uniquindio.vitalcareback.Utils.PasswordUtil;
import co.edu.uniquindio.vitalcareback.mapper.auth.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String DEFAULT_ROLE_NAME = "PATIENT"; // ajusta si tus datos seed usan "ROLE_PATIENT"

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordUtil passwordUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Registro por email + password, rol por defecto "PATIENT".
     */
    public UserDTO register(UserDTO userDTO) {
        userRepository.findByEmail(userDTO.getEmail())
                .ifPresent(u -> { throw new RuntimeException("El email ya está registrado"); });

        Role defaultRole = roleRepository.findByName(DEFAULT_ROLE_NAME)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + DEFAULT_ROLE_NAME));

        User user = userMapper.toEntity(userDTO);
        user.setPasswordHash(passwordUtil.encodePassword(userDTO.getPassword()));
        user.setEnabled(Boolean.TRUE);

        // Relación por entidad intermedia UserRole
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(defaultRole);
        user.getRoles().add(userRole);

        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    /**
     * Login por email + password (AuthenticationManager).
     */
    public JwtResponse login(UserDTO userDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
        );
        String subject = authentication.getName(); // aquí será el email
        String accessToken = jwtUtil.generateToken(subject);
        String refreshToken = jwtUtil.generateRefreshToken(subject);
        return new JwtResponse(accessToken, refreshToken);
    }

    /**
     * Renovación de access token a partir de refresh token.
     */
    public JwtResponse refreshToken(String refreshToken) {
        String email = jwtUtil.extractUsername(refreshToken);
        if (!jwtUtil.isTokenValid(refreshToken, email)) {
            throw new RuntimeException("Refresh token inválido");
        }
        String newAccess = jwtUtil.generateToken(email);
        return new JwtResponse(newAccess, refreshToken);
    }

    /**
     * Cambio de contraseña (requiere la actual).
     */
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordUtil.matches(currentPassword, u.getPasswordHash())) {
            throw new RuntimeException("La contraseña actual no es válida");
        }
        if (!passwordUtil.isStrongPassword(newPassword)) {
            throw new RuntimeException("La nueva contraseña no cumple la política de seguridad");
        }
        u.setPasswordHash(passwordUtil.encodePassword(newPassword));
        userRepository.save(u);
    }
}

