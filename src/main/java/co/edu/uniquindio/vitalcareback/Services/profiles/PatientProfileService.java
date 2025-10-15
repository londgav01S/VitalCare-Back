package co.edu.uniquindio.vitalcareback.Services.profiles;

import co.edu.uniquindio.vitalcareback.Dto.profiles.PatientProfileDTO;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.PatientProfileRepository;
import co.edu.uniquindio.vitalcareback.mapper.profiles.PatientProfileMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * PatientProfileService
 *
 * Servicio encargado de la gestión de los perfiles de pacientes dentro del sistema VitalCare.
 * Permite crear, consultar, actualizar y eliminar perfiles de pacientes.
 */
@Service
@RequiredArgsConstructor
public class PatientProfileService {

    private final PatientProfileRepository repository;
    private final PatientProfileMapper mapper;

    /**
     * Crear un perfil de paciente.
     *
     * @param dto DTO con la información del paciente a crear
     * @return PatientProfileDTO Perfil de paciente persistido
     */
    @Transactional
    public PatientProfileDTO create(PatientProfileDTO dto) {
        PatientProfile entity = mapper.toEntity(dto);
        PatientProfile saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    /**
     * Obtener un perfil de paciente por su ID.
     *
     * @param id UUID del paciente
     * @return PatientProfileDTO Perfil de paciente
     * @throws RuntimeException si el perfil no existe
     */
    @Transactional
    public PatientProfileDTO getById(UUID id) {
        PatientProfile entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil de paciente no encontrado"));
        return mapper.toDTO(entity);
    }

    /**
     * Listar todos los perfiles de pacientes.
     *
     * @return Lista de PatientProfileDTO con todos los perfiles
     */
    @Transactional
    public List<PatientProfileDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    /**
     * Actualizar un perfil de paciente.
     *
     * Solo se actualizan teléfono y dirección en este ejemplo.
     *
     * @param id  UUID del paciente
     * @param dto DTO con los datos a actualizar
     * @return PatientProfileDTO Perfil actualizado
     * @throws RuntimeException si el perfil no existe
     */
    @Transactional
    public PatientProfileDTO update(UUID id, PatientProfileDTO dto) {
        PatientProfile entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil de paciente no encontrado"));
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        PatientProfile updated = repository.save(entity);
        return mapper.toDTO(updated);
    }

    /**
     * Eliminar un perfil de paciente por su ID.
     *
     * @param id UUID del paciente
     */
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

}
