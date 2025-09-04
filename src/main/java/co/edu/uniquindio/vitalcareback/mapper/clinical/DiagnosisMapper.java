package co.edu.uniquindio.vitalcareback.mapper.clinical;

import co.edu.uniquindio.vitalcareback.Dto.clinical.DiagnosisDTO;
import co.edu.uniquindio.vitalcareback.Model.clinical.Diagnosis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiagnosisMapper {
    DiagnosisDTO toDTO(Diagnosis entity);
    Diagnosis toEntity(DiagnosisDTO dto);
}

