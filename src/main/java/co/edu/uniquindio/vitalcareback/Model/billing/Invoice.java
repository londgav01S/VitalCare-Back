package co.edu.uniquindio.vitalcareback.Model.billing;


import co.edu.uniquindio.vitalcareback.Model.clinical.Consultation;
import co.edu.uniquindio.vitalcareback.Model.common.BaseEntity;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @ManyToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation;

    @Column(nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status = InvoiceStatus.PENDING;
}

