package co.edu.uniquindio.vitalcareback.Dto.profiles;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorProfileDTO {
    private UUID id;
    private String name;
    private String idNumber;
    private String lastName;
    private String specialty;
    private String licenseNumber;
    private String phone;
    private String email;

    public DoctorProfileDTO(UUID id, String lastName, String licenseNumber) {
        this.id = id;
        this.lastName = lastName;
        this.licenseNumber = licenseNumber;
    }

    public DoctorProfileDTO(UUID id,
                            String name,
                            String idNumber,
                            String lastName,
                            String specialty,
                            String licenseNumber,
                            String email) {
        this.id = id;
        this.name = name;
        this.idNumber = idNumber;
        this.lastName = lastName;
        this.specialty = specialty;
        this.licenseNumber = licenseNumber;
        this.email = email;
    }
}

