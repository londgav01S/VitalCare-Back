package co.edu.uniquindio.vitalcareback.Services.clinical;

import co.edu.uniquindio.vitalcareback.Dto.clinical.ConsultationDTO;
import co.edu.uniquindio.vitalcareback.Model.clinical.Consultation;
import co.edu.uniquindio.vitalcareback.Model.profiles.DoctorProfile;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import co.edu.uniquindio.vitalcareback.Model.scheduling.Appointment;
import co.edu.uniquindio.vitalcareback.Repositories.clinical.ConsultationRepository;
import co.edu.uniquindio.vitalcareback.Repositories.scheduling.AppointmentRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.DoctorProfileRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.PatientProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class ConsultationServiceTest {

    @Autowired
    private ConsultationService consultationService;

    @MockBean
    private ConsultationRepository consultationRepository;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private DoctorProfileRepository doctorProfileRepository;

    @MockBean
    private PatientProfileRepository patientProfileRepository;

    @Test
    void createConsultation_marksAppointmentCompleted() {
        ConsultationDTO dto = ConsultationDTO.builder()
                .appointmentId(UUID.randomUUID())
                .doctorId(UUID.randomUUID())
                .reason("Consulta de prueba")
                .build();

        Appointment ap = new Appointment();
        PatientProfile patient = new PatientProfile();
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
