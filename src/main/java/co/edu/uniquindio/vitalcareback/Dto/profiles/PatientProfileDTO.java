package co.edu.uniquindio.vitalcareback.Dto.profiles;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProfileDTO {
    private UUID id;
    private String name;
    private String idNumber;
    private String documentNumber;
    private String phone;
    private String email;
    private String address;
    private String gender;
}

