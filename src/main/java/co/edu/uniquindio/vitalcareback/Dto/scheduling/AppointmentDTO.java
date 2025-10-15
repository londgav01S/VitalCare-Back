package co.edu.uniquindio.vitalcareback.Dto.scheduling;

import co.edu.uniquindio.vitalcareback.Dto.auth.UserDTO;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {
    private UUID id;
    private UUID patientId;
    private UUID doctorId;
    private UUID siteId;
    private LocalDateTime scheduledDate;
    private String status;
    private String patientEmail;

    private UserDTO patient; // 👈 info del paciente (con rol)
    private UserDTO doctor;

}

