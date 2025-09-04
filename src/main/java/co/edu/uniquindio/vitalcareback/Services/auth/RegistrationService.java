package co.edu.uniquindio.vitalcareback.Services.auth;

import co.edu.uniquindio.vitalcareback.Dto.auth.RegistrationRequest;
import co.edu.uniquindio.vitalcareback.Dto.auth.UserDTO;
import co.edu.uniquindio.vitalcareback.Model.auth.*;
import co.edu.uniquindio.vitalcareback.Model.profiles.*;
import co.edu.uniquindio.vitalcareback.Model.location.City;
import co.edu.uniquindio.vitalcareback.Repositories.auth.*;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.*;
import co.edu.uniquindio.vitalcareback.Repositories.location.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PatientProfileRepository patientRepo;
    private final DoctorProfileRepository doctorRepo;
    private final StaffProfileRepository staffRepo;
    private final CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO registerPatient(RegistrationRequest req) {
        User user = buildUser(req);
        Role role = roleRepository.findByName("PATIENT")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: PATIENT"));
        user.getRoles().add(new UserRole(user, role));
        userRepository.save(user);

        City city = cityRepository.findById(req.getCityId())
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));


        PatientProfile profile = new PatientProfile();
        profile.setUser(user);
        profile.setGender(req.getGender());
        profile.setBirthDate(req.getBirthDate());
        profile.setBloodType(req.getBloodType());
        profile.setEmail(req.getEmail());
        profile.setPhone(req.getPhone());
        profile.setAddress(req.getAddress());
        profile.setCity(city);
        patientRepo.save(profile);

        return new UserDTO(user.getId(), user.getEmail());
    }

    public UserDTO registerDoctor(RegistrationRequest req) {
        User user = buildUser(req);
        Role role = roleRepository.findByName("DOCTOR")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: DOCTOR"));
        user.getRoles().add(new UserRole(user, role));
        userRepository.save(user);

        DoctorProfile profile = new DoctorProfile();
        profile.setUser(user);
        profile.setLicenseNumber(req.getLicenseNumber());
        profile.setSpecialty(req.getSpecialty());
        profile.setLastName(req.getLastName());
        doctorRepo.save(profile);

        return new UserDTO(user.getId(), user.getEmail());
    }

    public UserDTO registerStaff(RegistrationRequest req) {
        User user = buildUser(req);
        Role role = roleRepository.findByName("STAFF")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: STAFF"));
        user.getRoles().add(new UserRole(user, role));
        userRepository.save(user);

        StaffProfile profile = new StaffProfile();
        profile.setUser(user);
        profile.setDepartment(req.getDepartment());
        profile.setPosition(req.getPosition());
        staffRepo.save(profile);

        return new UserDTO(user.getId(), user.getEmail());
    }

    private User buildUser(RegistrationRequest req) {
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setEnabled(true);
        return user;
    }
}

