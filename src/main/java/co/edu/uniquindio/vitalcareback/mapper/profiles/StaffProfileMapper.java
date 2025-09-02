package co.edu.uniquindio.vitalcareback.mapper.profiles;

import co.edu.uniquindio.vitalcareback.Dto.profiles.StaffProfileDTO;
import co.edu.uniquindio.vitalcareback.Model.profiles.StaffProfile;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StaffProfileMapper {
    StaffProfileDTO toDTO(StaffProfile entity);
    StaffProfile toEntity(StaffProfileDTO dto);
    List<StaffProfileDTO> toDTOList(List<StaffProfile> entities);
    List<StaffProfile> toEntityList(List<StaffProfileDTO> dtos);
}

