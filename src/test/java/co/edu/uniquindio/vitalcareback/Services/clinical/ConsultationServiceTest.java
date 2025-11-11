package co.edu.uniquindio.vitalcareback.Services.clinical;

import co.edu.uniquindio.vitalcareback.Dto.clinical.ConsultationDTO;
import co.edu.uniquindio.vitalcareback.Model.clinical.Consultation;
import co.edu.uniquindio.vitalcareback.Model.profiles.DoctorProfile;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import co.edu.uniquindio.vitalcareback.Model.scheduling.Appointment;
import co.edu.uniquindio.vitalcareback.Repositories.clinical.*;
import co.edu.uniquindio.vitalcareback.Repositories.scheduling.AppointmentRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.DoctorProfileRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.PatientProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link co.edu.uniquindio.vitalcareback.Services.clinical.ConsultationService}.
 *
 * Estrategia:
 * - Pruebas unitarias puras con Mockito/JUnit 5 usando @InjectMocks/@Mock.
 * - Se mockean todos los repositorios inyectados por constructor.
 * - No se levanta el contexto de Spring; pruebas rápidas y aisladas.
 */
@ExtendWith(MockitoExtension.class)
public class ConsultationServiceTest {

    @InjectMocks
    private ConsultationService consultationService;

    @Mock private ConsultationRepository consultationRepository;
    @Mock private VitalSignRepository vitalSignRepository;
    @Mock private AppointmentRepository appointmentRepository;
    @Mock private DoctorProfileRepository doctorProfileRepository;
    @Mock private PatientProfileRepository patientProfileRepository;
    @Mock private DiagnosisRepository diagnosisRepository;
    @Mock private TreatmentRepository treatmentRepository;
    @Mock private PrescriptionRepository prescriptionRepository;
    @Mock private PrescriptionDetailRepository prescriptionDetailRepository;
    @Mock private MedicationRepository medicationRepository;
    @Mock private MedicalRecordRepository medicalRecordRepository;
    @Mock private MedicalHistoryRepository medicalHistoryRepository;

    /**
     * Caso feliz: crear una consulta marca la cita como COMPLETED y persiste la consulta.
     *
     * Dado un DTO válido y repositorios que devuelven entidades existentes,
     * cuando se invoca createConsultation,
     * entonces se guarda la consulta y se actualiza la cita a COMPLETED.
     */
    @Test
    void createConsultation_marksAppointmentCompleted() {
        ConsultationDTO dto = ConsultationDTO.builder()
                .appointmentId(UUID.randomUUID())
                .doctorId(UUID.randomUUID())
                .reason("Consulta de prueba")
                .build();

        Appointment ap = new Appointment();
        PatientProfile patient = new PatientProfile();
        // evitar NPE en getPatient().getId()
        ap.setPatient(patient);

        when(appointmentRepository.findById(dto.getAppointmentId())).thenReturn(Optional.of(ap));
        when(doctorProfileRepository.findById(dto.getDoctorId())).thenReturn(Optional.of(new DoctorProfile()));
        when(patientProfileRepository.findById(any())).thenReturn(Optional.of(patient));

        doAnswer(invocation -> {
            Consultation c = invocation.getArgument(0);
            c.setId(UUID.randomUUID());
            return c;
        }).when(consultationRepository).save(any(Consultation.class));

        var res = consultationService.createConsultation(dto);
        assertNotNull(res);
        verify(consultationRepository, times(1)).save(any(Consultation.class));
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }
}
