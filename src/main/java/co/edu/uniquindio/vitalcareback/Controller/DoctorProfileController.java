package co.edu.uniquindio.vitalcareback.Controller;

import co.edu.uniquindio.vitalcareback.Dto.profiles.DoctorProfileDTO;
import co.edu.uniquindio.vitalcareback.Services.profiles.DoctorProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DoctorProfileController
 *
 * Controlador REST encargado de manejar operaciones relacionadas con los perfiles de los doctores.
 * Proporciona endpoints para consultar la información de todos los doctores registrados en el sistema.
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorProfileController {

    /**
     * Servicio que contiene la lógica de negocio para los perfiles de los doctores.
     */
    private final DoctorProfileService doctorProfileService;

    /**
     * Constructor para inyección de dependencias del servicio de perfiles de doctores.
     *
     * @param doctorProfileService Servicio de perfiles de doctores
     */
    public DoctorProfileController(DoctorProfileService doctorProfileService) {
        this.doctorProfileService = doctorProfileService;
    }

    /**
     * Endpoint para obtener todos los perfiles de los doctores registrados.
     *
     * @return Lista de DoctorProfileDTO con información de todos los doctores
     */
    @GetMapping
    public List<DoctorProfileDTO> getAllDoctors() {
        return doctorProfileService.getAllDoctors();
    }
}
