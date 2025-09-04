package co.edu.uniquindio.vitalcareback.mapper.auth;


import co.edu.uniquindio.vitalcareback.Dto.auth.UserDTO;
import co.edu.uniquindio.vitalcareback.Model.auth.User;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User entity);
    User toEntity(UserDTO dto);
    List<UserDTO> toDTOList(List<User> entities);
    List<User> toEntityList(List<UserDTO> dtos);
}

