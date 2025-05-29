package andrehsvictor.mooral.jwt;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class JwtLifespanProperties {

    @Value("${mooral.jwt.access-token.lifespan:15m}")
    private Duration accessTokenLifespan;

    @Value("${mooral.jwt.refresh-token.lifespan:1h}")
    private Duration refreshTokenLifespan;

}