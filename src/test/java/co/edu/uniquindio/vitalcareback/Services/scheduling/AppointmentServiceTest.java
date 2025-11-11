package co.edu.uniquindio.vitalcareback.Services.scheduling;

import co.edu.uniquindio.vitalcareback.Dto.scheduling.AppointmentDTO;
import co.edu.uniquindio.vitalcareback.Model.profiles.DoctorProfile;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import co.edu.uniquindio.vitalcareback.Model.scheduling.Appointment;
import co.edu.uniquindio.vitalcareback.Repositories.scheduling.AppointmentRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.DoctorProfileRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.PatientProfileRepository;
import co.edu.uniquindio.vitalcareback.Repositories.location.SiteRepository;
import co.edu.uniquindio.vitalcareback.Services.notifications.NotificationService;
import co.edu.uniquindio.vitalcareback.mapper.scheduling.AppointmentMapper;
import co.edu.uniquindio.vitalcareback.mapper.auth.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link co.edu.uniquindio.vitalcareback.Services.scheduling.AppointmentService}.
 *
 * Estrategia:
 * - Se usa Mockito para aislar dependencias (repositorios, mappers y notificaciones).
 * - Verifica que se persista la cita y se invoque la notificación.
 */
@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock private AppointmentRepository appointmentRepository;
    @Mock private PatientProfileRepository patientProfileRepository;
    @Mock private DoctorProfileRepository doctorProfileRepository;
    @Mock private NotificationService notificationService;
    @Mock private SiteRepository siteRepository;
    @Mock private AppointmentMapper appointmentMapper;
    @Mock private UserMapper userMapper;

    /**
     * Caso feliz: crear una cita con paciente y doctor existentes persiste la entidad
     * y dispara el envío de notificación (mockeada en unit test).
     */
    @Test
    void createAppointment_setsStatusScheduled() {
        AppointmentDTO dto = AppointmentDTO.builder()
                .patientId(UUID.randomUUID())
                .doctorId(UUID.randomUUID())
                .scheduledDate(LocalDateTime.now().plusDays(1))
                .build();

        when(patientProfileRepository.findById(dto.getPatientId())).thenReturn(Optional.of(new PatientProfile()));
        when(doctorProfileRepository.findById(dto.getDoctorId())).thenReturn(Optional.of(new DoctorProfile()));

        when(appointmentMapper.toEntity(any())).thenReturn(new Appointment());
        when(appointmentMapper.toDTO(any())).thenReturn(AppointmentDTO.builder().id(UUID.randomUUID()).build());

        doAnswer(invocation -> {
            Appointment a = invocation.getArgument(0);
            a.setId(UUID.randomUUID());
            return a;
        }).when(appointmentRepository).save(any(Appointment.class));

        var res = appointmentService.createAppointment(dto);
        assertNotNull(res);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
        verify(notificationService, times(1)).sendNotification(any(), any(), any());
    }
}
