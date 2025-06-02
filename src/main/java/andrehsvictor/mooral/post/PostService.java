package andrehsvictor.mooral.post;

import java.util.UUID;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import andrehsvictor.mooral.exception.ForbiddenOperationException;
import andrehsvictor.mooral.exception.ResourceNotFoundException;
import andrehsvictor.mooral.image.ImageService;
import andrehsvictor.mooral.jwt.JwtService;
import andrehsvictor.mooral.post.dto.CreatePostDto;
import andrehsvictor.mooral.post.dto.PostDto;
import andrehsvictor.mooral.post.dto.UpdatePostDto;
import andrehsvictor.mooral.user.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "posts")
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final ImageService imageService;
    private final JwtService jwtService;
    private final UserService userService;

    public PostDto toDto(Post post) {
        return postMapper.postToPostDto(post);
    }

    @Cacheable(key = "'byUsername_' + #username + '_page_' + #pageable.pageNumber + '_size_' + #pageable.pageSize")
    public Page<Post> getAllByUsername(String username, Pageable pageable) {
        return postRepository.findAllByUserUsername(username, pageable);
    }

    @Cacheable(key = "'postById_' + #postId")
    public Post getById(UUID postId) {
        Post post = findPostById(postId);
        incrementViewsIfNotOwner(post);
        return post;
    }

    @Caching(evict = {
            @CacheEvict(key = "'postById_' + #result.id"),
            @CacheEvict(key = "'byUsername_' + #result.user.username + '_*'", allEntries = true)
    })
    public Post create(CreatePostDto createPostDto) {
        Post post = postMapper.createPostDtoToPost(createPostDto);

        UUID currentUserId = getCurrentUserId();
        post.setUser(userService.getById(currentUserId));

        return postRepository.save(post);
    }

    @Caching(evict = {
            @CacheEvict(key = "'postById_' + #postId"),
            @CacheEvict(key = "'byUsername_' + @userService.getById(@jwtService.getCurrentUserId()).username + '_*'", allEntries = true)
    })
    public Post update(UUID postId, UpdatePostDto updatePostDto) {
        Post post = findPostById(postId);
        validateOwnership(post);

        postMapper.updatePostFromUpdatePostDto(updatePostDto, post);
        Post savedPost = postRepository.save(post);

        return savedPost;
    }

    @Caching(evict = {
            @CacheEvict(key = "'postById_' + #postId"),
            @CacheEvict(key = "'byUsername_' + @userService.getById(@jwtService.getCurrentUserId()).username + '_*'", allEntries = true)
    })
    public void delete(UUID postId) {
        Post post = findPostById(postId);
        validateOwnership(post);

        String imageUrl = post.getImageUrl();
        postRepository.delete(post);
        deleteImageIfExists(imageUrl);
    }

    private UUID getCurrentUserId() {
        return jwtService.getCurrentUserId();
    }

    private Post findPostById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
    }

    private void validateOwnership(Post post) {
        UUID currentUserId = getCurrentUserId();
        if (!post.getUserId().equals(currentUserId)) {
            throw new ForbiddenOperationException("You can only modify your own posts");
        }
    }

    private void incrementViewsIfNotOwner(Post post) {
        UUID currentUserId = getCurrentUserId();
        if (!post.getUserId().equals(currentUserId)) {
            post.setViewsCount(post.getViewsCount() + 1);
            postRepository.save(post);
        }
    }

    private void deleteImageIfExists(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            imageService.delete(imageUrl);
        }
    }
}