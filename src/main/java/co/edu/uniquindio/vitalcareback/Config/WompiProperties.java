package co.edu.uniquindio.vitalcareback.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "wompi")
public class WompiProperties {
    /** Llave pública para front */
    private String publicKey;
    /** Llave privada para autenticación server-server */
    private String privateKey;
    /** Secreto de eventos (webhooks) */
    private String eventsSecret;
    /** Llave de integridad para Checkout */
    private String integrityKey;
    /** URL base API Wompi (por defecto producción) */
    private String baseUrl = "https://production.wompi.co/v1";
}
