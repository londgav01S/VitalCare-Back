package co.edu.uniquindio.vitalcareback.mapper.notifications;

import co.edu.uniquindio.vitalcareback.Dto.notifications.NotificationDTO;
import co.edu.uniquindio.vitalcareback.Model.notifications.Notification;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDTO toDTO(Notification entity);
    Notification toEntity(NotificationDTO dto);
    List<NotificationDTO> toDTOList(List<Notification> entities);
    List<Notification> toEntityList(List<NotificationDTO> dtos);
}

