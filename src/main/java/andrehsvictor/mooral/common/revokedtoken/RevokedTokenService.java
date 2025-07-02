package andrehsvictor.mooral.common.revokedtoken;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RevokedTokenService {

    private final RedisTemplate<String, Integer> redisTemplate;

    private static final String PREFIX = "revoked_token:";

    public void revoke(Jwt jwt) {
        String jti = jwt.getId();
        String key = PREFIX + jti;
        Long ttl = jwt.getExpiresAt().toEpochMilli() - System.currentTimeMillis();
        if (ttl > 0) {
            redisTemplate.opsForValue().set(key, 0, Duration.ofMillis(ttl));
        }
    }

    public boolean isRevoked(Jwt jwt) {
        String jti = jwt.getId();
        return redisTemplate.hasKey(jti);
    }
}
