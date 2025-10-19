package co.edu.uniquindio.vitalcareback.Model.profiles;


import co.edu.uniquindio.vitalcareback.Model.auth.User;
import co.edu.uniquindio.vitalcareback.Model.common.BaseEntity;
import co.edu.uniquindio.vitalcareback.Model.common.Gender;
import co.edu.uniquindio.vitalcareback.Model.location.City;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import co.edu.uniquindio.vitalcareback.Model.clinical.MedicalRecord;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfile extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    private String bloodType;

    private String email;

    private String phone;

    private String address;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private MedicalRecord medicalRecord;
}

