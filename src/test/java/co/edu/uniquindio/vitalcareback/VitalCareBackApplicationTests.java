package co.edu.uniquindio.vitalcareback;

import co.edu.uniquindio.vitalcareback.Services.auth.RegistrationService;
import co.edu.uniquindio.vitalcareback.Services.clinical.ConsultationService;
import co.edu.uniquindio.vitalcareback.Services.scheduling.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Spring Boot context smoke tests.
 *
 * Verifica que el contexto de Spring arranque y que los servicios principales
 * estén disponibles via inyección de dependencias.
 */
@SpringBootTest
class VitalCareBackApplicationTests {

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private ConsultationService consultationService;

	/**
	 * Smoke test del contexto de Spring Boot.
	 */
	@Test
	void contextLoads() {
		// Verifica que el contexto de Spring arranca correctamente
	}

	/**
	 * Verifica que servicios clave están presentes en el contexto.
	 */
	@Test
	void servicesAreAvailableInContext() {
		assertNotNull(registrationService);
		assertNotNull(appointmentService);
		assertNotNull(consultationService);
	}
}
