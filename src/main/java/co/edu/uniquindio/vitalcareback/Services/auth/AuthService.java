package co.edu.uniquindio.vitalcareback.Services.auth;

import co.edu.uniquindio.vitalcareback.Config.jwt.JwtUtil;
import co.edu.uniquindio.vitalcareback.Dto.auth.UserDTO;
import co.edu.uniquindio.vitalcareback.Model.auth.Role;
import co.edu.uniquindio.vitalcareback.Model.auth.User;
import co.edu.uniquindio.vitalcareback.Model.auth.UserRole;
import co.edu.uniquindio.vitalcareback.Repositories.auth.RoleRepository;
import co.edu.uniquindio.vitalcareback.Repositories.auth.UserRepository;
import co.edu.uniquindio.vitalcareback.Services.notifications.EmailService;
import co.edu.uniquindio.vitalcareback.Utils.JwtResponse;
import co.edu.uniquindio.vitalcareback.Utils.PasswordUtil;
import co.edu.uniquindio.vitalcareback.mapper.auth.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * AuthService
 *
 * Servicio encargado de la lógica de autenticación y registro de usuarios.
 * Incluye registro, login, renovación de tokens, cambio de contraseña y consulta de usuario.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * Nombre del rol por defecto para usuarios registrados vía registro general.
     */
    private static final String DEFAULT_ROLE_NAME = "PATIENT"; // Ajusta según tus datos seed

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordUtil passwordUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    /**
     * Registro de un nuevo usuario con rol por defecto "PATIENT".
     *
     * @param userDTO DTO con los datos del usuario a registrar
     * @return UserDTO con la información del usuario registrado
     */
    public UserDTO register(UserDTO userDTO) {
        userRepository.findByEmail(userDTO.getEmail())
                .ifPresent(u -> { throw new RuntimeException("El email ya está registrado"); });

        Role defaultRole = roleRepository.findByName(DEFAULT_ROLE_NAME)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + DEFAULT_ROLE_NAME));

        User user = userMapper.toEntity(userDTO);
        user.setPasswordHash(passwordUtil.encodePassword(userDTO.getPassword()));
        user.setEnabled(Boolean.TRUE);
        // Si el front no envía state, lo dejamos true por defecto para evitar NPE/errores al persistir
        if (user.getState() == null) {
            user.setState(Boolean.TRUE);
        }

        // Relación por entidad intermedia UserRole
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(defaultRole);
        user.getRoles().add(userRole);

        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    /**
     * Login de un usuario mediante email y contraseña.
     *
     * @param userDTO DTO con email y password del usuario
     * @return JwtResponse con accessToken y refreshToken
     */
    public JwtResponse login(UserDTO userDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
        );

        String accessToken = jwtUtil.generateToken(authentication.getName());
        String refreshToken = jwtUtil.generateRefreshToken(authentication.getName());

        emailService.sendLoginAlert(userDTO.getEmail(), "");

        return new JwtResponse(accessToken, refreshToken);
    }

    /**
     * Renovación de access token a partir de un refresh token válido.
     *
     * @param refreshToken Token de refresco
     * @return JwtResponse con nuevo accessToken y el refreshToken original
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
     * Cambio de contraseña de un usuario. Requiere la contraseña actual y valida la política de seguridad.
     *
     * @param userId ID del usuario
     * @param currentPassword Contraseña actual
     * @param newPassword Nueva contraseña a establecer
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

    /**
     * Extrae el email de un token JWT.
     *
     * @param token Token JWT
     * @return Email contenido en el token
     */
    public String getEmailFromToken(String token) {
        return jwtUtil.extractEmail(token);
    }

    /**
     * Obtiene un usuario por su email.
     *
     * @param email Email del usuario
     * @return UserDTO con la información del usuario
     */
    public UserDTO getUserByEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return userMapper.toDTO(user);
    }
}
