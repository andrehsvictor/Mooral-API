package io.github.andrehsvictor.mooral_api.session;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import io.github.andrehsvictor.mooral_api.common.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final JwtService jwtService;

    public void create(Jwt jwt, HttpServletRequest request) {
        Session session = Session.builder()
                .id(UUID.fromString(jwt.getClaimAsString("sid")))
                .jti(UUID.fromString(jwt.getId()))
                .userId(UUID.fromString(jwt.getSubject()))
                .ipAddress(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .createdAt(jwt.getIssuedAt().toEpochMilli())
                .expiresAt(jwt.getExpiresAt().toEpochMilli())
                .build();
        sessionRepository.save(session);
    }

    public Page<Session> findAllByCurrentUser(Pageable pageable) {
        UUID userId = jwtService.getCurrentUserId();
        return sessionRepository.findAllByUserId(userId, pageable);
    }

    public Page<Session> findAllByUserId(UUID userId, Pageable pageable) {
        return sessionRepository.findAllByUserId(userId, pageable);
    }

}
