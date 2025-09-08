package co.edu.uniquindio.vitalcareback.Controller;

import co.edu.uniquindio.vitalcareback.Dto.scheduling.AppointmentDTO;
import co.edu.uniquindio.vitalcareback.Services.scheduling.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.createAppointment(appointmentDTO));
    }

    @PostMapping("/create-by-email")
    public ResponseEntity<AppointmentDTO> createAppointmentByEmail(@RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO createdAppointment = appointmentService.createAppointmentByEmail(appointmentDTO);
        return ResponseEntity.ok(createdAppointment);
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentDTO> rescheduleAppointment(@PathVariable UUID id,
                                                                @RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(id, appointmentDTO));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable UUID id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctor(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    @PutMapping("/{id}/confirm-attendance")
    public ResponseEntity<Void> confirmAttendance(@PathVariable UUID id) {
        appointmentService.confirmAttendance(id);
        return ResponseEntity.noContent().build();
    }
}

