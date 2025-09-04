package co.edu.uniquindio.vitalcareback.mapper.scheduling;

import co.edu.uniquindio.vitalcareback.Dto.scheduling.AttendanceDTO;
import co.edu.uniquindio.vitalcareback.Model.scheduling.Attendance;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
    AttendanceDTO toDTO(Attendance entity);
    Attendance toEntity(AttendanceDTO dto);
    List<AttendanceDTO> toDTOList(List<Attendance> entities);
    List<Attendance> toEntityList(List<AttendanceDTO> dtos);
}

