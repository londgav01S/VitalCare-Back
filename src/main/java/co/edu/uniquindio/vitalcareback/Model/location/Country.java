package co.edu.uniquindio.vitalcareback.Model.location;


import co.edu.uniquindio.vitalcareback.Model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;
}
