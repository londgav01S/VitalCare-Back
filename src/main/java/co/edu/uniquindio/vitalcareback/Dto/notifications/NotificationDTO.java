package co.edu.uniquindio.vitalcareback.Dto.notifications;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private UUID id;
    private UUID recipientId;
    private String type;
    private String message;
    private String title;
    private Boolean read;
    private LocalDateTime sentAt;
}

