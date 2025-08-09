package io.github.andrehsvictor.mooral_api.common.jwt;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class JwtProperties {

    @Value("${mooral.jwt.issuer:http://localhost:8080}")
    private String issuer = "http://localhost:8080";

    @Value("${mooral.jwt.audiences:[dev]}")
    private List<String> audiences = List.of("dev");

    @Value("${spring.security.oauth2.resourceserver.jwt.public-key-location}")
    private RSAPublicKey publicKey;

    @Value("${mooral.jwt.private-key-location}")
    private RSAPrivateKey privateKey;

    @Value("${mooral.jwt.access-token-lifespan:15m}")
    private Duration accessTokenLifespan = Duration.ofMinutes(15);

    @Value("${mooral.jwt.refresh-token-lifespan:1h}")
    private Duration refreshTokenLifespan = Duration.ofHours(1);

    @Value("${mooral.jwt.action-token-lifespan:1h}")
    private Duration actionTokenLifespan = Duration.ofHours(1);

}
