package io.github.andrehsvictor.mooral_api.common.jwt;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public Jwt encodeAccessToken(Authentication authentication, String sessionId) {
        return createTokenBuilder(authentication)
                .asAccessToken(sessionId)
                .build();
    }

    public Jwt encodeRefreshToken(Authentication authentication, String sessionId) {
        return createTokenBuilder(authentication)
                .asRefreshToken(sessionId)
                .build();
    }

    public Jwt encodeActionToken(Authentication authentication, JwtAction action, Map<String, Object> additionalClaims) {
        return createTokenBuilder(authentication)
                .asActionToken(action)
                .withClaims(additionalClaims)
                .build();
    }
    
    public JwtTokenBuilder createTokenBuilder(Authentication authentication) {
        return new JwtTokenBuilder(jwtEncoder, jwtProperties, authentication);
    }
}
