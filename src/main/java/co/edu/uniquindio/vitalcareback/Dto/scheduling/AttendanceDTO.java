package co.edu.uniquindio.vitalcareback.Dto.scheduling;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDTO {
    private UUID id;
    private UUID appointmentId;
    private Boolean attended;
    private String notes;
}

