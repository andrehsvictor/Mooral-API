package andrehsvictor.mooral.account;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.stereotype.Service;

import andrehsvictor.mooral.email.EmailService;
import andrehsvictor.mooral.exception.BadRequestException;
import andrehsvictor.mooral.exception.ResourceConflictException;
import andrehsvictor.mooral.user.TokenType;
import andrehsvictor.mooral.user.User;
import andrehsvictor.mooral.user.UserService;
import andrehsvictor.mooral.util.ClasspathFileService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailVerifier {

    private static final String EMAIL_TEMPLATE_PATH = "templates/verify-email.html";
    private static final String EMAIL_SUBJECT = "Verifique seu e-mail - Mooral";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int TOKEN_BYTES_LENGTH = 32;

    private final EmailService emailService;
    private final UserService userService;
    private final ClasspathFileService classpathFileService;
    private final ActionTokenLifespanProperties tokenProperties;

    public void sendVerificationEmail(String email, String url) {
        User user = userService.getByEmail(email);
        if (user.isEmailVerified()) {
            throw new ResourceConflictException("Email already verified");
        }

        String token = generateToken();
        LocalDateTime expiresAt = LocalDateTime.now().plus(tokenProperties.getEmailVerificationTokenLifespan());

        user.setEmailVerificationToken(token);
        user.setEmailVerificationTokenExpiresAt(expiresAt);
        userService.save(user);

        String verificationUrl = url + (url.contains("?") ? "&" : "?") + "token=" + token;
        String template = classpathFileService.getContent(EMAIL_TEMPLATE_PATH);
        String expirationText = formatDuration(tokenProperties.getEmailVerificationTokenLifespan());

        String emailBody = template
                .replace("{{link}}", verificationUrl)
                .replace("{{expiration}}", expirationText);

        emailService.send(email, EMAIL_SUBJECT, emailBody);
    }

    public void verify(String token) {
        User user = userService.getByToken(token, TokenType.EMAIL_VERIFICATION);
        if (user.getEmailVerificationTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Action token expired");
        }
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationTokenExpiresAt(null);
        userService.save(user);
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();

        if (hours > 0) {
            String hourText = hours + (hours == 1 ? " hora" : " horas");
            return minutes > 0 ? hourText + " e " + minutes + (minutes == 1 ? " minuto" : " minutos") : hourText;
        } else {
            long totalMinutes = duration.toMinutes();
            return totalMinutes + (totalMinutes == 1 ? " minuto" : " minutos");
        }
    }

    private String generateToken() {
        byte[] randomBytes = new byte[TOKEN_BYTES_LENGTH];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}