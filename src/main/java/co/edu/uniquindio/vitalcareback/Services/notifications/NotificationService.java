package co.edu.uniquindio.vitalcareback.Services.notifications;

import co.edu.uniquindio.vitalcareback.Dto.notifications.NotificationDTO;
import co.edu.uniquindio.vitalcareback.Model.auth.User;
import co.edu.uniquindio.vitalcareback.Model.notifications.Notification;
import co.edu.uniquindio.vitalcareback.Model.notifications.NotificationType;
import co.edu.uniquindio.vitalcareback.Repositories.auth.UserRepository;
import co.edu.uniquindio.vitalcareback.Repositories.notifications.NotificationRepository;
import co.edu.uniquindio.vitalcareback.mapper.notifications.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final EmailService emailService;
    private final UserRepository userRepository; // para buscar usuario por id si hace falta

    /**
     * Crear y enviar una notificación.
     */
    @Transactional
    public NotificationDTO createNotification(NotificationDTO dto) {
        Notification notification = notificationMapper.toEntity(dto);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        Notification saved = notificationRepository.save(notification);

        // Disparar acciones según el tipo (usar email del recipient)
        if (saved.getType() == NotificationType.EMAIL) {
            // asegurar que recipient y su email existen
            User recipient = saved.getRecipient();
            if (recipient == null || recipient.getEmail() == null) {
                throw new RuntimeException("El destinatario o su correo no están definidos");
            }
            String to = recipient.getEmail();
            String subject = (saved.getTitle() != null && !saved.getTitle().isBlank())
                    ? saved.getTitle()
                    : "Notificación VitalCare";
            String body = saved.getMessage() != null ? saved.getMessage() : "";

            emailService.sendSimpleMessage(to, subject, body);

            notificationRepository.save(saved);
        }
        // otros canales (SMS, Push) aquí...

        return notificationMapper.toDTO(saved);
    }

    /**
     * Listar todas las notificaciones de un usuario por su id.
     */
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUserNotifications(UUID userId) {
        // Usamos el método derivado findByRecipientId
        return notificationRepository.findByRecipientId(userId).stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void sendNotification(String recipientEmail, String title, String message) {
        // buscar usuario por email
        User recipient = userRepository.findByEmail(recipientEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + recipientEmail));

        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setType(NotificationType.EMAIL);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        Notification saved = notificationRepository.save(notification);

        // enviar email
        emailService.sendSimpleMessage(recipientEmail, title, message);

        notificationRepository.save(saved);
    }

}

