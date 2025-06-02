package andrehsvictor.mooral.account.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateAccountDto {

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers and underscores")
    private String username;

    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Pattern(regexp = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$", message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^(https?://.*\\.(?:png|jpg|jpeg)$)", message = "Picture URL must be a valid URL ending with .png, .jpg, or .jpeg")
    @Size(max = 255, message = "Picture URL must not exceed 255 characters")
    private String pictureUrl;
}
