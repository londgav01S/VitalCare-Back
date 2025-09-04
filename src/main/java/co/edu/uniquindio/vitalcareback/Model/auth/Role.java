package co.edu.uniquindio.vitalcareback.Model.auth;


import co.edu.uniquindio.vitalcareback.Model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name; // ADMIN_GENERAL, ADMINISTRATIVO, MEDICO, PACIENTE
}

