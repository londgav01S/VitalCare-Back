package co.edu.uniquindio.vitalcareback.Services.notifications;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email:no-reply@vitalcare.com}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try {
            Email from = new Email(fromEmail, "VitalCare");
            Email toEmail = new Email(to);
            Content content = new Content("text/html", body);

            Mail mail = new Mail(from, subject, toEmail, content);
            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar correo: " + e.getMessage(), e);
        }
    }

    public void sendRegistrationEmail(String to, String name) {
        sendEmail(to, "Bienvenido a VitalCare 🎉",
                "<h2>¡Hola " + name + "!</h2><p>Tu registro en <b>VitalCare</b> se ha completado con éxito.</p>");
    }

    public void sendLoginAlert(String to, String ip) {
        sendEmail(to, "Nuevo inicio de sesión detectado 🔐",
                "<p>Hemos detectado un nuevo inicio de sesión en tu cuenta desde la IP <b>" + ip + "</b>.</p>");
    }

    public void sendPasswordResetCode(String to, String code) {
        sendEmail(to, "Código para restablecer tu contraseña",
                "<p>Tu código de verificación es: <b>" + code + "</b><br>Este código expira en 10 minutos.</p>");
    }
}
