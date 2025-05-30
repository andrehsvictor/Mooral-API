package andrehsvictor.mooral.account.dto;

import andrehsvictor.mooral.account.EmailSendingAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendActionEmailDto {

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$", message = "Email is not valid")
    private String email;

    @NotNull(message = "Action is required")
    private EmailSendingAction action;

    @NotBlank(message = "URL is required")
    @Pattern(regexp = "^(http|https)://.*$", message = "URL must start with http:// or https://")
    @Size(max = 255, message = "URL must be less than 255 characters")
    private String url;

}