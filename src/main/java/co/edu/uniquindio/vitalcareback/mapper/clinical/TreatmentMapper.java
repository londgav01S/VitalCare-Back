package co.edu.uniquindio.vitalcareback.mapper.clinical;

import co.edu.uniquindio.vitalcareback.Dto.clinical.TreatmentDTO;
import co.edu.uniquindio.vitalcareback.Model.clinical.Treatment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TreatmentMapper {
    TreatmentDTO toDTO(Treatment entity);
    Treatment toEntity(TreatmentDTO dto);
}

