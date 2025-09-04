package co.edu.uniquindio.vitalcareback.Dto.clinical;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationDTO {
    private UUID id;
    private UUID appointmentId;
    private UUID doctorId;
    private String reason;
    private String notes;
}

