package andrehsvictor.mooral.account;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import andrehsvictor.mooral.exception.BadRequestException;
import andrehsvictor.mooral.user.TokenType;
import andrehsvictor.mooral.user.User;
import andrehsvictor.mooral.user.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailVerifier {

    private final UserService userService;

    @Value("${mooral.action-token.email-verification.lifespan:24h}")
    private String emailVerificationTokenLifespan;

    public void sendVerificationEmail(User user) {
        String token = generateVerificationToken();
        LocalDateTime expiresAt = calculateExpirationTime();
        
        user.setEmailVerificationToken(token);
        user.setEmailVerificationTokenExpiresAt(expiresAt);
        
        userService.save(user);
        
        // TODO: Enviar email de verificação
        // emailService.sendVerificationEmail(user.getEmail(), token);
    }

    public void verifyEmail(String token) {
        User user = userService.getByToken(token, TokenType.EMAIL_VERIFICATION);
        
        if (user.getEmailVerificationTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Email verification token has expired");
        }
        
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationTokenExpiresAt(null);
        
        userService.save(user);
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    private LocalDateTime calculateExpirationTime() {
        // Parse duration from configuration and add to current time
        // For now, using 24 hours as default
        return LocalDateTime.now().plusHours(24);
    }
}
