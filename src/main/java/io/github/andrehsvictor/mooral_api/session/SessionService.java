package io.github.andrehsvictor.mooral_api.session;

import java.util.UUID;

import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public Session create(JwtClaimAccessor claims, String ip, String agent) {
        Session session = Session.builder()
                .id(UUID.fromString(claims.getClaimAsString("sid")))
                .userId(UUID.fromString(claims.getSubject()))
                .jti(UUID.fromString(claims.getId()))
                .createdAt(claims.getIssuedAt().toEpochMilli())
                .expiresAt(claims.getExpiresAt().toEpochMilli())
                .ipAddress(ip)
                .userAgent(agent)
                .build();
        return sessionRepository.save(session);
    }

    public boolean isValid(UUID sid) {
        return sessionRepository.findById(sid)
                .map(Session::getExpiresAt)
                .map(expiresAt -> expiresAt > System.currentTimeMillis())
                .orElse(false);
    }

}
