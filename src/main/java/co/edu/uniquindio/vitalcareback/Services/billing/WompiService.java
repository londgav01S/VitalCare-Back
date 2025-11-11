package co.edu.uniquindio.vitalcareback.Services.billing;

import co.edu.uniquindio.vitalcareback.Config.WompiProperties;
import co.edu.uniquindio.vitalcareback.Dto.billing.wompi.CheckoutSignatureResponse;
import co.edu.uniquindio.vitalcareback.Dto.billing.wompi.CreatePaymentSessionRequest;
import co.edu.uniquindio.vitalcareback.Dto.billing.wompi.WompiWebhookEvent;
import co.edu.uniquindio.vitalcareback.Model.billing.Invoice;
import co.edu.uniquindio.vitalcareback.Model.billing.InvoiceStatus;
import co.edu.uniquindio.vitalcareback.Model.billing.Payment;
import co.edu.uniquindio.vitalcareback.Model.billing.PaymentMethod;
import co.edu.uniquindio.vitalcareback.Model.billing.PaymentStatus;
import co.edu.uniquindio.vitalcareback.Repositories.billing.InvoiceRepository;
import co.edu.uniquindio.vitalcareback.Repositories.billing.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WompiService {

    private final WompiProperties wompiProperties;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate = new RestTemplate(); // Simple HTTP client

    /**
     * Genera la firma de integridad y referencia para inicializar el Checkout en el front.
     */
    public CheckoutSignatureResponse crearSesion(CreatePaymentSessionRequest request) {
    Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
        .orElseThrow(() -> new IllegalArgumentException("Invoice no encontrada"));

        Long amountInCents = invoice.getTotal().multiply(BigDecimal.valueOf(100)).longValue();
        String reference = generarReferencia();

        String cadena = reference + amountInCents + request.getCurrency() + wompiProperties.getIntegrityKey();
        String signature = DigestUtils.md5DigestAsHex(cadena.getBytes(StandardCharsets.UTF_8));

        // Persistimos un Payment en estado PENDING
    Payment payment = new Payment();
    payment.setInvoice(invoice);
    payment.setMethod(PaymentMethod.WOMPI);
    payment.setAmount(invoice.getTotal());
    payment.setPaymentDate(LocalDateTime.now());
    payment.setReference(reference);
    payment.setStatus(PaymentStatus.PENDING);
    paymentRepository.save(payment);

        return new CheckoutSignatureResponse(reference, amountInCents, request.getCurrency(), signature, wompiProperties.getPublicKey());
    }

    /**
     * Procesa evento webhook validando firma y actualizando estado de Payment/Invoice.
     */
    public void procesarWebhook(String providedSignature, String rawBody, WompiWebhookEvent event) {
        if (!firmaValida(providedSignature, rawBody)) {
            throw new SecurityException("Firma de webhook inválida");
        }
        WompiWebhookEvent.Transaction tx = event.getPayload().getTransaction();
        Optional<Payment> opt = paymentRepository.findAll().stream()
                .filter(p -> tx.getReference().equals(p.getReference()))
                .findFirst();
        if (opt.isEmpty()) return; // No referencia encontrada
        Payment payment = opt.get();
        payment.setTransactionId(tx.getId());
        payment.setStatus(mapStatus(tx.getStatus()));
        if (payment.getStatus() == PaymentStatus.APPROVED) {
            Invoice invoice = payment.getInvoice();
            invoice.setStatus(InvoiceStatus.PAID);
            invoiceRepository.save(invoice);
        }
        paymentRepository.save(payment);
    }

    /** Verifica la firma del webhook usando eventsSecret según doc Wompi (SHA-256 hex). */
    public boolean firmaValida(String providedSignature, String rawBody) {
        // Wompi firma: hex( SHA256(rawBody + eventsSecret) )
        String material = rawBody + wompiProperties.getEventsSecret();
        java.security.MessageDigest digest;
        try {
            digest = java.security.MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
        byte[] hash = digest.digest(material.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString().equalsIgnoreCase(providedSignature);
    }

    private PaymentStatus mapStatus(String wompiStatus) {
        return switch (wompiStatus.toUpperCase()) {
            case "APPROVED" -> PaymentStatus.APPROVED;
            case "DECLINED" -> PaymentStatus.DECLINED;
            case "VOIDED" -> PaymentStatus.VOIDED;
            case "ERROR" -> PaymentStatus.ERROR;
            default -> PaymentStatus.PENDING;
        };
    }

    private String generarReferencia() {
        return "INV-" + UUID.randomUUID();
    }
}
