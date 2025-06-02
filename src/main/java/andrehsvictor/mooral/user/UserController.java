package andrehsvictor.mooral.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import andrehsvictor.mooral.user.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Users", description = "User profile and interaction operations")
public class UserController {

    private final UserService userService;

    @GetMapping("/api/v1/users/{username}")
    public ResponseEntity<UserDto> getByUsername(
            @PathVariable String username) {
        UserDto userDto = userService.toDto(userService.getByUsername(username));
        return ResponseEntity.ok(userDto);
    }
}