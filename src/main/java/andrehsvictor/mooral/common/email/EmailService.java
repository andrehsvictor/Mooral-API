package andrehsvictor.mooral.common.email;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import andrehsvictor.mooral.common.email.dto.ActionEmailArgs;
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
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    private void sendTemplatedEmail(ActionEmailArgs dto, String template, String subject) {
        Context context = new Context();
        context.setVariable("url", dto.getUrl());
        context.setVariable("lifetime", dto.getLifetime());
        String text = templateEngine.process(template, context);
        send(dto.getTo(), subject, text);
    }

    @RabbitListener(queues = { "email-service.v1.send-email-verification" })
    public void sendVerificationEmail(ActionEmailArgs dto) {
        sendTemplatedEmail(dto, "verify-email", "Verify your email address - Mooral");
    }

    @RabbitListener(queues = { "email-service.v1.send-password-reset" })
    public void sendPasswordResetEmail(ActionEmailArgs dto) {
        sendTemplatedEmail(dto, "reset-password", "Reset your password - Mooral");
    }

    @RabbitListener(queues = { "email-service.v1.send-email-change" })
    public void sendEmailChangeNotification(ActionEmailArgs dto) {
        sendTemplatedEmail(dto, "change-email", "Change your email address - Mooral");
    }
}