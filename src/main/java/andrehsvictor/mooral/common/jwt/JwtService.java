package andrehsvictor.mooral.common.jwt;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    public boolean isPresent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication instanceof JwtAuthenticationToken;
    }

    public UUID getUserId() {
        if (!isPresent()) {
            // TODO: throw an exception or handle the case where the JWT is not present
        }
        JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        String userId = jwtAuthentication.getToken().getSubject();
        return UUID.fromString(userId);
    }
}
