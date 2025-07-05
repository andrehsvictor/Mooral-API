package andrehsvictor.mooral.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import andrehsvictor.mooral.auth.dto.CredentialsDto;
import andrehsvictor.mooral.auth.dto.RefreshTokenDto;
import andrehsvictor.mooral.auth.dto.RevokeTokenDto;
import andrehsvictor.mooral.auth.dto.TokenDto;
import andrehsvictor.mooral.common.jwt.JwtService;
import andrehsvictor.mooral.common.revokedtoken.RevokedTokenService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtService jwtService;
    private final RevokedTokenService revokedTokenService;
    private final AuthenticationService authenticationService;

    public TokenDto request(CredentialsDto credentialsDto) {
        Authentication authentication = authenticationService.authenticate(credentialsDto.getUsername(),
                credentialsDto.getPassword());
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Jwt accessToken = jwtService.encode(userDetails, "access", null);
        Jwt refreshToken = jwtService.encode(userDetails, "refresh", null);
        Long expiresIn = accessToken.getExpiresAt().getEpochSecond() - accessToken.getIssuedAt().getEpochSecond();
        return TokenDto.builder()
                .accessToken(accessToken.getTokenValue())
                .refreshToken(refreshToken.getTokenValue())
                .expiresIn(expiresIn)
                .build();
    }

    public TokenDto refresh(RefreshTokenDto refreshTokenDto) {
        Jwt refreshToken = jwtService.decode(refreshTokenDto.getRefreshToken());
        revokedTokenService.revoke(refreshToken);
        Jwt accessToken = jwtService.encode(refreshToken, "access");
        Jwt newRefreshToken = jwtService.encode(refreshToken, "refresh");
        Long expiresIn = accessToken.getExpiresAt().getEpochSecond() - accessToken.getIssuedAt().getEpochSecond();
        return TokenDto.builder()
                .accessToken(accessToken.getTokenValue())
                .refreshToken(newRefreshToken.getTokenValue())
                .expiresIn(expiresIn)
                .build();
    }

    public void revoke(RevokeTokenDto revokeTokenDto) {
        Jwt jwt = jwtService.decode(revokeTokenDto.getToken());
        revokedTokenService.revoke(jwt);
    }

}
