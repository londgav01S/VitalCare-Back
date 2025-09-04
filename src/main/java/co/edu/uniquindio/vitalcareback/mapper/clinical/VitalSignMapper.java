package co.edu.uniquindio.vitalcareback.mapper.clinical;


import co.edu.uniquindio.vitalcareback.Dto.clinical.VitalSignDTO;
import co.edu.uniquindio.vitalcareback.Model.clinical.VitalSign;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VitalSignMapper {
    VitalSignDTO toDTO(VitalSign entity);
    VitalSign toEntity(VitalSignDTO dto);
}

