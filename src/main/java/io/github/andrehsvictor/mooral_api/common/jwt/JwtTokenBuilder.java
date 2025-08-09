package io.github.andrehsvictor.mooral_api.common.jwt;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

public class JwtTokenBuilder {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;
    private final Authentication authentication;
    private final Map<String, Object> extraClaims = new HashMap<>();

    private JwtType type;
    private Duration lifespan;

    public JwtTokenBuilder(JwtEncoder jwtEncoder, JwtProperties jwtProperties, Authentication authentication) {
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;
        this.authentication = authentication;
    }

    public JwtTokenBuilder asAccessToken(String sessionId) {
        this.type = JwtType.BEARER;
        this.lifespan = jwtProperties.getAccessTokenLifespan();
        this.extraClaims.put(JwtClaim.SESSION_ID.getValue(), sessionId);
        this.extraClaims.put(JwtClaim.SCOPE.getValue(), getScope());
        return this;
    }

    public JwtTokenBuilder asRefreshToken(String sessionId) {
        this.type = JwtType.REFRESH;
        this.lifespan = jwtProperties.getRefreshTokenLifespan();
        this.extraClaims.put(JwtClaim.SESSION_ID.getValue(), sessionId);
        this.extraClaims.put(JwtClaim.SCOPE.getValue(), getScope());
        return this;
    }

    public JwtTokenBuilder asActionToken(JwtAction action) {
        this.type = JwtType.ACTION;
        this.lifespan = jwtProperties.getActionTokenLifespan();
        this.extraClaims.put(JwtClaim.ACTION.getValue(), action.getValue());
        return this;
    }

    public JwtTokenBuilder withClaim(String key, Object value) {
        this.extraClaims.put(key, value);
        return this;
    }

    public JwtTokenBuilder withClaims(Map<String, Object> claims) {
        this.extraClaims.putAll(claims);
        return this;
    }

    public Jwt build() {
        Instant now = Instant.now();
        Instant expiration = now.plus(lifespan);

        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .issuer(jwtProperties.getIssuer())
                .audience(jwtProperties.getAudiences())
                .subject(authentication.getName())
                .issuedAt(now)
                .expiresAt(expiration)
                .claim(JwtClaim.TYPE.getValue(), type.getValue());

        extraClaims.forEach(claims::claim);

        return jwtEncoder.encode(JwtEncoderParameters.from(claims.build()));
    }

    private String getScope() {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
}
