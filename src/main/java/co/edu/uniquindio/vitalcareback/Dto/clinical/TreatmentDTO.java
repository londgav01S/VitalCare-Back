package co.edu.uniquindio.vitalcareback.Dto.clinical;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentDTO {
    private UUID id;
    private UUID consultationId;
    private String description;
}
