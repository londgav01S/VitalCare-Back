package co.edu.uniquindio.vitalcareback.mapper.profiles;


import co.edu.uniquindio.vitalcareback.Dto.profiles.PatientProfileDTO;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", uses = {co.edu.uniquindio.vitalcareback.mapper.clinical.MedicalRecordMapper.class})
public interface PatientProfileMapper {
    @Mapping(target = "medicalRecord", source = "medicalRecord")
    PatientProfileDTO toDTO(PatientProfile entity);

    @Mapping(target = "medicalRecord", source = "medicalRecord")
    PatientProfile toEntity(PatientProfileDTO dto);

    List<PatientProfileDTO> toDTOList(List<PatientProfile> entities);
    List<PatientProfile> toEntityList(List<PatientProfileDTO> dtos);
}

