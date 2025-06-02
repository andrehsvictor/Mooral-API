package andrehsvictor.mooral.post;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import andrehsvictor.mooral.post.dto.CreatePostDto;
import andrehsvictor.mooral.post.dto.PostDto;
import andrehsvictor.mooral.post.dto.UpdatePostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Post management operations")
public class PostController {

    private final PostService postService;

    @Operation(summary = "Create a new post", description = "Creates a new post for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid post data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token is required", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/api/v1/posts")
    public ResponseEntity<PostDto> create(@RequestBody @Valid CreatePostDto createPostDto) {
        PostDto postDto = postService.toDto(postService.create(createPostDto));
        return ResponseEntity.status(201).body(postDto);
    }

    @Operation(summary = "Update an existing post", description = "Updates a post that belongs to the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid post data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Can only update your own posts", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/api/v1/posts/{postId}")
    public ResponseEntity<PostDto> update(
            @Parameter(description = "ID of the post to update", required = true) @PathVariable UUID postId,
            @RequestBody @Valid UpdatePostDto updatePostDto) {
        PostDto postDto = postService.toDto(postService.update(postId, updatePostDto));
        return ResponseEntity.ok(postDto);
    }

    @Operation(summary = "Get post by ID", description = "Retrieves a specific post by its ID. Increments view count if viewer is not the post owner")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @GetMapping("/api/v1/posts/{postId}")
    public ResponseEntity<PostDto> getById(
            @Parameter(description = "ID of the post to retrieve", required = true) @PathVariable UUID postId) {
        PostDto postDto = postService.toDto(postService.getById(postId));
        return ResponseEntity.ok(postDto);
    }

    @Operation(summary = "Get posts by username", description = "Retrieves all posts from a specific user with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/api/v1/users/{username}/posts")
    public Page<PostDto> getAllByUsername(
            @Parameter(description = "Username of the post owner", required = true) @PathVariable String username,
            @Parameter(description = "Pagination parameters (page, size, sort)") Pageable pageable) {
        return postService.getAllByUsername(username, pageable)
                .map(postService::toDto);
    }

    @Operation(summary = "Delete a post", description = "Deletes a post that belongs to the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Can only delete your own posts", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/api/v1/posts/{postId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the post to delete", required = true) @PathVariable UUID postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }
}