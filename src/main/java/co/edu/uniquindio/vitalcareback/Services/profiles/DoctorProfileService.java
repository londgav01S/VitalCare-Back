package co.edu.uniquindio.vitalcareback.Services.profiles;

// DoctorService.java

import co.edu.uniquindio.vitalcareback.Dto.profiles.DoctorProfileDTO;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.DoctorProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorProfileService {

    private final DoctorProfileRepository doctorProfileRepository;

    public DoctorProfileService(DoctorProfileRepository doctorProfileRepository) {
        this.doctorProfileRepository = doctorProfileRepository;
    }

    public List<DoctorProfileDTO> getAllDoctors() {
        return doctorProfileRepository.findAllDoctorProfiles();
    }
}

