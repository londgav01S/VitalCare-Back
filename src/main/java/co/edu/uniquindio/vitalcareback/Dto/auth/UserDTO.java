package co.edu.uniquindio.vitalcareback.Dto.auth;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private UUID id;
    private String name;
    private String idNumber;
    private String username;
    private String password;
    private String email;
    private Boolean enabled;
    private String role; // Nombre del rol

    public UserDTO(UUID id, String email) {
        this.id = id;
        this.email = email;
    }
}

