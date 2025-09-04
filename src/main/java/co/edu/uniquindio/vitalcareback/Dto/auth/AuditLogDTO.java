package co.edu.uniquindio.vitalcareback.Dto.auth;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogDTO {
    private UUID id;
    private UUID userId;
    private String action;
    private LocalDateTime timestamp;
}

