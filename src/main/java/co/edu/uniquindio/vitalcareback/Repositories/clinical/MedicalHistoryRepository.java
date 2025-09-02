package co.edu.uniquindio.vitalcareback.Repositories.clinical;

import co.edu.uniquindio.vitalcareback.Model.clinical.MedicalHistory;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
    List<MedicalHistory> findByPatient(PatientProfile patient);
}

