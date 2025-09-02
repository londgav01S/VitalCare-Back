package co.edu.uniquindio.vitalcareback.mapper.auth;


import co.edu.uniquindio.vitalcareback.Dto.auth.RoleDTO;
import co.edu.uniquindio.vitalcareback.Model.auth.Role;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toDTO(Role entity);
    Role toEntity(RoleDTO dto);
    List<RoleDTO> toDTOList(List<Role> entities);
    List<Role> toEntityList(List<RoleDTO> dtos);
}

