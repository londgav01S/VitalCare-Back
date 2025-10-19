package co.edu.uniquindio.vitalcareback.mapper.auth;

import co.edu.uniquindio.vitalcareback.Dto.auth.UserDTO;
import co.edu.uniquindio.vitalcareback.Model.auth.User;
import co.edu.uniquindio.vitalcareback.mapper.profiles.DoctorProfileMapper;
import co.edu.uniquindio.vitalcareback.mapper.profiles.PatientProfileMapper;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", uses = {
        PatientProfileMapper.class,
        DoctorProfileMapper.class,
        RoleMapper.class
})
public interface UserMapper {

    @Mapping(target = "role",
            expression = "java(user.getRoles() != null && !user.getRoles().isEmpty() ? " +
                    "user.getRoles().iterator().next().getRole().getName() : null)")
    @Mapping(target = "state", source = "state")
    UserDTO toDTO(User user);
    @Mapping(target = "state", source = "state")
    User toEntity(UserDTO dto);
    List<UserDTO> toDTOList(List<User> entities);
    List<User> toEntityList(List<UserDTO> dtos);


}

