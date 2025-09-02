package co.edu.uniquindio.vitalcareback.Model.clinical;


import co.edu.uniquindio.vitalcareback.Model.common.BaseEntity;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    private String notes;
}

