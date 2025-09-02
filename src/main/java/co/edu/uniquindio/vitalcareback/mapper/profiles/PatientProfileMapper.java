package co.edu.uniquindio.vitalcareback.mapper.profiles;


import co.edu.uniquindio.vitalcareback.Dto.profiles.PatientProfileDTO;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientProfileMapper {
    PatientProfileDTO toDTO(PatientProfile entity);
    PatientProfile toEntity(PatientProfileDTO dto);
    List<PatientProfileDTO> toDTOList(List<PatientProfile> entities);
    List<PatientProfile> toEntityList(List<PatientProfileDTO> dtos);
}

