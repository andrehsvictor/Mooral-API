package andrehsvictor.mooral.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Users", description = "User profile and interaction operations")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Increment user profile visit count", description = "Increments the visit counter for a user's profile page. Only counts visits from other users, not self-visits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Visit count incremented successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/api/v1/users/{username}/visits")
    public ResponseEntity<Void> incrementVisitCount(
            @Parameter(description = "Username of the profile being visited", required = true) @PathVariable String username) {
        userService.incrementMuralVisitsCount(username);
        return ResponseEntity.noContent().build();
    }
}