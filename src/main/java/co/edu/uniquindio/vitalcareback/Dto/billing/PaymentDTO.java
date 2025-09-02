package co.edu.uniquindio.vitalcareback.Dto.billing;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private UUID id;
    private UUID invoiceId;
    private BigDecimal amount;
    private String method;
    private LocalDateTime paymentDate;
}

