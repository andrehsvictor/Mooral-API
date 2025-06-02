package andrehsvictor.mooral.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/api/v1/users/{username}/visits")
    public ResponseEntity<Void> incrementVisitCount(@PathVariable String username) {
        userService.incrementMuralVisitsCount(username);
        return ResponseEntity.noContent().build();
    }
    
}
