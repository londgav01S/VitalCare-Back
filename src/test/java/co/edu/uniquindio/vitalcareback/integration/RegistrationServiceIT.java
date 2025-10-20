package co.edu.uniquindio.vitalcareback.integration;

import co.edu.uniquindio.vitalcareback.Dto.auth.RegistrationRequest;
import co.edu.uniquindio.vitalcareback.Dto.auth.UserDTO;
import co.edu.uniquindio.vitalcareback.Model.auth.Role;
import co.edu.uniquindio.vitalcareback.Model.common.Gender;
import co.edu.uniquindio.vitalcareback.Model.location.City;
import co.edu.uniquindio.vitalcareback.Model.location.Department;
import co.edu.uniquindio.vitalcareback.Model.location.Country;
import co.edu.uniquindio.vitalcareback.Repositories.auth.RoleRepository;
import co.edu.uniquindio.vitalcareback.Repositories.location.CityRepository;
import co.edu.uniquindio.vitalcareback.Repositories.location.DepartmentRepository;
import co.edu.uniquindio.vitalcareback.Repositories.location.CountryRepository;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.PatientProfileRepository;
import co.edu.uniquindio.vitalcareback.Services.auth.RegistrationService;
import co.edu.uniquindio.vitalcareback.Services.notifications.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for RegistrationService using Spring Boot context and H2.
 *
 * Estrategia:
 * - Se levanta el contexto completo con repositorios JPA reales y BD en memoria (H2).
 * - Beans de EmailService se reemplazan por un @Primary no-op para evitar efectos externos.
 * - Se siembran Country/Department/City por restricciones de FK.
 */
@SpringBootTest
@Transactional
class RegistrationServiceIT {

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
    }

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PatientProfileRepository patientProfileRepository;

    private City testCity;

    @BeforeEach
    void setUp() {
        roleRepository.findByName("PATIENT").orElseGet(() -> {
            Role r = new Role();
            r.setName("PATIENT");
            return roleRepository.save(r);
        });

    Country country = new Country();
    country.setName("Testland");
    country = countryRepository.save(country);

    Department department = new Department();
    department.setName("Central");
    department.setCountry(country);
    department = departmentRepository.save(department);

    City c = new City();
    c.setName("Test City");
    c.setDepartment(department);
    testCity = cityRepository.save(c);
    }

    /**
     * Caso feliz de integración: registrar un paciente persiste User/PatientProfile
     * y retorna un UserDTO con el email esperado.
     */
    @Test
    void registerPatient_persistsUserAndProfile() {
        RegistrationRequest req = new RegistrationRequest();
        req.setEmail("it-user@example.com");
        req.setPassword("Password1");
        req.setName("IT User");
        req.setIdNumber("ID-123");
        req.setCityId(testCity.getId());
        req.setGender(Gender.OTHER);
        req.setBirthDate(LocalDate.now().minusYears(25));

        UserDTO result = registrationService.registerPatient(req);

        assertNotNull(result);
        assertEquals("it-user@example.com", result.getEmail());
        assertFalse(patientProfileRepository.findAll().isEmpty(), "Debe existir al menos un PatientProfile");
    }
}
