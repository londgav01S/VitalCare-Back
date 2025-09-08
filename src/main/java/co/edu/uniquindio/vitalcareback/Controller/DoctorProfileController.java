package co.edu.uniquindio.vitalcareback.Controller;

import co.edu.uniquindio.vitalcareback.Dto.profiles.DoctorProfileDTO;
import co.edu.uniquindio.vitalcareback.Services.profiles.DoctorProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorProfileController {

    private final DoctorProfileService doctorProfileService;

    public DoctorProfileController(DoctorProfileService doctorProfileService) {
        this.doctorProfileService = doctorProfileService;
    }

    @GetMapping
    public List<DoctorProfileDTO> getAllDoctors() {
        return doctorProfileService.getAllDoctors();
    }
}