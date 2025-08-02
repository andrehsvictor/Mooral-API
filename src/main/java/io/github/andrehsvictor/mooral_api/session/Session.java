package io.github.andrehsvictor.mooral_api.session;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@RedisHash("session")
@EqualsAndHashCode(of = "id")
public class Session implements Serializable {

    private static final long serialVersionUID = -7636091897673895051L;

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Indexed
    private UUID userId;

    private String ipAddress;
    private String userAgent;
    private String deviceName;
    private String deviceType;

    @Builder.Default
    private Long lastAccessedAt = System.currentTimeMillis();

    @Builder.Default
    private Long createdAt = System.currentTimeMillis();

    private Long expiresAt;

}
