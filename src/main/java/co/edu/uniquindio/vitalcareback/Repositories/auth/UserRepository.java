package co.edu.uniquindio.vitalcareback.Repositories.auth;

import co.edu.uniquindio.vitalcareback.Model.auth.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * UserRepository
 *
 * Repositorio JPA para la entidad User.
 * Proporciona métodos para acceder a la base de datos relacionados con usuarios,
 * incluyendo búsqueda por email y verificación de existencia.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Busca un usuario por su email y carga también sus roles asociados.
     *
     * @param email Email del usuario a buscar
     * @return Optional<User> que puede contener el usuario con sus roles
     */
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    /**
     * Busca un usuario por su email utilizando JPQL y realiza un fetch de roles explícito.
     * Esto evita el problema de LazyInitializationException al acceder a roles fuera de la sesión.
     *
     * @param email Email del usuario a buscar
     * @return Usuario con sus roles cargados
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    User findByEmailWithRoles(@Param("email") String email);

    /**
     * Verifica si existe un usuario con un email específico.
     *
     * @param email Email a verificar
     * @return true si existe un usuario con ese email, false en caso contrario
     */
    boolean existsByEmail(String email);
}
