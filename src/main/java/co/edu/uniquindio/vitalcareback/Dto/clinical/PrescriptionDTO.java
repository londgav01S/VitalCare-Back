package co.edu.uniquindio.vitalcareback.Dto.clinical;

import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDTO {
    private UUID id;
    private UUID consultationId;
    private LocalDate date;
}

