package co.edu.uniquindio.vitalcareback.Repositories.billing;

import co.edu.uniquindio.vitalcareback.Model.billing.Invoice;
import co.edu.uniquindio.vitalcareback.Model.billing.Payment;
import co.edu.uniquindio.vitalcareback.Model.billing.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByInvoice(Invoice invoice);

    List<Payment> findByMethod(PaymentMethod method);
}

