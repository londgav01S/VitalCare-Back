package co.edu.uniquindio.vitalcareback.Services.notifications;

import co.edu.uniquindio.vitalcareback.Dto.notifications.NotificationDTO;
import co.edu.uniquindio.vitalcareback.Model.auth.User;
import co.edu.uniquindio.vitalcareback.Model.notifications.Notification;
import co.edu.uniquindio.vitalcareback.Model.notifications.NotificationType;
import co.edu.uniquindio.vitalcareback.Repositories.auth.UserRepository;
import co.edu.uniquindio.vitalcareback.Repositories.notifications.NotificationRepository;
import co.edu.uniquindio.vitalcareback.mapper.notifications.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * NotificationService
 * Gestiona las notificaciones internas y los correos del sistema VitalCare.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final EmailService emailService;
    private final UserRepository userRepository;

    /**
     * Crear y enviar una notificación (correo + registro en BD).
     */
    @Transactional
    public NotificationDTO createNotification(NotificationDTO dto) {
        Notification notification = notificationMapper.toEntity(dto);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        Notification saved = notificationRepository.save(notification);

        if (saved.getType() == NotificationType.EMAIL) {
            User recipient = saved.getRecipient();
            if (recipient == null || recipient.getEmail() == null) {
                throw new RuntimeException("El destinatario o su correo no están definidos");
            }

            String to = recipient.getEmail();
            String subject = saved.getTitle() != null ? saved.getTitle() : "Notificación VitalCare";
            String body = saved.getMessage() != null ? saved.getMessage() : "";

            // Envía email
            emailService.sendEmail(to, subject, body);
            log.info("📧 Notificación enviada a {}", to);
        }

        return notificationMapper.toDTO(saved);
    }

    /**
     * Listar notificaciones de un usuario.
     */
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUserNotifications(UUID userId) {
        return notificationRepository.findByRecipientId(userId).stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crear y enviar una notificación directamente por correo.
     */
    @Transactional
    public void sendNotification(String recipientEmail, String title, String message) {
        User recipient = userRepository.findByEmail(recipientEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + recipientEmail));

        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setType(NotificationType.EMAIL);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        notificationRepository.save(notification);

        emailService.sendEmail(recipientEmail, title, message);
        log.info("📩 Correo enviado a {}", recipientEmail);
    }
}
