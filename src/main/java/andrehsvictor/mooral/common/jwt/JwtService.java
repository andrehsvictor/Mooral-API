package andrehsvictor.mooral.common.jwt;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import andrehsvictor.mooral.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecurityContextHolderStrategy securityContextHolderStrategy;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${mooral.jwt.access-token.lifetime:15m}")
    private Duration accessTokenLifetime = Duration.ofMinutes(15);

    @Value("${mooral.jwt.refresh-token.lifetime:1h}")
    private Duration refreshTokenLifetime = Duration.ofHours(1);

    public boolean isPresent() {
        Authentication auth = securityContextHolderStrategy.getContext().getAuthentication();
        return auth instanceof JwtAuthenticationToken;
    }

    public UUID getUserId() {
        if (!isPresent()) {
            throw new UnauthorizedException();
        }
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) securityContextHolderStrategy.getContext()
                .getAuthentication();
        return UUID.fromString(jwtAuth.getToken().getSubject());
    }

    public Jwt issue(UserDetails userDetails, String type) {
        String userId = userDetails.getUsername();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority())
                .orElse("USER");
        return encodeToken(userId, role, type);
    }

    public Jwt issue(Jwt jwt, String type) {
        if (!"refresh".equals(jwt.getClaims().get("typ"))) {
            throw new UnauthorizedException("Invalid token type for refresh operation");
        }
        String userId = jwt.getSubject();
        String role = jwt.getClaims().getOrDefault("role", "USER").toString();
        return encodeToken(userId, role, type);
    }

    public Jwt decode(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            throw new UnauthorizedException();
        }
    }

    private Jwt encodeToken(String userId, String role, String type) {
        Instant now = Instant.now();
        Duration lifetime = "access".equals(type) ? accessTokenLifetime : refreshTokenLifetime;
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(userId)
                .issuedAt(now)
                .expiresAt(now.plus(lifetime))
                .claim("typ", type)
                .id(UUID.randomUUID().toString())
                .claim("role", role)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }
}