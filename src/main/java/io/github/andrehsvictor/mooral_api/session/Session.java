package io.github.andrehsvictor.mooral_api.session;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("session")
@EqualsAndHashCode(of = { "id" })
public class Session implements Serializable {

    private static final long serialVersionUID = -4933111494402809678L;

    @Id
    private UUID id;

    @Indexed
    private UUID jti;

    @Indexed
    private UUID userId;

    private String ipAddress;
    private String userAgent;
    private Long createdAt;
    private Long expiresAt;

}
