package co.edu.uniquindio.vitalcareback.Services.billing;

import co.edu.uniquindio.vitalcareback.Config.WompiProperties;
import co.edu.uniquindio.vitalcareback.Dto.billing.wompi.CreatePaymentSessionRequest;
import co.edu.uniquindio.vitalcareback.Model.billing.Invoice;
import co.edu.uniquindio.vitalcareback.Model.billing.InvoiceStatus;
import co.edu.uniquindio.vitalcareback.Repositories.billing.InvoiceRepository;
import co.edu.uniquindio.vitalcareback.Repositories.billing.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class WompiServiceTest {

    private WompiService wompiService;
    private WompiProperties props;
    private InvoiceRepository invoiceRepository;
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setup() {
        props = new WompiProperties();
        props.setIntegrityKey("TEST_INTEGRITY");
        props.setPublicKey("pub_test_xxx");
        props.setEventsSecret("EV_SECRET");
        props.setPrivateKey("priv_xxx");

        invoiceRepository = Mockito.mock(InvoiceRepository.class);
        paymentRepository = Mockito.mock(PaymentRepository.class);
        wompiService = new WompiService(props, invoiceRepository, paymentRepository);
    }

    @Test
    void creaSesionGeneraSignature() {
        Invoice invoice = new Invoice();
        invoice.setId(UUID.randomUUID());
        invoice.setTotal(BigDecimal.valueOf(1234));
        invoice.setStatus(InvoiceStatus.PENDING);
        when(invoiceRepository.findById(invoice.getId())).thenReturn(Optional.of(invoice));
        when(paymentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var request = new CreatePaymentSessionRequest(invoice.getId(), "COP");
        var response = wompiService.crearSesion(request);

        assertNotNull(response.getSignature());
        assertEquals("COP", response.getCurrency());
        assertEquals(props.getPublicKey(), response.getPublicKey());
        assertTrue(response.getAmountInCents() > 0);
    }

    @Test
    void firmaWebhookValida() {
        String body = "{\"test\":1}";
        String material = body + props.getEventsSecret();
        try {
            var digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(material.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            String expectedSignature = sb.toString();
            assertTrue(wompiService.firmaValida(expectedSignature, body));
        } catch (Exception e) {
            fail(e);
        }
    }
}
