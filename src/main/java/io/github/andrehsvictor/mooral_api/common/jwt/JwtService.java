package io.github.andrehsvictor.mooral_api.common.jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final JwtProperties jwtProperties;

    public Jwt encodeAccessToken(Authentication authentication, String sessionId) {
        Instant iat = Instant.now();
        Instant exp = iat.plus(jwtProperties.getAccessTokenLifespan());
        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .audience(jwtProperties.getAudiences())
                .subject(authentication.getName())
                .issuedAt(iat)
                .expiresAt(exp)
                .claim(JwtClaim.SESSION_ID.value, sessionId)
                .claim(JwtClaim.SCOPE.value, String.join(" ", authentication.getAuthorities()))
                .claim(JwtClaim.TYPE.value, JwtType.BEARER.value);

        return jwtEncoder.encode(JwtEncoderParameters.from(claims.build()));
    }

    public Jwt encodeRefreshToken(Authentication authentication, String sessionId) {
        Instant iat = Instant.now();
        Instant exp = iat.plus(jwtProperties.getRefreshTokenLifespan());
        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .audience(jwtProperties.getAudiences())
                .subject(authentication.getName())
                .issuedAt(iat)
                .expiresAt(exp)
                .claim(JwtClaim.SESSION_ID.value, sessionId)
                .claim(JwtClaim.SCOPE.value, String.join(" ", authentication.getAuthorities()))
                .claim(JwtClaim.TYPE.value, JwtType.REFRESH.value);

        return jwtEncoder.encode(JwtEncoderParameters.from(claims.build()));
    }

    public Jwt encodeActionToken(Authentication authentication, JwtAction action,
            Map<String, Object> additionalClaims) {
        Instant iat = Instant.now();
        Instant exp = iat.plus(jwtProperties.getActionTokenLifespan());
        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .audience(jwtProperties.getAudiences())
                .subject(authentication.getName())
                .issuedAt(iat)
                .expiresAt(exp)
                .claim(JwtClaim.TYPE.value, JwtType.ACTION.value)
                .claim(JwtClaim.ACTION.value, action.value);

        additionalClaims.forEach(claims::claim);

        return jwtEncoder.encode(JwtEncoderParameters.from(claims.build()));
    }

}
