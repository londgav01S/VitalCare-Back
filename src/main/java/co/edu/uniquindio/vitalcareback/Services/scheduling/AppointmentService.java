package co.edu.uniquindio.vitalcareback.Services.scheduling;

import co.edu.uniquindio.vitalcareback.Dto.scheduling.AppointmentDTO;
import co.edu.uniquindio.vitalcareback.Model.profiles.DoctorProfile;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import co.edu.uniquindio.vitalcareback.Model.scheduling.Appointment;
import co.edu.uniquindio.vitalcareback.Model.scheduling.AppointmentStatus;
import co.edu.uniquindio.vitalcareback.Repositories.location.SiteRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.DoctorProfileRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.PatientProfileRepository;
import co.edu.uniquindio.vitalcareback.Repositories.scheduling.AppointmentRepository;
import co.edu.uniquindio.vitalcareback.Services.notifications.NotificationService;
import co.edu.uniquindio.vitalcareback.mapper.scheduling.AppointmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * AppointmentService
 *
 * Servicio encargado de la gestión de citas médicas dentro de VitalCare.
 * Permite crear, reprogramar, cancelar, listar y confirmar asistencia de citas.
 */
@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final AppointmentMapper appointmentMapper;
    private final NotificationService notificationService;
    private final SiteRepository siteRepository;

    /**
     * Crear una cita para un paciente y doctor existentes.
     *
     * @param appointmentDTO DTO con información de la cita
     * @return AppointmentDTO Cita creada
     */
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        var patient = patientProfileRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        var doctor = doctorProfileRepository.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        appointmentRepository.save(appointment);

        // Enviar notificación al paciente
        notificationService.sendNotification(
                patient.getEmail(),
                "Cita médica programada",
                "Su cita con el Dr. " + doctor.getLastName() +
                        " ha sido programada para el " + appointment.getScheduledDate()
        );

        return appointmentMapper.toDTO(appointment);
    }

    /**
     * Reprogramar una cita existente.
     *
     * @param id             ID de la cita
     * @param appointmentDTO DTO con nueva fecha
     * @return AppointmentDTO Cita actualizada
     */
    public AppointmentDTO rescheduleAppointment(UUID id, AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        appointment.setScheduledDate(appointmentDTO.getScheduledDate());
        appointment.setStatus(AppointmentStatus.RESCHEDULED);
        appointmentRepository.save(appointment);

        notificationService.sendNotification(
                appointment.getPatient().getEmail(),
                "Cita médica reprogramada",
                "Su cita ha sido reprogramada para el " + appointment.getScheduledDate()
        );

        return appointmentMapper.toDTO(appointment);
    }

    /**
     * Cancelar una cita.
     *
     * @param id ID de la cita
     */
    public void cancelAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        notificationService.sendNotification(
                appointment.getPatient().getEmail(),
                "Cita cancelada",
                "Su cita programada para el " + appointment.getScheduledDate() + " ha sido cancelada."
        );
    }

    /**
     * Listar todas las citas de un paciente.
     *
     * @param patientId UUID del paciente
     * @return Lista de AppointmentDTO
     */
    public List<AppointmentDTO> getAppointmentsByPatient(UUID patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Listar todas las citas de un doctor.
     *
     * @param doctorId UUID del doctor
     * @return Lista de AppointmentDTO
     */
    public List<AppointmentDTO> getAppointmentsByDoctor(UUID doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Confirmar asistencia de un paciente a la cita.
     *
     * @param id ID de la cita
     */
    public void confirmAttendance(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }

    /**
     * Crear cita usando email del paciente (sin ID).
     *
     * @param appointmentDTO DTO con información de la cita
     * @return AppointmentDTO Cita creada
     */
    public AppointmentDTO createAppointmentByEmail(AppointmentDTO appointmentDTO) {
        // Buscar paciente por email
        PatientProfile patientProfile = patientProfileRepository.findByEmail(appointmentDTO.getPatientEmail())
                .orElseThrow(() -> new RuntimeException(
                        "Paciente no encontrado con email: " + appointmentDTO.getPatientEmail()));

        // Buscar doctor por UUID
        DoctorProfile doctor = doctorProfileRepository.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException(
                        "Doctor no encontrado con id: " + appointmentDTO.getDoctorId()));

        // Crear la cita
        Appointment appointment = Appointment.builder()
                .patient(patientProfile)
                .doctor(doctor)
                .site(appointmentDTO.getSiteId() != null ?
                        siteRepository.findById(appointmentDTO.getSiteId()).orElse(null)
                        : null)
                .scheduledDate(appointmentDTO.getScheduledDate())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Convertir a DTO
        return AppointmentDTO.builder()
                .id(savedAppointment.getId())
                .patientId(savedAppointment.getPatient().getId())
                .doctorId(savedAppointment.getDoctor().getId())
                .siteId(savedAppointment.getSite() != null ? savedAppointment.getSite().getId() : null)
                .scheduledDate(savedAppointment.getScheduledDate())
                .status(savedAppointment.getStatus().name())
                .patientEmail(savedAppointment.getPatient().getUser().getEmail())
                .build();
    }
}
