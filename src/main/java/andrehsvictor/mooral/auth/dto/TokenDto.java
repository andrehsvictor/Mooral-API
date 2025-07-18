package andrehsvictor.mooral.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
    private String accessToken;
    private String refreshToken;

    @Builder.Default
    private String type = "Bearer";
    
    private Long expiresIn;
}
