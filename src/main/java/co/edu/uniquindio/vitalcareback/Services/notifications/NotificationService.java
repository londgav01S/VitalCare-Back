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

/**
 * NotificationService
 *
 * Servicio encargado de gestionar las notificaciones dentro del sistema VitalCare.
 * Permite crear notificaciones, enviarlas vía email y consultar las notificaciones
 * de un usuario específico.
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final EmailService emailService;
    private final UserRepository userRepository;

    /**
     * Crear y enviar una notificación.
     *
     * - Crea una notificación en la base de datos.
     * - Si el tipo es EMAIL, envía un correo al destinatario.
     * - Otros canales (SMS, Push) podrían agregarse en el futuro.
     *
     * @param dto DTO de notificación con la información a crear
     * @return NotificationDTO Notificación creada y persistida
     * @throws RuntimeException si el destinatario o su email no están definidos
     */
    @Transactional
    public NotificationDTO createNotification(NotificationDTO dto) {
        Notification notification = notificationMapper.toEntity(dto);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        Notification saved = notificationRepository.save(notification);

        // Enviar email si el tipo de notificación es EMAIL
        if (saved.getType() == NotificationType.EMAIL) {
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

        return notificationMapper.toDTO(saved);
    }

    /**
     * Listar todas las notificaciones de un usuario.
     *
     * @param userId UUID del usuario destinatario
     * @return Lista de NotificationDTO con las notificaciones del usuario
     */
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUserNotifications(UUID userId) {
        return notificationRepository.findByRecipientId(userId).stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crear y enviar una notificación directamente a un email.
     *
     * - Busca el usuario por su email.
     * - Crea la notificación en la base de datos.
     * - Envía el correo electrónico al destinatario.
     *
     * @param recipientEmail Email del destinatario
     * @param title          Asunto de la notificación/correo
     * @param message        Mensaje de la notificación/correo
     * @throws RuntimeException si el usuario no existe
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

        Notification saved = notificationRepository.save(notification);

        // Enviar correo electrónico
        emailService.sendSimpleMessage(recipientEmail, title, message);

        notificationRepository.save(saved);
    }

}
