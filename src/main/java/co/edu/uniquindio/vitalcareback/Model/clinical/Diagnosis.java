package co.edu.uniquindio.vitalcareback.Model.clinical;


import co.edu.uniquindio.vitalcareback.Model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "diagnoses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Diagnosis extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    private String code;
    private String description;
}

