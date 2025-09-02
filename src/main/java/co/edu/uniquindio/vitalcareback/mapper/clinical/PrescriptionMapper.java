package co.edu.uniquindio.vitalcareback.mapper.clinical;

import co.edu.uniquindio.vitalcareback.Dto.clinical.PrescriptionDTO;
import co.edu.uniquindio.vitalcareback.Model.clinical.Prescription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
    PrescriptionDTO toDTO(Prescription entity);
    Prescription toEntity(PrescriptionDTO dto);
}

