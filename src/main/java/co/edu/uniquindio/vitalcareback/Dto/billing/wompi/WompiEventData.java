package co.edu.uniquindio.vitalcareback.Dto.billing.wompi;

import lombok.Data;

@Data
public class WompiEventData {
    private String transactionId;
    private String status; // APPROVED, DECLINED, etc.
    private String reference;
    private Long amountInCents;
    private String currency;
}
