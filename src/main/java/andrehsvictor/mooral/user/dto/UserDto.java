package andrehsvictor.mooral.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String id;
    private String username;
    private String pictureUrl;
    private String createdAt;
}