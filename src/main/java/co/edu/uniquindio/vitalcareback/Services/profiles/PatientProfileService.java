package co.edu.uniquindio.vitalcareback.Services.profiles;


import co.edu.uniquindio.vitalcareback.Dto.profiles.PatientProfileDTO;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.PatientProfileRepository;
import co.edu.uniquindio.vitalcareback.mapper.profiles.PatientProfileMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientProfileService {

    private final PatientProfileRepository repository;
    private final PatientProfileMapper mapper;

    @Transactional
    public PatientProfileDTO create(PatientProfileDTO dto) {
        PatientProfile entity = mapper.toEntity(dto);
        PatientProfile saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Transactional
    public PatientProfileDTO getById(UUID id) {
        PatientProfile entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil de paciente no encontrado"));
        return mapper.toDTO(entity);
    }

    @Transactional
    public List<PatientProfileDTO> getAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public PatientProfileDTO update(UUID id, PatientProfileDTO dto) {
        PatientProfile entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil de paciente no encontrado"));
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        PatientProfile updated = repository.save(entity);
        return mapper.toDTO(updated);
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }


}

