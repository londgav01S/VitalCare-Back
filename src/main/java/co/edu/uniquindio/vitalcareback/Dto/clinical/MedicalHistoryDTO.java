package co.edu.uniquindio.vitalcareback.Dto.clinical;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalHistoryDTO {
    private UUID id;
    private UUID patientId;
    private String condition;
    private String notes;
}

