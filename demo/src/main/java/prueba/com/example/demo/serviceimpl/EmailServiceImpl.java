package prueba.com.example.demo.serviceimpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import prueba.com.example.demo.services.EmailService;

import java.io.UnsupportedEncodingException;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    @Value("${app.mail.from-name:AutoTaller}")
    private String fromName;

    @Override
    public void sendTemporaryPassword(String toEmail, String technicianName, String tempPassword, String workshopName) {
        String subject = "Tu acceso a " + workshopName + " en AutoTaller";
        String html = """
            <div style="font-family:Arial,sans-serif;max-width:560px;margin:auto;color:#222">
              <h2>Bienvenido a %s 👋</h2>
              <p>Hola <strong>%s</strong>, el administrador del taller te ha creado una cuenta de técnico.</p>
              <p>Estas son tus credenciales temporales para iniciar sesión:</p>
              <table style="border-collapse:collapse;margin:16px 0">
                <tr><td style="padding:6px 12px;background:#f4f4f4"><b>Email</b></td>
                    <td style="padding:6px 12px;background:#f4f4f4">%s</td></tr>
                <tr><td style="padding:6px 12px"><b>Contraseña temporal</b></td>
                    <td style="padding:6px 12px"><code style="font-size:15px">%s</code></td></tr>
              </table>
              <p style="color:#b00">Por seguridad debes cambiarla la primera vez que ingreses.</p>
              <hr style="border:none;border-top:1px solid #eee">
              <p style="font-size:12px;color:#888">AutoTaller · mensaje automático, no responder.</p>
            </div>
            """.formatted(workshopName, technicianName, toEmail, tempPassword);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(new InternetAddress(from, fromName));
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
            log.info("Correo de bienvenida enviado a {}", toEmail);
        } catch (MessagingException | UnsupportedEncodingException | MailException e) {
            log.error("Fallo enviando correo a {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo al técnico: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String userName, String resetUrl) {
        String subject = "Restablece tu contraseña en AutoTaller";
        String safeName = (userName == null || userName.isBlank()) ? toEmail : userName;
        String html = """
            <div style="font-family:Arial,sans-serif;max-width:560px;margin:auto;color:#222">
              <h2>Restablecer contraseña 🔐</h2>
              <p>Hola <strong>%s</strong>, recibimos una solicitud para restablecer tu contraseña en AutoTaller.</p>
              <p>Haz click en el siguiente botón para crear una nueva contraseña. El enlace expira en <b>30 minutos</b>.</p>
              <p style="margin:24px 0">
                <a href="%s" style="background:#0066cc;color:#fff;padding:12px 24px;text-decoration:none;border-radius:6px;display:inline-block">
                  Restablecer contraseña
                </a>
              </p>
              <p style="font-size:13px;color:#555">Si el botón no funciona, copia y pega este enlace en tu navegador:</p>
              <p style="font-size:12px;word-break:break-all;color:#0066cc">%s</p>
              <p style="color:#b00;font-size:13px">Si no solicitaste este cambio, ignora este correo. Tu contraseña seguirá igual.</p>
              <hr style="border:none;border-top:1px solid #eee">
              <p style="font-size:12px;color:#888">AutoTaller · mensaje automático, no responder.</p>
            </div>
            """.formatted(safeName, resetUrl, resetUrl);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(new InternetAddress(from, fromName));
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
            log.info("Correo de reset de contraseña enviado a {}", toEmail);
        } catch (MessagingException | UnsupportedEncodingException | MailException e) {
            log.error("Fallo enviando correo de reset a {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo de reset: " + e.getMessage(), e);
        }
    }
}
