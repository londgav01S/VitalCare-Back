package co.edu.uniquindio.vitalcareback.mapper.clinical;

import co.edu.uniquindio.vitalcareback.Dto.clinical.MedicalHistoryDTO;
import co.edu.uniquindio.vitalcareback.Model.clinical.MedicalHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalHistoryMapper {
    MedicalHistoryDTO toDTO(MedicalHistory entity);
    MedicalHistory toEntity(MedicalHistoryDTO dto);
}

