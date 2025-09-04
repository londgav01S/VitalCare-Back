package co.edu.uniquindio.vitalcareback.mapper.scheduling;

import co.edu.uniquindio.vitalcareback.Dto.scheduling.AppointmentDTO;
import co.edu.uniquindio.vitalcareback.Model.scheduling.Appointment;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentDTO toDTO(Appointment entity);
    Appointment toEntity(AppointmentDTO dto);
    List<AppointmentDTO> toDTOList(List<Appointment> entities);
    List<Appointment> toEntityList(List<AppointmentDTO> dtos);
}

