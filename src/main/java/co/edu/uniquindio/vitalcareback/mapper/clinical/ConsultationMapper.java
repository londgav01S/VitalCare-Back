package co.edu.uniquindio.vitalcareback.mapper.clinical;

import co.edu.uniquindio.vitalcareback.Dto.clinical.ConsultationDTO;
import co.edu.uniquindio.vitalcareback.Model.clinical.Consultation;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ConsultationMapper {
    ConsultationDTO toDTO(Consultation entity);
    Consultation toEntity(ConsultationDTO dto);
    List<ConsultationDTO> toDTOList(List<Consultation> entities);
    List<Consultation> toEntityList(List<ConsultationDTO> dtos);
}

