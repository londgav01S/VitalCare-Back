package co.edu.uniquindio.vitalcareback.Repositories.clinical;

import co.edu.uniquindio.vitalcareback.Model.clinical.Consultation;
import co.edu.uniquindio.vitalcareback.Model.clinical.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByConsultation(Consultation consultation);
}

