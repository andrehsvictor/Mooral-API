package io.github.andrehsvictor.mooral_api.common.tokenrevocation;

import java.time.Duration;
import java.time.Instant;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenRevocationService {

    private final RedisTemplate<String, Long> redisTemplate;
    private final static String REVOKED_TOKEN_KEY_PREFIX = "revoked_token:";

    public void revoke(Jwt jwt) {
        String jti = jwt.getId();
        Duration ttl = Duration.between(Instant.now(), jwt.getExpiresAt());
        String key = REVOKED_TOKEN_KEY_PREFIX + jti;
        redisTemplate.opsForValue().set(key, 1L, ttl);
    }

    public boolean isRevoked(Jwt jwt) {
        String jti = jwt.getId();
        String key = REVOKED_TOKEN_KEY_PREFIX + jti;
        return redisTemplate.hasKey(key);
    }

}
