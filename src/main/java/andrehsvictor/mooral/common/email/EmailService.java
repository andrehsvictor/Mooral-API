package andrehsvictor.mooral.common.email;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username:test@mooral.dev}")
    private String from;

    public void send(String to, String subject, String text) {
        try {
            MimeMessage message = createMimeMessage(to, subject, text);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public void sendUsingTemplate(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            String processedText = processTemplate(templateName, variables);
            send(to, subject, processedText);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email with template: " + e.getMessage(), e);
        }
    }

    public void sendEmailVerification(String to, String url, String lifetime) {
        String subject = "Verify your email address - Mooral";
        String templateName = "verify-email";
        Map<String, Object> variables = Map.of(
                "url", url,
                "lifetime", lifetime);
        sendUsingTemplate(to, subject, templateName, variables);
    }

    public void sendPasswordReset(String to, String url, String lifetime) {
        String subject = "Reset your password - Mooral";
        String templateName = "reset-password";
        Map<String, Object> variables = Map.of(
                "url", url,
                "lifetime", lifetime);
        sendUsingTemplate(to, subject, templateName, variables);
    }

    public void sendEmailChange(String to, String url, String lifetime) {
        String subject = "Confirm your email change - Mooral";
        String templateName = "change-email";
        Map<String, Object> variables = Map.of(
                "url", url,
                "lifetime", lifetime);
        sendUsingTemplate(to, subject, templateName, variables);
    }

    private MimeMessage createMimeMessage(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        return message;
    }

    private String processTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        if (variables != null) {
            context.setVariables(variables);
        }
        return templateEngine.process(templateName, context);
    }

}
