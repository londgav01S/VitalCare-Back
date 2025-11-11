package co.edu.uniquindio.vitalcareback.Dto.billing.wompi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentSessionRequest {
    @NotNull
    private UUID invoiceId;

    @NotBlank
    private String currency; // e.g. COP
}
