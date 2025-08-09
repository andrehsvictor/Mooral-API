package io.github.andrehsvictor.mooral_api.common.jwt.validation;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import io.github.andrehsvictor.mooral_api.common.tokenrevocation.TokenRevocationService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidator implements OAuth2TokenValidator<Jwt> {

    private final TokenRevocationService tokenRevocationService;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if (tokenRevocationService.isRevoked(token)) {
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Token is revoked", null));
        }
        return OAuth2TokenValidatorResult.success();
    }

}
