package andrehsvictor.mooral.account;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class ActionTokenLifespanProperties {

    @Value("${mooral.action-token.email-verification.lifespan:6h}")
    private Duration emailVerificationTokenLifespan = Duration.ofHours(6);

    @Value("${mooral.action-token.password-reset.lifespan:1h}")
    private Duration passwordResetTokenLifespan = Duration.ofHours(1);

}