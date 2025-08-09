package io.github.andrehsvictor.mooral_api.common.jwt;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final JwtProperties jwtProperties;
    private final SecurityContextHolderStrategy securityContextHolderStrategy;

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

    public Jwt encodeActionToken(Authentication authentication, JwtAction action,
            Map<String, Object> additionalClaims) {
        return createTokenBuilder(authentication)
                .asActionToken(action)
                .withClaims(additionalClaims)
                .build();
    }

    public JwtTokenBuilder createTokenBuilder(Authentication authentication) {
        return new JwtTokenBuilder(jwtEncoder, jwtProperties, authentication);
    }

    public Jwt decode(String token) {
        return jwtDecoder.decode(token);
    }

    public UUID getCurrentUserId() {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            String sub = jwt.getSubject();
            return UUID.fromString(sub);
        }
        return null;
    }

    public List<String> getCurrentUserPermissions() {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            String scope = jwt.getClaimAsString(JwtClaim.SCOPE.getValue());
            return List.of(scope.split(" "));
        }
        return List.of();
    }
}
