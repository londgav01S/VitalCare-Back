package co.edu.uniquindio.vitalcareback.mapper.auth;


import co.edu.uniquindio.vitalcareback.Dto.auth.AuditLogDTO;
import co.edu.uniquindio.vitalcareback.Model.auth.AuditLog;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {
    AuditLogDTO toDTO(AuditLog entity);
    AuditLog toEntity(AuditLogDTO dto);
    List<AuditLogDTO> toDTOList(List<AuditLog> entities);
    List<AuditLog> toEntityList(List<AuditLogDTO> dtos);
}

