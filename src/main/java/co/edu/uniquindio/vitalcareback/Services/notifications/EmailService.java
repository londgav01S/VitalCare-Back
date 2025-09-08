package co.edu.uniquindio.vitalcareback.Services.notifications;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * EmailService
 *
 * Servicio encargado del envío de correos electrónicos.
 * Permite enviar correos de texto plano o en formato HTML
 * utilizando JavaMailSender.
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Envía un correo electrónico de texto plano.
     *
     * @param to      Destinatario del correo
     * @param subject Asunto del correo
     * @param text    Contenido del correo en texto plano
     */
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    /**
     * Envía un correo electrónico en formato HTML.
     * Permite enviar correos más personalizados con estilos y formato.
     *
     * @param to       Destinatario del correo
     * @param subject  Asunto del correo
     * @param htmlBody Contenido del correo en HTML
     * @throws RuntimeException si ocurre un error al crear o enviar el correo
     */
    public void sendHtmlMessage(String to, String subject, String htmlBody) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo HTML", e);
        }
    }
}
