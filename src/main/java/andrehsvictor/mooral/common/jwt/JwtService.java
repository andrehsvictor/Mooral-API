package andrehsvictor.mooral.common.jwt;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import andrehsvictor.mooral.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${mooral.jwt.access-token.lifetime:15m}")
    private Duration accessTokenLifetime;

    @Value("${mooral.jwt.refresh-token.lifetime:1h}")
    private Duration refreshTokenLifetime;

    @Value("${mooral.jwt.action-token.email-verification.lifetime:1h}")
    private Duration emailVerificationTokenLifetime;

    @Value("${mooral.jwt.action-token.password-reset.lifetime:1h}")
    private Duration passwordResetTokenLifetime;

    @Value("${mooral.jwt.action-token.email-change.lifetime:1h}")
    private Duration emailChangeTokenLifetime;

    public UUID getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
            throw new UnauthorizedException("No valid JWT authentication found");
        }
        return UUID.fromString(jwtAuthenticationToken.getToken().getSubject());
    }

    public Jwt decode(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new UnauthorizedException("Invalid token: " + e.getMessage());
        }
    }

    public Jwt encode(UserDetails userDetails, String type, Map<String, Object> extraClaims) {
        return createToken(userDetails.getUsername(), type, extraClaims);
    }

    public Jwt encode(Jwt refreshToken, String type) {
        if (!"refresh".equals(refreshToken.getClaimAsString("typ"))) {
            throw new UnauthorizedException("Can only issue new tokens with a refresh token");
        }
        return createToken(refreshToken.getSubject(), type, refreshToken.getClaims());
    }

    private Jwt createToken(String subject, String type, Map<String, Object> claims) {
        Instant now = Instant.now();
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plus(getTokenLifetime(type)))
                .subject(subject)
                .claim("typ", type);
        
        if (claims != null) {
            claims.forEach(builder::claim);
        }
        
        return jwtEncoder.encode(JwtEncoderParameters.from(builder.build()));
    }

    private Duration getTokenLifetime(String type) {
        return switch (type) {
            case "access" -> accessTokenLifetime;
            case "refresh" -> refreshTokenLifetime;
            case "verify-email" -> emailVerificationTokenLifetime;
            case "reset-password" -> passwordResetTokenLifetime;
            case "change-email" -> emailChangeTokenLifetime;
            default -> throw new IllegalArgumentException("Unsupported token type: " + type);
        };
    }

}
