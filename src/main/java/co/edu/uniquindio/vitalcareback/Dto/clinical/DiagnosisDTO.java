package co.edu.uniquindio.vitalcareback.Dto.clinical;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosisDTO {
    private UUID id;
    private UUID consultationId;
    private String code;
    private String description;
}

