package co.edu.uniquindio.vitalcareback.Controller;

import co.edu.uniquindio.vitalcareback.Dto.clinical.ConsultationDTO;
import co.edu.uniquindio.vitalcareback.Dto.scheduling.AppointmentDTO;
import co.edu.uniquindio.vitalcareback.Services.clinical.ConsultationService;
import co.edu.uniquindio.vitalcareback.Services.scheduling.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/clinical")
@RequiredArgsConstructor
@Slf4j
public class ConsultationController {

    private final ConsultationService consultationService;
    private final AppointmentService appointmentService;

    // RF09 - Registrar atención médica (solo médicos autenticados)
    @PostMapping("/consultations")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> createConsultation(@RequestBody ConsultationDTO dto) {
        var created = consultationService.createConsultation(dto);
        return ResponseEntity.ok(created);
    }

    // Obtener consulta por id
    @GetMapping("/consultations/{id}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('NURSE') or hasRole('ADMIN')")
    public ResponseEntity<?> getConsultationById(@PathVariable UUID id) {
        var c = consultationService.getById(id);
        return ResponseEntity.ok(c);
    }

    // Obtener consulta por id de cita
    @GetMapping("/consultations/appointment/{appointmentId}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('NURSE')")
    public ResponseEntity<?> getConsultationByAppointment(@PathVariable UUID appointmentId) {
        var c = consultationService.getByAppointmentId(appointmentId);
        return ResponseEntity.ok(c);
    }

    // Actualizar notas generales
    @PutMapping("/consultations/{consultationId}/notes")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> updateNotes(@PathVariable UUID consultationId, @RequestBody Map<String, String> body) {
        var updated = consultationService.updateNotes(consultationId, body.get("notes"));
        return ResponseEntity.ok(updated);
    }

    // Completar consulta
    @PutMapping("/consultations/{consultationId}/complete")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> completeConsultation(@PathVariable UUID consultationId) {
        var updated = consultationService.completeConsultation(consultationId);
        return ResponseEntity.ok(updated);
    }

    // RF10 - Registrar/actualizar signos vitales (solo personal médico)
    @PostMapping("/consultations/{consultationId}/vital-signs")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('NURSE')")
    public ResponseEntity<?> addVitalSign(@PathVariable UUID consultationId, @RequestBody Map<String, Object> body) {
        // front envia { vitalSigns: [{ type, value, unit }, ...] }
        var list = consultationService.registerVitalSigns(consultationId, body);
        return ResponseEntity.ok(list);
    }

    // Registrar síntomas
    @PostMapping("/consultations/{consultationId}/symptoms")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('NURSE')")
    public ResponseEntity<?> registerSymptoms(@PathVariable UUID consultationId, @RequestBody Map<String, Object> body) {
        var updated = consultationService.registerSymptoms(consultationId, body);
        return ResponseEntity.ok(updated);
    }

    // Crear diagnósticos
    @PostMapping("/consultations/{consultationId}/diagnoses")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> createDiagnosis(@PathVariable UUID consultationId, @RequestBody Map<String, Object> body) {
        var updated = consultationService.createDiagnoses(consultationId, body);
        return ResponseEntity.ok(updated);
    }

    // Crear tratamientos
    @PostMapping("/consultations/{consultationId}/treatments")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> createTreatment(@PathVariable UUID consultationId, @RequestBody Map<String, Object> body) {
        var updated = consultationService.createTreatments(consultationId, body);
        return ResponseEntity.ok(updated);
    }

    // Crear prescripción
    @PostMapping("/consultations/{consultationId}/prescriptions")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> createPrescription(@PathVariable UUID consultationId, @RequestBody Map<String, Object> body) {
        var updated = consultationService.createPrescription(consultationId, body);
        return ResponseEntity.ok(updated);
    }

    // Obtener catálogo de medicamentos
    @GetMapping("/medications")
    public ResponseEntity<?> getMedications() {
        var meds = consultationService.getMedications();
        return ResponseEntity.ok(meds);
    }

    // Expediente médico
    @GetMapping("/patients/{patientId}/medical-record")
    public ResponseEntity<?> getMedicalRecord(@PathVariable UUID patientId) {
        var mr = consultationService.getMedicalRecord(patientId);
        return ResponseEntity.ok(mr);
    }

    @GetMapping("/patients/{patientId}/medical-history")
    public ResponseEntity<?> getMedicalHistory(@PathVariable UUID patientId) {
        var list = consultationService.getMedicalHistory(patientId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/patients/{patientId}/medical-history")
    public ResponseEntity<?> addMedicalHistory(@PathVariable UUID patientId, @RequestBody Map<String, Object> body) {
        var created = consultationService.addMedicalHistory(patientId, body);
        return ResponseEntity.ok(created);
    }

    // Obtener todas las consultas de un paciente
    @GetMapping("/patients/{patientId}/consultations")
    public ResponseEntity<?> getPatientConsultations(@PathVariable UUID patientId) {
        var list = consultationService.getConsultationsByPatient(patientId);
        return ResponseEntity.ok(list);
    }

    // RF11 - Adjuntar resultados de exámenes (multipart/form-data)
    @PostMapping("/consultations/{consultationId}/attachments")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> uploadAttachment(@PathVariable UUID consultationId, @RequestParam("file") MultipartFile file) {
        var result = consultationService.saveAttachment(consultationId, file);
        return ResponseEntity.ok(Map.of("message", "Adjunto guardado", "url", result));
    }

    // RF12 - Generar alertas de revisiones médicas
    @PostMapping("/consultations/{consultationId}/alerts")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> createAlert(@PathVariable UUID consultationId, @RequestBody Map<String, String> payload) {
        consultationService.createReviewAlert(consultationId, payload.get("date"));
        return ResponseEntity.ok(Map.of("message", "Alerta creada"));
    }

    // Mostrar todas las citas a atender en rol doctor
    @GetMapping("/doctor/{doctorId}/appointments")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsForDoctor(@PathVariable UUID doctorId) {
        var list = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(list);
    }

}
