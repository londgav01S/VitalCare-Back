package co.edu.uniquindio.vitalcareback.mapper.profiles;

import co.edu.uniquindio.vitalcareback.Dto.profiles.DoctorProfileDTO;
import co.edu.uniquindio.vitalcareback.Model.profiles.DoctorProfile;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorProfileMapper {
    DoctorProfileDTO toDTO(DoctorProfile entity);
    DoctorProfile toEntity(DoctorProfileDTO dto);
    List<DoctorProfileDTO> toDTOList(List<DoctorProfile> entities);
    List<DoctorProfile> toEntityList(List<DoctorProfileDTO> dtos);
}

