package andrehsvictor.mooral.account.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {
    private String id;
    private String username;
    private String email;
    private String pictureUrl;
    private String plan;
    private boolean emailVerified;
    private String createdAt;
    private String updatedAt;
}
