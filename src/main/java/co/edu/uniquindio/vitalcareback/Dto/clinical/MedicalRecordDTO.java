package co.edu.uniquindio.vitalcareback.Dto.clinical;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordDTO {
    private UUID id;
    private String notes;
}

