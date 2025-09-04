package co.edu.uniquindio.vitalcareback.mapper.location;

import co.edu.uniquindio.vitalcareback.Dto.location.DepartmentDTO;
import co.edu.uniquindio.vitalcareback.Model.location.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentDTO toDTO(Department entity);
    Department toEntity(DepartmentDTO dto);
}

