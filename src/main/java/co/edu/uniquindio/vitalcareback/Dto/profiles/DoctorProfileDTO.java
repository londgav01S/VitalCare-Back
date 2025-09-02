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
    private String firstName;
    private String lastName;
    private String specialty;
    private String licenseNumber;
    private String phone;
    private String email;

}

