package co.edu.uniquindio.vitalcareback.Controller;

import co.edu.uniquindio.vitalcareback.Dto.scheduling.AppointmentDTO;
import co.edu.uniquindio.vitalcareback.Services.scheduling.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * AppointmentController
 *
 * Controlador REST para manejar operaciones relacionadas con citas médicas (appointments).
 * Proporciona endpoints para crear, reprogramar, cancelar y consultar citas,
 * así como confirmar la asistencia del paciente.
 */
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    /**
     * Servicio que contiene la lógica de negocio de las citas.
     */
    private final AppointmentService appointmentService;

    /**
     * Crear una nueva cita.
     *
     * @param appointmentDTO DTO con la información de la cita
     * @return ResponseEntity con la cita creada
     */
    @PostMapping("/create")
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.createAppointment(appointmentDTO));
    }

    /**
     * Crear una nueva cita usando el email del paciente/doctor.
     *
     * @param appointmentDTO DTO con la información de la cita
     * @return ResponseEntity con la cita creada
     */
    @PostMapping("/create-by-email")
    public ResponseEntity<AppointmentDTO> createAppointmentByEmail(@RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO createdAppointment = appointmentService.createAppointmentByEmail(appointmentDTO);
        return ResponseEntity.ok(createdAppointment);
    }

    /**
     * Reprogramar una cita existente.
     *
     * @param id ID de la cita a reprogramar
     * @param appointmentDTO DTO con la nueva información de la cita
     * @return ResponseEntity con la cita actualizada
     */
    @PutMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentDTO> rescheduleAppointment(@PathVariable UUID id,
                                                                @RequestBody AppointmentDTO appointmentDTO) {
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(id, appointmentDTO));
    }

    /**
     * Cancelar una cita existente.
     *
     * @param id ID de la cita a cancelar
     * @return ResponseEntity sin contenido
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable UUID id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener todas las citas de un paciente por su ID.
     *
     * @param patientId ID del paciente
     * @return ResponseEntity con la lista de citas del paciente
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    /**
     * Obtener todas las citas de un doctor por su ID.
     *
     * @param doctorId ID del doctor
     * @return ResponseEntity con la lista de citas del doctor
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctor(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    /**
     * Confirmar la asistencia de un paciente a una cita.
     *
     * @param id ID de la cita
     * @return ResponseEntity sin contenido
     */
    @PutMapping("/{id}/confirm-attendance")
    public ResponseEntity<Void> confirmAttendance(@PathVariable UUID id) {
        appointmentService.confirmAttendance(id);
        return ResponseEntity.noContent().build();
    }
}
