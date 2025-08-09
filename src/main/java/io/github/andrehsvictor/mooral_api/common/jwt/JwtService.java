package io.github.andrehsvictor.mooral_api.common.jwt;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public Jwt encodeAccessToken(Authentication authentication, String sessionId) {
        Map<String, Object> extraClaims = Map.of(
            JwtClaim.SESSION_ID.value, sessionId,
            JwtClaim.SCOPE.value, getScope(authentication)
        );
        return createToken(authentication, JwtType.BEARER, jwtProperties.getAccessTokenLifespan(), extraClaims);
    }

    public Jwt encodeRefreshToken(Authentication authentication, String sessionId) {
        Map<String, Object> extraClaims = Map.of(
            JwtClaim.SESSION_ID.value, sessionId,
            JwtClaim.SCOPE.value, getScope(authentication)
        );
        return createToken(authentication, JwtType.REFRESH, jwtProperties.getRefreshTokenLifespan(), extraClaims);
    }

    public Jwt encodeActionToken(Authentication authentication, JwtAction action, Map<String, Object> additionalClaims) {
        Map<String, Object> extraClaims = new HashMap<>(additionalClaims);
        extraClaims.put(JwtClaim.ACTION.value, action.value);
        return createToken(authentication, JwtType.ACTION, jwtProperties.getActionTokenLifespan(), extraClaims);
    }

    private String getScope(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }

    private Jwt createToken(Authentication authentication, JwtType type, Duration lifespan, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        Instant expiration = now.plus(lifespan);
        
        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .audience(jwtProperties.getAudiences())
                .subject(authentication.getName())
                .issuedAt(now)
                .expiresAt(expiration)
                .claim(JwtClaim.TYPE.value, type.value);

        extraClaims.forEach(claims::claim);

        return jwtEncoder.encode(JwtEncoderParameters.from(claims.build()));
    }

}
