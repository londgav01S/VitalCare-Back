package co.edu.uniquindio.vitalcareback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan // Escanea clases @ConfigurationProperties como WompiProperties
public class VitalCareBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(VitalCareBackApplication.class, args);
	}

}
