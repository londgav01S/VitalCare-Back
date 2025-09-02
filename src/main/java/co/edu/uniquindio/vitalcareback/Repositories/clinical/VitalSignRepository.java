package co.edu.uniquindio.vitalcareback.Repositories.clinical;


import co.edu.uniquindio.vitalcareback.Model.clinical.Consultation;
import co.edu.uniquindio.vitalcareback.Model.clinical.VitalSign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VitalSignRepository extends JpaRepository<VitalSign, Long> {
    List<VitalSign> findByConsultation(Consultation consultation);
}
