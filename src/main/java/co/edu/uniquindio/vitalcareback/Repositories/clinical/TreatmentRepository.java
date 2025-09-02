package co.edu.uniquindio.vitalcareback.Repositories.clinical;


import co.edu.uniquindio.vitalcareback.Model.clinical.Diagnosis;
import co.edu.uniquindio.vitalcareback.Model.clinical.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
}

