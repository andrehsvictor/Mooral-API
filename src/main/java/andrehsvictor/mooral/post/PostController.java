package andrehsvictor.mooral.post;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import andrehsvictor.mooral.post.dto.CreatePostDto;
import andrehsvictor.mooral.post.dto.PostDto;
import andrehsvictor.mooral.post.dto.UpdatePostDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/v1/posts")
    public ResponseEntity<PostDto> create(@RequestBody @Valid CreatePostDto createPostDto) {
        PostDto postDto = postService.toDto(postService.create(createPostDto));
        return ResponseEntity.status(201).body(postDto);
    }

    @PutMapping("/api/v1/posts/{postId}")
    public ResponseEntity<PostDto> update(@PathVariable UUID postId,
            @RequestBody @Valid UpdatePostDto updatePostDto) {
        PostDto postDto = postService.toDto(postService.update(postId, updatePostDto));
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/api/v1/posts/{postId}")
    public ResponseEntity<PostDto> getById(@PathVariable UUID postId) {
        PostDto postDto = postService.toDto(postService.getById(postId));
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/api/v1/users/{username}/posts")
    public Page<PostDto> getAllByUsername(@PathVariable String username,
            Pageable pageable) {
        return postService.getAllByUsername(username, pageable)
                .map(postService::toDto);
    }
}
