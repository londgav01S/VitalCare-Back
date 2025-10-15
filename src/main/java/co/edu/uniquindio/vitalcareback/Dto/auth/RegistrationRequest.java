package co.edu.uniquindio.vitalcareback.Dto.auth;

import co.edu.uniquindio.vitalcareback.Model.common.Gender;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class RegistrationRequest {

    // User
    private String email;
    private String password;
    private String name;
    private String idNumber;

    // Patient
    private Gender gender;
    private LocalDate birthDate;
    private String bloodType;
    private String phone;
    private String address;
    private UUID cityId;

    // Doctor
    private String licenseNumber;
    private String specialty;
    private String lastName;

    // Staff
    private String department;
    private String position;
}

