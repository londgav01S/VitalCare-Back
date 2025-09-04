package co.edu.uniquindio.vitalcareback.Repositories.clinical;

import co.edu.uniquindio.vitalcareback.Model.clinical.Consultation;
import co.edu.uniquindio.vitalcareback.Model.profiles.DoctorProfile;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {
    List<Consultation> findByPatient(PatientProfile patient);
    List<Consultation> findByDoctor(DoctorProfile doctor);
}

