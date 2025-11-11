package co.edu.uniquindio.vitalcareback.Model.billing;

import co.edu.uniquindio.vitalcareback.Model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    private BigDecimal amount;

    private LocalDateTime paymentDate;

    // Wompi fields
    private String reference; // Referencia única enviada a Wompi

    private String transactionId; // ID de transacción de Wompi

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING; // Estado proveniente de Wompi
}

