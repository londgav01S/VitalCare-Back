package co.edu.uniquindio.vitalcareback.Repositories.clinical;

import co.edu.uniquindio.vitalcareback.Model.clinical.MedicalRecord;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByPatient(PatientProfile patient);
}

