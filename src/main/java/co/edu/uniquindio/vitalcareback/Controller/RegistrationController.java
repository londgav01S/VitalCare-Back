package co.edu.uniquindio.vitalcareback.Controller;

import co.edu.uniquindio.vitalcareback.Dto.auth.RegistrationRequest;
import co.edu.uniquindio.vitalcareback.Dto.auth.UserDTO;
import co.edu.uniquindio.vitalcareback.Services.auth.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RegistrationController
 *
 * Controlador REST encargado de manejar el registro de nuevos usuarios en el sistema VitalCare.
 * Proporciona endpoints para registrar pacientes, doctores y personal administrativo (staff).
 */
@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegistrationController {

    /**
     * Servicio que contiene la lógica de negocio para el registro de usuarios.
     */
    private final RegistrationService registrationService;

    /**
     * Endpoint para registrar un nuevo paciente.
     *
     * @param request DTO con la información necesaria para registrar un paciente
     * @return ResponseEntity con el usuario registrado (UserDTO)
     */
    @PostMapping("/patient")
    public ResponseEntity<UserDTO> registerPatient(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(registrationService.registerPatient(request));
    }

    /**
     * Endpoint para registrar un nuevo doctor.
     *
     * @param request DTO con la información necesaria para registrar un doctor
     * @return ResponseEntity con el usuario registrado (UserDTO)
     */
    @PostMapping("/doctor")
    public ResponseEntity<UserDTO> registerDoctor(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(registrationService.registerDoctor(request));
    }

    /**
     * Endpoint para registrar un nuevo miembro del staff.
     *
     * @param request DTO con la información necesaria para registrar personal administrativo
     * @return ResponseEntity con el usuario registrado (UserDTO)
     */
    @PostMapping("/staff")
    public ResponseEntity<UserDTO> registerStaff(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(registrationService.registerStaff(request));
    }
}
