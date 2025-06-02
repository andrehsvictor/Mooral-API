package andrehsvictor.mooral.user;

import java.util.UUID;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import andrehsvictor.mooral.exception.ResourceNotFoundException;
import andrehsvictor.mooral.jwt.JwtService;
import andrehsvictor.mooral.user.dto.UserDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "users")
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @Cacheable(key = "'userById_' + #id")
    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", id));
    }

    @Cacheable(key = "'userByEmail_' + #email")
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Cacheable(key = "'userByProviderId_' + #providerId")
    public User getByProviderId(String providerId) {
        return userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "provider ID", providerId));
    }

    @Cacheable(key = "'existsByUsername_' + #username")
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Cacheable(key = "'existsByEmail_' + #email")
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Cacheable(key = "'userByToken_' + #token")
    public User getByToken(String token, TokenType type) {
        return switch (type) {
            case EMAIL_VERIFICATION -> userRepository.findByEmailVerificationToken(token)
                    .orElseThrow(() -> new ResourceNotFoundException("Email verification token not found"));
            case PASSWORD_RESET -> userRepository.findByResetPasswordToken(token)
                    .orElseThrow(() -> new ResourceNotFoundException("Password reset token not found"));
        };
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "'userByUsername_' + #username", condition = "@userRepository.existsByUsername(#username)")
    })
    public void incrementMuralVisitsCount(String username) {
        UUID currentUserId = jwtService.getCurrentUserId();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        if (user.getId().equals(currentUserId)) {
            return;
        }
        user.incrementMuralVisitsCount();
        userRepository.save(user);
    }

    public UserDto toDto(User user) {
        return userMapper.userToUserDto(user);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "'userById_' + #user.id", condition = "#user.id != null"),
            @CacheEvict(key = "'userByEmail_' + #user.email", condition = "#user.email != null"),
            @CacheEvict(key = "'userByProviderId_' + #user.providerId", condition = "#user.providerId != null"),
            @CacheEvict(key = "'existsByUsername_' + #user.username", condition = "#user.username != null"),
            @CacheEvict(key = "'existsByEmail_' + #user.email", condition = "#user.email != null")
    })
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "'userById_' + #id"),
            @CacheEvict(key = "'filters_*'", allEntries = true),
            @CacheEvict(cacheNames = "accounts", allEntries = true)
    })
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

}