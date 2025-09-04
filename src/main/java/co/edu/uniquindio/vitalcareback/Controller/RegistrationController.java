package co.edu.uniquindio.vitalcareback.Controller;


import co.edu.uniquindio.vitalcareback.Dto.auth.RegistrationRequest;
import co.edu.uniquindio.vitalcareback.Dto.auth.UserDTO;
import co.edu.uniquindio.vitalcareback.Services.auth.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/patient")
    public ResponseEntity<UserDTO> registerPatient(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(registrationService.registerPatient(request));
    }

    @PostMapping("/doctor")
    public ResponseEntity<UserDTO> registerDoctor(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(registrationService.registerDoctor(request));
    }

    @PostMapping("/staff")
    public ResponseEntity<UserDTO> registerStaff(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(registrationService.registerStaff(request));
    }
}

