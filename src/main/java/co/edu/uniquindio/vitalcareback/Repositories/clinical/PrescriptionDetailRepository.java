package co.edu.uniquindio.vitalcareback.Repositories.clinical;

import co.edu.uniquindio.vitalcareback.Model.clinical.Prescription;
import co.edu.uniquindio.vitalcareback.Model.clinical.PrescriptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionDetailRepository extends JpaRepository<PrescriptionDetail, Long> {
    List<PrescriptionDetail> findByPrescription(Prescription prescription);
}

