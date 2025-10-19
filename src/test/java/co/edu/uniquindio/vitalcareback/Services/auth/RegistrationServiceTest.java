package co.edu.uniquindio.vitalcareback.Services.auth;

import co.edu.uniquindio.vitalcareback.Dto.auth.RegistrationRequest;
import co.edu.uniquindio.vitalcareback.Model.auth.Role;
import co.edu.uniquindio.vitalcareback.Model.location.City;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import co.edu.uniquindio.vitalcareback.Model.auth.User;
import co.edu.uniquindio.vitalcareback.Repositories.auth.UserRepository;
import co.edu.uniquindio.vitalcareback.Repositories.auth.RoleRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.PatientProfileRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.DoctorProfileRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.StaffProfileRepository;
import co.edu.uniquindio.vitalcareback.Repositories.location.CityRepository;
import co.edu.uniquindio.vitalcareback.Services.notifications.EmailService;
import co.edu.uniquindio.vitalcareback.Model.common.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
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
public class RegistrationServiceTest {

    @Autowired
    private RegistrationService registrationService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PatientProfileRepository patientRepo;

    @MockBean
    private DoctorProfileRepository doctorRepo;

    @MockBean
    private StaffProfileRepository staffRepo;

    @MockBean
    private CityRepository cityRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailService emailService;

    @Test
    void registerPatient_createsUserAndMedicalRecord() {
        RegistrationRequest req = new RegistrationRequest();
        req.setEmail("test@example.com");
        req.setPassword("Password1");
        req.setName("Test User");
        req.setIdNumber("12345");
        req.setCityId(UUID.randomUUID());
        req.setGender(Gender.MALE);
        req.setBirthDate(LocalDate.now().minusYears(30));

        when(roleRepository.findByName("PATIENT")).thenReturn(Optional.of(new Role()));
        when(cityRepository.findById(any())).thenReturn(Optional.of(new City()));
        when(passwordEncoder.encode(any())).thenReturn("encoded");

        // capture saved user
        doAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        }).when(userRepository).save(any(User.class));

        doAnswer(invocation -> {
            PatientProfile p = invocation.getArgument(0);
            p.setId(UUID.randomUUID());
            return p;
        }).when(patientRepo).save(any(PatientProfile.class));

        var result = registrationService.registerPatient(req);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
        verify(patientRepo, times(1)).save(any(PatientProfile.class));
        verify(emailService, times(1)).sendRegistrationEmail(eq("test@example.com"), anyString());
    }
}
