package io.github.andrehsvictor.mooral_api.session;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, UUID> {

    Optional<Session> findBySub(UUID sub);

    Optional<Session> findByJti(UUID jti);

}
