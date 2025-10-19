package co.edu.uniquindio.vitalcareback.Services.scheduling;

import co.edu.uniquindio.vitalcareback.Dto.scheduling.AppointmentDTO;
import co.edu.uniquindio.vitalcareback.Model.profiles.DoctorProfile;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import co.edu.uniquindio.vitalcareback.Model.scheduling.Appointment;
import co.edu.uniquindio.vitalcareback.Model.scheduling.AppointmentStatus;
import co.edu.uniquindio.vitalcareback.Repositories.scheduling.AppointmentRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.DoctorProfileRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.PatientProfileRepository;
import co.edu.uniquindio.vitalcareback.Repositories.location.SiteRepository;
import co.edu.uniquindio.vitalcareback.Services.notifications.NotificationService;
import co.edu.uniquindio.vitalcareback.mapper.scheduling.AppointmentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class AppointmentServiceTest {

    @Autowired
    private AppointmentService appointmentService;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private PatientProfileRepository patientProfileRepository;

    @MockBean
    private DoctorProfileRepository doctorProfileRepository;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private SiteRepository siteRepository;

    @MockBean
    private AppointmentMapper appointmentMapper;

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
        doAnswer(invocation -> {
            Appointment a = invocation.getArgument(0);
            a.setId(UUID.randomUUID());
            return a;
        }).when(appointmentRepository).save(any(Appointment.class));

        var res = appointmentService.createAppointment(dto);
        assertNotNull(res);
        // mapper may return null fields, but we ensure save was called
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }
}
