package co.edu.uniquindio.vitalcareback.Controller.billing;

import co.edu.uniquindio.vitalcareback.Dto.billing.wompi.CheckoutSignatureResponse;
import co.edu.uniquindio.vitalcareback.Dto.billing.wompi.CreatePaymentSessionRequest;
import co.edu.uniquindio.vitalcareback.Dto.billing.wompi.WompiWebhookEvent;
import co.edu.uniquindio.vitalcareback.Services.billing.WompiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final WompiService wompiService;

    @PostMapping("/session")
    public ResponseEntity<CheckoutSignatureResponse> createSession(@Valid @RequestBody CreatePaymentSessionRequest request) {
        CheckoutSignatureResponse response = wompiService.crearSesion(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(HttpServletRequest httpRequest, @RequestHeader(name = "X-Event-Signature", required = false) String signature,
                                        @RequestBody WompiWebhookEvent event) throws IOException {
        String rawBody = getRawBody(httpRequest);
        wompiService.procesarWebhook(signature, rawBody, event);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private String getRawBody(HttpServletRequest request) throws IOException {
        try (BufferedReader reader = request.getReader()) {
            return reader.lines().collect(Collectors.joining());
        }
    }
}
