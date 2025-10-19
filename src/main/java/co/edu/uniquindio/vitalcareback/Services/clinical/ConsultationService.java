package co.edu.uniquindio.vitalcareback.Services.clinical;

import co.edu.uniquindio.vitalcareback.Dto.clinical.ConsultationDTO;
import co.edu.uniquindio.vitalcareback.Dto.clinical.VitalSignDTO;
import co.edu.uniquindio.vitalcareback.Model.clinical.Consultation;
import co.edu.uniquindio.vitalcareback.Model.clinical.VitalSign;
import co.edu.uniquindio.vitalcareback.Model.scheduling.Appointment;
import co.edu.uniquindio.vitalcareback.Model.profiles.DoctorProfile;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import co.edu.uniquindio.vitalcareback.Repositories.clinical.ConsultationRepository;
import co.edu.uniquindio.vitalcareback.Repositories.clinical.VitalSignRepository;
import co.edu.uniquindio.vitalcareback.Repositories.scheduling.AppointmentRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.DoctorProfileRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.uniquindio.vitalcareback.Repositories.clinical.DiagnosisRepository;
import co.edu.uniquindio.vitalcareback.Repositories.clinical.TreatmentRepository;
import co.edu.uniquindio.vitalcareback.Repositories.clinical.PrescriptionRepository;
import co.edu.uniquindio.vitalcareback.Repositories.clinical.PrescriptionDetailRepository;
import co.edu.uniquindio.vitalcareback.Repositories.clinical.MedicationRepository;
import co.edu.uniquindio.vitalcareback.Repositories.clinical.MedicalRecordRepository;
import co.edu.uniquindio.vitalcareback.Repositories.clinical.MedicalHistoryRepository;
import co.edu.uniquindio.vitalcareback.Model.clinical.Diagnosis;
import co.edu.uniquindio.vitalcareback.Model.clinical.Treatment;
import co.edu.uniquindio.vitalcareback.Model.clinical.Prescription;
import co.edu.uniquindio.vitalcareback.Model.clinical.PrescriptionDetail;
import co.edu.uniquindio.vitalcareback.Model.clinical.Medication;
import co.edu.uniquindio.vitalcareback.Model.clinical.MedicalHistory;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final VitalSignRepository vitalSignRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PatientProfileRepository patientProfileRepository;
        private final DiagnosisRepository diagnosisRepository;
        private final TreatmentRepository treatmentRepository;
        private final PrescriptionRepository prescriptionRepository;
        private final PrescriptionDetailRepository prescriptionDetailRepository;
        private final MedicationRepository medicationRepository;
        private final MedicalRecordRepository medicalRecordRepository;
        private final MedicalHistoryRepository medicalHistoryRepository;

    public ConsultationDTO createConsultation(ConsultationDTO dto) {
        // Validar existencia de appointment
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cita no encontrada"));

        DoctorProfile doctor = doctorProfileRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor no encontrado"));

        PatientProfile patient = patientProfileRepository.findById(appointment.getPatient().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paciente no encontrado"));

        Consultation c = new Consultation();
        c.setAppointment(appointment);
        c.setDoctor(doctor);
        c.setPatient(patient);
        c.setReason(dto.getReason());
        c.setNotes(dto.getNotes());

        consultationRepository.save(c);

        // Marcar cita como completada
        appointment.setStatus(co.edu.uniquindio.vitalcareback.Model.scheduling.AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        return ConsultationDTO.builder()
                .id(c.getId())
                .appointmentId(appointment.getId())
                .doctorId(doctor.getId())
                .reason(c.getReason())
                .notes(c.getNotes())
                .build();
    }

    public VitalSignDTO addOrUpdateVitalSign(UUID consultationId, VitalSignDTO dto) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Consulta no encontrada"));

        VitalSign vs = new VitalSign();
        vs.setConsultation(consultation);
        vs.setType(dto.getType());
        vs.setValue(dto.getValue());

        vitalSignRepository.save(vs);

        return VitalSignDTO.builder()
                .id(vs.getId())
                .consultationId(consultation.getId())
                .type(vs.getType())
                .value(vs.getValue())
                .unit(dto.getUnit())
                .build();
    }

    public String saveAttachment(UUID consultationId, MultipartFile file) {
        // Validaciones básicas
        if (file == null || file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Archivo vacío");
        if (file.getSize() > 10 * 1024 * 1024) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Archivo demasiado grande");

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("application/pdf") && !contentType.startsWith("image/"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato no permitido");
        }

        // Aquí sólo simulamos el guardado retornando un path/URL simulado
        String simulatedUrl = "/files/consultations/" + consultationId + "/" + file.getOriginalFilename();
        // En un sistema real guardarías en S3/FS y persistirías referencia.
        return simulatedUrl;
    }

    public void createReviewAlert(UUID consultationId, String dateStr) {
        // Aquí sólo validamos y simularíamos la creación de una alerta
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Consulta no encontrada"));

        // Parse simple y uso para evitar warning
        LocalDateTime date = LocalDateTime.parse(dateStr);

        // En un flujo real se crearía una entidad Alert y se notificaría: simulamos la notificación
        System.out.println("[DEBUG] Crear alerta para consulta=" + consultation.getId() + " fecha=" + date);
    }

        public ConsultationDTO getById(UUID id) {
                Consultation c = consultationRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta no encontrada"));

                return ConsultationDTO.builder()
                                .id(c.getId())
                                .appointmentId(c.getAppointment().getId())
                                .doctorId(c.getDoctor().getId())
                                .reason(c.getReason())
                                .notes(c.getNotes())
                                .build();
        }

        public ConsultationDTO getByAppointmentId(UUID appointmentId) {
                Appointment ap = appointmentRepository.findById(appointmentId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));

                List<Consultation> list = consultationRepository.findByPatient(ap.getPatient()).stream()
                                .filter(c -> c.getAppointment().getId().equals(appointmentId))
                                .collect(Collectors.toList());

                if (list.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta no encontrada para la cita");

                Consultation c = list.get(0);
                return ConsultationDTO.builder()
                                .id(c.getId())
                                .appointmentId(c.getAppointment().getId())
                                .doctorId(c.getDoctor().getId())
                                .reason(c.getReason())
                                .notes(c.getNotes())
                                .build();
        }

        public ConsultationDTO updateNotes(UUID consultationId, String notes) {
                Consultation c = consultationRepository.findById(consultationId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta no encontrada"));
                if (notes == null) notes = "";
                c.setNotes(notes);
                consultationRepository.save(c);
                return ConsultationDTO.builder().id(c.getId()).notes(c.getNotes()).build();
        }

        public ConsultationDTO completeConsultation(UUID consultationId) {
                Consultation c = consultationRepository.findById(consultationId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta no encontrada"));

                Appointment ap = c.getAppointment();
                ap.setStatus(co.edu.uniquindio.vitalcareback.Model.scheduling.AppointmentStatus.COMPLETED);
                appointmentRepository.save(ap);

                return getById(c.getId());
        }

        public List<VitalSignDTO> registerVitalSigns(UUID consultationId, java.util.Map<String, Object> body) {
                Consultation consultation = consultationRepository.findById(consultationId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta no encontrada"));

                var vitalSigns = (java.util.List<java.util.Map<String, Object>>) body.get("vitalSigns");
                if (vitalSigns == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay signos vitales en el body");

                List<VitalSignDTO> saved = vitalSigns.stream().map(map -> {
                        VitalSign vs = new VitalSign();
                        vs.setConsultation(consultation);
                        vs.setType((String) map.getOrDefault("type", "unknown"));
                        vs.setValue((String) map.getOrDefault("value", ""));
                        vitalSignRepository.save(vs);
                        return VitalSignDTO.builder().id(vs.getId()).consultationId(consultation.getId()).type(vs.getType()).value(vs.getValue()).unit((String) map.getOrDefault("unit", ""))
                                        .build();
                }).collect(Collectors.toList());

                return saved;
        }

        public ConsultationDTO registerSymptoms(UUID consultationId, java.util.Map<String, Object> body) {
                Consultation consultation = consultationRepository.findById(consultationId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta no encontrada"));

                var symptoms = (java.util.List<String>) body.get("symptoms");
                if (symptoms == null) symptoms = java.util.List.of();

                String notes = consultation.getNotes() == null ? "" : consultation.getNotes();
                notes += "\nSymptoms: " + String.join(", ", symptoms);
                consultation.setNotes(notes);
                consultationRepository.save(consultation);

                return getById(consultation.getId());
        }

        public ConsultationDTO createDiagnoses(UUID consultationId, java.util.Map<String, Object> body) {
                Consultation consultation = consultationRepository.findById(consultationId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta no encontrada"));

                var diagnoses = (java.util.List<java.util.Map<String, Object>>) body.get("diagnoses");
                if (diagnoses == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "diagnoses es requerido");

                for (var d : diagnoses) {
                        Diagnosis diag = new Diagnosis();
                        diag.setConsultation(consultation);
                        diag.setCode((String) d.getOrDefault("code", ""));
                        diag.setDescription((String) d.getOrDefault("description", ""));
                        diagnosisRepository.save(diag);
                }

                return getById(consultation.getId());
        }

        public ConsultationDTO createTreatments(UUID consultationId, java.util.Map<String, Object> body) {
                Consultation consultation = consultationRepository.findById(consultationId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta no encontrada"));

                var treatments = (java.util.List<java.util.Map<String, Object>>) body.get("treatments");
                if (treatments == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "treatments es requerido");

                for (var t : treatments) {
                        Treatment tr = new Treatment();
                        tr.setConsultation(consultation);
                        tr.setDescription((String) t.getOrDefault("description", ""));
                        treatmentRepository.save(tr);
                }

                return getById(consultation.getId());
        }

                @SuppressWarnings("unchecked")
                public ConsultationDTO createPrescription(UUID consultationId, java.util.Map<String, Object> body) {
                Consultation consultation = consultationRepository.findById(consultationId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta no encontrada"));

                        var details = (java.util.List<java.util.Map<String, Object>>) body.get("details");

                        Prescription prescription = new Prescription();
                        prescription.setConsultation(consultation);
                        prescriptionRepository.save(prescription);

                        if (details != null) {
                                for (var det : details) {
                                        String medName = (String) det.getOrDefault("medicationName", "");
                                        String dose = (String) det.getOrDefault("dose", "");
                                        String frequency = (String) det.getOrDefault("frequency", "");

                                        Medication medication = medicationRepository.findByName(medName).orElseGet(() -> {
                                                Medication m = new Medication();
                                                m.setName(medName);
                                                m.setUnit((String) det.getOrDefault("unit", ""));
                                                return medicationRepository.save(m);
                                        });

                                        PrescriptionDetail pd = new PrescriptionDetail();
                                        pd.setPrescription(prescription);
                                        pd.setMedication(medication);
                                        pd.setDosage(dose);
                                        pd.setFrequency(frequency);
                                        prescriptionDetailRepository.save(pd);
                                }
                        }

                        return getById(consultation.getId());
        }

                public List<Object> getMedications() {
                        return medicationRepository.findAll().stream().map(m -> java.util.Map.of(
                                        "id", m.getId(),
                                        "name", m.getName(),
                                        "unit", m.getUnit()
                        )).collect(Collectors.toList());
                }

        public Object getMedicalRecord(UUID patientId) {
                PatientProfile patient = patientProfileRepository.findById(patientId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));

                return medicalRecordRepository.findByPatient(patient).orElse(null);
        }

        public List<Object> getMedicalHistory(UUID patientId) {
                PatientProfile patient = patientProfileRepository.findById(patientId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));

                return medicalHistoryRepository.findByPatient(patient).stream().map(h -> java.util.Map.of(
                                "id", h.getId(),
                                "condition", h.getCondition(),
                                "notes", h.getNotes()
                )).collect(Collectors.toList());
        }

        public Object addMedicalHistory(UUID patientId, java.util.Map<String, Object> body) {
                PatientProfile patient = patientProfileRepository.findById(patientId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));

                MedicalHistory mh = new MedicalHistory();
                mh.setPatient(patient);
                mh.setCondition((String) body.getOrDefault("condition", "Sin especificar"));
                mh.setNotes((String) body.getOrDefault("notes", ""));
                medicalHistoryRepository.save(mh);
                return java.util.Map.of("id", mh.getId(), "condition", mh.getCondition(), "notes", mh.getNotes());
        }

        public List<ConsultationDTO> getConsultationsByPatient(UUID patientId) {
                PatientProfile patient = patientProfileRepository.findById(patientId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));

                return consultationRepository.findByPatient(patient).stream().map(c -> ConsultationDTO.builder()
                                .id(c.getId())
                                .appointmentId(c.getAppointment().getId())
                                .doctorId(c.getDoctor().getId())
                                .reason(c.getReason())
                                .notes(c.getNotes())
                                .build()).collect(Collectors.toList());
        }
}
