package co.edu.uniquindio.vitalcareback.Repositories.notifications;


import co.edu.uniquindio.vitalcareback.Model.auth.User;
import co.edu.uniquindio.vitalcareback.Model.notifications.Notification;
import co.edu.uniquindio.vitalcareback.Model.notifications.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    // Buscar por usuario (objeto)
    List<Notification> findByRecipient(User recipient);

    // Buscar por usuario id (Spring Data soporta navegación por propiedades)
    List<Notification> findByRecipientId(UUID recipientId);

    // Buscar no leídas por usuario
    List<Notification> findByRecipientAndReadFalse(User recipient);

    List<Notification> findByType(NotificationType type);
}

