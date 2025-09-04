package co.edu.uniquindio.vitalcareback.Dto.billing;


import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDTO {
    private UUID id;
    private UUID patientId;
    private UUID consultationId;
    private BigDecimal total;
    private String status;
}
