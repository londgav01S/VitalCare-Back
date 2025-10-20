package co.edu.uniquindio.vitalcareback.Repositories.profiles;

import co.edu.uniquindio.vitalcareback.Dto.profiles.DoctorProfileDTO;
import co.edu.uniquindio.vitalcareback.Model.profiles.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * DoctorProfileRepository
 *
 * Repositorio JPA para la entidad DoctorProfile.
 * Proporciona métodos para acceder a la base de datos relacionados con perfiles de doctores,
 * incluyendo búsqueda por ID y obtención de todos los perfiles como DTOs.
 */
@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, UUID> {

    /**
     * Busca un perfil de doctor por su ID.
     *
     * @param id ID del perfil del doctor
     * @return Optional<DoctorProfile> que puede contener el perfil si existe
     */
    Optional<DoctorProfile> findById(UUID id);

    /**
     * Obtiene todos los perfiles de doctores y los transforma en DoctorProfileDTO.
     * Este query selecciona solo los campos necesarios para la vista o listado.
     *
     * @return Lista de DoctorProfileDTO con ID, apellido y número de licencia
     */
    @Query("SELECT new co.edu.uniquindio.vitalcareback.Dto.profiles.DoctorProfileDTO(d.id, d.lastName, d.licenseNumber) FROM DoctorProfile d")
    List<DoctorProfileDTO> findAllDoctorProfiles();

    /**
     * Obtiene el perfil del doctor por el email del usuario asociado y lo proyecta a DoctorProfileDTO
     * con los datos completos necesarios (incluye ID del DoctorProfile).
     *
     * @param email correo electrónico del usuario doctor
     * @return DoctorProfileDTO con datos del doctor si existe
     */
    @Query("SELECT new co.edu.uniquindio.vitalcareback.Dto.profiles.DoctorProfileDTO("
            + " d.id, u.name, u.idNumber, d.lastName, d.specialty, d.licenseNumber, u.email)"
            + " FROM DoctorProfile d JOIN d.user u WHERE u.email = :email")
    Optional<DoctorProfileDTO> findDoctorProfileDtoByUserEmail(@Param("email") String email);
}
