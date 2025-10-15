package co.edu.uniquindio.vitalcareback.Services.auth;

import co.edu.uniquindio.vitalcareback.Dto.auth.UserDTO;
import co.edu.uniquindio.vitalcareback.Model.auth.User;
import co.edu.uniquindio.vitalcareback.Repositories.auth.UserRepository;
import co.edu.uniquindio.vitalcareback.mapper.auth.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * UserService
 *
 * Servicio encargado de la gestión de usuarios en el sistema.
 * Permite obtener, actualizar, listar y eliminar usuarios,
 * utilizando UserRepository y mapeando a DTO mediante UserMapper.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id UUID del usuario
     * @return UserDTO con la información del usuario
     * @throws RuntimeException si el usuario no existe
     */
    @Transactional
    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return userMapper.toDTO(user);
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     *
     * @return Lista de UserDTO con todos los usuarios
     */
    @Transactional
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    /**
     * Actualiza un usuario existente con los datos proporcionados.
     *
     * @param id      UUID del usuario a actualizar
     * @param userDTO DTO con los nuevos datos
     * @return UserDTO actualizado
     * @throws RuntimeException si el usuario no existe
     */
    @Transactional
    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setEmail(userDTO.getEmail());
        user.setEnabled(userDTO.getEnabled());

        User updated = userRepository.save(user);
        return userMapper.toDTO(updated);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id UUID del usuario a eliminar
     */
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
