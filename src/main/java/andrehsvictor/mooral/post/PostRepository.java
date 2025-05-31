package andrehsvictor.mooral.post;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, UUID> {

    Page<Post> findAllByUserId(UUID userId, Pageable pageable);

}
