package co.edu.uniquindio.vitalcareback.Services.profiles;

// DoctorService.java

import co.edu.uniquindio.vitalcareback.Dto.profiles.DoctorProfileDTO;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.DoctorProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DoctorProfileService {

    private final DoctorProfileRepository doctorProfileRepository;

    public DoctorProfileService(DoctorProfileRepository doctorProfileRepository) {
        this.doctorProfileRepository = doctorProfileRepository;
    }

    public List<DoctorProfileDTO> getAllDoctors() {
        return doctorProfileRepository.findAllDoctorProfiles();
    }

    public DoctorProfileDTO getDoctorByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email es requerido");
        }
        return doctorProfileRepository.findDoctorProfileDtoByUserEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor no encontrado"));
    }
}

