package co.edu.uniquindio.vitalcareback.Repositories.billing;

import co.edu.uniquindio.vitalcareback.Model.billing.Invoice;
import co.edu.uniquindio.vitalcareback.Model.billing.InvoiceStatus;
import co.edu.uniquindio.vitalcareback.Model.profiles.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    List<Invoice> findByPatient(PatientProfile patient);

    List<Invoice> findByStatus(InvoiceStatus status);

}

