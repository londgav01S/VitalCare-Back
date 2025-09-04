package co.edu.uniquindio.vitalcareback.Dto.profiles;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffProfileDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String role;
    private String phone;
    private String email;
}

