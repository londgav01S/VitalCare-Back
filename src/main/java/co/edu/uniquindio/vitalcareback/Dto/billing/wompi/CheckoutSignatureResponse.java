package co.edu.uniquindio.vitalcareback.Dto.billing.wompi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutSignatureResponse {
    private String reference;
    private Long amountInCents;
    private String currency;
    private String signature;
    private String publicKey; // Para que el front pueda inicializar el checkout
}
