package io.github.andrehsvictor.mooral_api.session;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, UUID> {

    Optional<Session> findByUserId(UUID userId);

    Page<Session> findAllByUserId(UUID userId, Pageable pageable);

}
