package co.edu.uniquindio.vitalcareback.Model.clinical;


import co.edu.uniquindio.vitalcareback.Model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medication extends BaseEntity {

    private String name;

    private String unit; // Ej: mg, ml
}

