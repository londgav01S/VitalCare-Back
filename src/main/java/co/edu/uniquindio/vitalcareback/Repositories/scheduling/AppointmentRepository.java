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

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByPatient(PatientProfile patient);

    List<Appointment> findByDoctor(DoctorProfile doctor);

    List<Appointment> findByDoctorAndScheduledDateBetween(
            DoctorProfile doctor,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Appointment> findByPatientAndStatus(
            PatientProfile patient,
            AppointmentStatus status
    );


    // Buscar citas por paciente (id)
    List<Appointment> findByPatientId(UUID patientId);

    // Buscar citas por doctor (id)
    List<Appointment> findByDoctorId(UUID doctorId);

    // Opcional: buscar por estado
    List<Appointment> findByStatus(AppointmentStatus status);
}

