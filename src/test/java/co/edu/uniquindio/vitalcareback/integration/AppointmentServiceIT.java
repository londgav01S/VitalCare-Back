package co.edu.uniquindio.vitalcareback.integration;

import co.edu.uniquindio.vitalcareback.Dto.scheduling.AppointmentDTO;
import co.edu.uniquindio.vitalcareback.Model.auth.User;
import co.edu.uniquindio.vitalcareback.Model.profiles.DoctorProfile;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import co.edu.uniquindio.vitalcareback.Repositories.auth.UserRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.DoctorProfileRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.PatientProfileRepository;
import co.edu.uniquindio.vitalcareback.Services.notifications.EmailService;
import co.edu.uniquindio.vitalcareback.Services.notifications.NotificationService;
import co.edu.uniquindio.vitalcareback.Services.scheduling.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for AppointmentService using Spring Boot context and H2.
 *
 * Estrategia:
 * - BD H2 en memoria y repositorios JPA reales.
 * - EmailService/NotificationService se reemplazan por @Primary no-op para no enviar nada.
 * - Se siembran User/PatientProfile/DoctorProfile mínimos para poder crear la cita.
 */
@SpringBootTest
@Transactional
class AppointmentServiceIT {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        EmailService emailService() {
            return new EmailService() {
                @Override
                public void sendEmail(String to, String subject, String body) {
                    // no-op in tests
                }
            };
        }

        @Bean
        @Primary
        NotificationService notificationService() {
            return new NotificationService(null, null, emailService(), null) {
                @Override
                public void sendNotification(String recipientEmail, String title, String message) {
                    // no-op in tests
                }
            };
        }
    }

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientProfileRepository patientProfileRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private UserRepository userRepository;

    private PatientProfile patient;
    private DoctorProfile doctor;

    @BeforeEach
    void seedProfiles() {
        User u1 = new User();
        u1.setEmail("p1@example.com");
        u1.setName("Patient One");
        u1.setEnabled(true);
        u1.setState(true);
        userRepository.save(u1);

        patient = new PatientProfile();
        patient.setUser(u1);
        patient.setEmail("p1@example.com");
        patient = patientProfileRepository.save(patient);

        User u2 = new User();
        u2.setEmail("d1@example.com");
        u2.setName("Doctor One");
        u2.setEnabled(true);
        u2.setState(true);
        userRepository.save(u2);

        doctor = new DoctorProfile();
        doctor.setUser(u2);
        doctor.setLastName("House");
        doctor = doctorProfileRepository.save(doctor);
    }

    /**
     * Caso feliz de integración: crear cita con paciente/doctor existentes
     * persiste y retorna un AppointmentDTO con id.
     */
    @Test
    void createAppointment_withExistingProfiles_succeeds() {
        AppointmentDTO dto = AppointmentDTO.builder()
                .patientId(patient.getId())
                .doctorId(doctor.getId())
                .scheduledDate(LocalDateTime.now().plusDays(1))
                .build();

        AppointmentDTO saved = appointmentService.createAppointment(dto);

        assertNotNull(saved);
        assertNotNull(saved.getId());
    }
}
