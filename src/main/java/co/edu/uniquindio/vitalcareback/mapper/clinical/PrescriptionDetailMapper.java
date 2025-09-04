package co.edu.uniquindio.vitalcareback.mapper.clinical;

import co.edu.uniquindio.vitalcareback.Dto.clinical.PrescriptionDetailDTO;
import co.edu.uniquindio.vitalcareback.Model.clinical.PrescriptionDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrescriptionDetailMapper {
    PrescriptionDetailDTO toDTO(PrescriptionDetail entity);
    PrescriptionDetail toEntity(PrescriptionDetailDTO dto);
}

