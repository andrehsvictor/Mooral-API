package io.github.andrehsvictor.mooral_api.session;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    @Value("${io.github.andrehsvictor.mooral-api.jwt.refresh-token.lifespan:1h}")
    private Duration lifespan = Duration.ofHours(1);

    public Session create(Authentication authentication, HttpServletRequest request) {
        Session session = Session.builder()
                .userId(UUID.fromString(authentication.getName()))
                .ipAddress(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .deviceName(request.getHeader("Device-Name"))
                .deviceType(request.getHeader("Device-Type"))
                .expiresAt(System.currentTimeMillis() + lifespan.toMillis())
                .build();
        return sessionRepository.save(session);
    }

}
