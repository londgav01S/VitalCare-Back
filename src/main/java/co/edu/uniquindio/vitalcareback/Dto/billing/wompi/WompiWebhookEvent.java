package co.edu.uniquindio.vitalcareback.Dto.billing.wompi;

import lombok.Data;

@Data
public class WompiWebhookEvent {
    private String event; // e.g., transaction.updated
    private Payload payload;

    @Data
    public static class Payload {
        private Transaction transaction;
    }

    @Data
    public static class Transaction {
        private String id;
        private Long amountInCents;
        private String reference;
        private String currency;
        private String status; // APPROVED, DECLINED, VOIDED, ERROR, PENDING
    }
}
