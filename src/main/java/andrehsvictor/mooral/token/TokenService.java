package andrehsvictor.mooral.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import andrehsvictor.mooral.google.GoogleAuthenticationService;
import andrehsvictor.mooral.token.dto.IdTokenDto;
import andrehsvictor.mooral.token.dto.RefreshTokenDto;
import andrehsvictor.mooral.token.dto.RevokeTokenDto;
import andrehsvictor.mooral.token.dto.TokenDto;
import andrehsvictor.mooral.token.dto.UsernamePasswordDto;
import andrehsvictor.mooral.authentication.AuthenticationService;
import andrehsvictor.mooral.jwt.JwtService;
import andrehsvictor.mooral.jwt.JwtType;
import andrehsvictor.mooral.revokedtoken.RevokedTokenService;
import andrehsvictor.mooral.security.UserDetailsImpl;
import andrehsvictor.mooral.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RevokedTokenService revokedTokenService;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final GoogleAuthenticationService googleAuthenticationService;

    public TokenDto request(UsernamePasswordDto credentials) {
        Authentication authentication = authenticationService.authenticate(
                credentials.getUsername(),
                credentials.getPassword());

        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        Jwt accessToken = jwtService.issue(user, JwtType.ACCESS);
        Jwt refreshToken = jwtService.issue(user, JwtType.REFRESH);

        return buildTokenResponse(accessToken, refreshToken);
    }

    public TokenDto refresh(RefreshTokenDto refreshTokenDto) {
        Jwt oldRefreshToken = jwtService.decode(refreshTokenDto.getRefreshToken());

        Jwt accessToken = jwtService.issue(oldRefreshToken, JwtType.ACCESS);
        Jwt newRefreshToken = jwtService.issue(oldRefreshToken, JwtType.REFRESH);
        revokedTokenService.revoke(oldRefreshToken);

        return buildTokenResponse(accessToken, newRefreshToken);
    }

    public void revoke(RevokeTokenDto revokeTokenDto) {
        Jwt token = jwtService.decode(revokeTokenDto.getToken());
        revokedTokenService.revoke(token);
    }

    public TokenDto google(IdTokenDto idTokenDto) {
        Authentication authentication = googleAuthenticationService.authenticate(idTokenDto.getIdToken());
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        Jwt accessToken = jwtService.issue(user, JwtType.ACCESS);
        Jwt refreshToken = jwtService.issue(user, JwtType.REFRESH);

        return buildTokenResponse(accessToken, refreshToken);
    }

    private TokenDto buildTokenResponse(Jwt accessToken, Jwt refreshToken) {
        long expiresIn = accessToken.getExpiresAt().getEpochSecond() - accessToken.getIssuedAt().getEpochSecond();

        return TokenDto.builder()
                .accessToken(accessToken.getTokenValue())
                .refreshToken(refreshToken.getTokenValue())
                .expiresIn(expiresIn)
                .build();
    }
}