package co.edu.uniquindio.vitalcareback.Repositories.scheduling;

import co.edu.uniquindio.vitalcareback.Model.profiles.DoctorProfile;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import co.edu.uniquindio.vitalcareback.Model.scheduling.Appointment;
import co.edu.uniquindio.vitalcareback.Model.scheduling.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * AppointmentRepository
 *
 * Repositorio JPA para la entidad Appointment (cita médica).
 * Proporciona métodos para consultar citas por paciente, doctor, fechas y estado.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    /**
     * Obtiene todas las citas asociadas a un paciente.
     *
     * @param patient Perfil del paciente
     * @return Lista de citas del paciente
     */
    List<Appointment> findByPatient(PatientProfile patient);

    /**
     * Obtiene todas las citas asociadas a un doctor.
     *
     * @param doctor Perfil del doctor
     * @return Lista de citas del doctor
     */
    List<Appointment> findByDoctor(DoctorProfile doctor);

    /**
     * Obtiene todas las citas de un doctor entre dos fechas.
     *
     * @param doctor Perfil del doctor
     * @param start Fecha de inicio
     * @param end Fecha de fin
     * @return Lista de citas del doctor dentro del rango
     */
    List<Appointment> findByDoctorAndScheduledDateBetween(
            DoctorProfile doctor,
            LocalDateTime start,
            LocalDateTime end
    );

    /**
     * Obtiene todas las citas de un paciente filtrando por estado.
     *
     * @param patient Perfil del paciente
     * @param status Estado de la cita
     * @return Lista de citas del paciente con el estado especificado
     */
    List<Appointment> findByPatientAndStatus(
            PatientProfile patient,
            AppointmentStatus status
    );

    /**
     * Buscar citas por ID del paciente.
     *
     * @param patientId UUID del paciente
     * @return Lista de citas del paciente
     */
    List<Appointment> findByPatientId(UUID patientId);

    /**
     * Buscar citas por ID del doctor.
     *
     * @param doctorId UUID del doctor
     * @return Lista de citas del doctor
     */
    List<Appointment> findByDoctorId(UUID doctorId);

    /**
     * Buscar citas por estado.
     *
     * @param status Estado de la cita
     * @return Lista de citas con el estado especificado
     */
    List<Appointment> findByStatus(AppointmentStatus status);
}
