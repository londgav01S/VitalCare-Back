package co.edu.uniquindio.vitalcareback.Services.auth;

import co.edu.uniquindio.vitalcareback.Dto.auth.RegistrationRequest;
import co.edu.uniquindio.vitalcareback.Dto.auth.UserDTO;
import co.edu.uniquindio.vitalcareback.Model.auth.*;
import co.edu.uniquindio.vitalcareback.Model.profiles.*;
import co.edu.uniquindio.vitalcareback.Model.location.City;
import co.edu.uniquindio.vitalcareback.Repositories.auth.*;
import co.edu.uniquindio.vitalcareback.Repositories.profiles.*;
import co.edu.uniquindio.vitalcareback.Repositories.location.CityRepository;
import co.edu.uniquindio.vitalcareback.Services.notifications.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * RegistrationService
 *
 * Servicio encargado del registro de usuarios según su tipo:
 * paciente, doctor o staff.
 * Crea tanto la entidad User como el perfil correspondiente
 * (PatientProfile, DoctorProfile o StaffProfile) y asigna el rol correspondiente.
 */
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
    private final EmailService emailService;

    /**
     * Registra un paciente.
     * Crea el usuario, asigna el rol PATIENT y construye el perfil de paciente.
     *
     * @param req DTO con datos de registro del paciente
     * @return UserDTO con id y email del usuario registrado
     */
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
        emailService.sendRegistrationEmail(user.getEmail(), user.getName());


        return new UserDTO(user.getId(), user.getEmail());
    }

    /**
     * Registra un doctor.
     * Crea el usuario, asigna el rol DOCTOR y construye el perfil de doctor.
     *
     * @param req DTO con datos de registro del doctor
     * @return UserDTO con id y email del usuario registrado
     */
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

        emailService.sendRegistrationEmail(user.getEmail(), user.getName());

        return new UserDTO(user.getId(), user.getEmail());
    }

    /**
     * Registra un staff.
     * Crea el usuario, asigna el rol STAFF y construye el perfil de staff.
     *
     * @param req DTO con datos de registro del staff
     * @return UserDTO con id y email del usuario registrado
     */
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
        emailService.sendRegistrationEmail(user.getEmail(), user.getName());

        return new UserDTO(user.getId(), user.getEmail());
    }

    /**
     * Construye un objeto User a partir de los datos de registro.
     *
     * @param req DTO con email y contraseña
     * @return User con email, password hash y enabled = true
     */
    private User buildUser(RegistrationRequest req) {
        User user = new User();
        user.setName(req.getName());
        user.setIdNumber(req.getIdNumber());
        user.setEmail(req.getEmail());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setEnabled(true);
        // Por si el front no envía el campo state, asignamos true por defecto
        user.setState(true);
        return user;
    }
}
