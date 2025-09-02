package co.edu.uniquindio.vitalcareback.Dto.clinical;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDetailDTO {
    private UUID id;
    private UUID prescriptionId;
    private UUID medicationId;
    private String dosage;
    private String frequency;
}

