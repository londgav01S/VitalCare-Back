package co.edu.uniquindio.vitalcareback.Services.auth;

import co.edu.uniquindio.vitalcareback.Model.auth.User;
import co.edu.uniquindio.vitalcareback.Repositories.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CustomUserDetailsService
 *
 * Implementación personalizada de UserDetailsService de Spring Security.
 * Permite cargar los detalles de un usuario desde la base de datos
 * usando su email como identificador para autenticación.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Carga un usuario por su email y construye un UserDetails para Spring Security.
     *
     * @param email Email del usuario a autenticar
     * @return UserDetails con email, contraseña, estado y roles del usuario
     * @throws UsernameNotFoundException Si no se encuentra el usuario
     */
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Tomamos solo el primer rol asignado al usuario (generalmente habrá solo uno)
        SimpleGrantedAuthority authority = user.getRoles().stream()
                .findFirst()
                .map(ur -> new SimpleGrantedAuthority(ur.getRole().getName()))
                .orElseThrow(() -> new IllegalStateException("User has no role"));

        // Construye un objeto UserDetails de Spring Security con la información del usuario
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                user.getEnabled(),
                true, // cuenta no expirada
                true, // credenciales no expirada
                true, // cuenta no bloqueada
                List.of(authority)
        );
    }
}
