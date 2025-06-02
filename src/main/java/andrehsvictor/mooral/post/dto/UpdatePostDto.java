package andrehsvictor.mooral.post.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePostDto {

    @Size(min = 1, max = 500, message = "Content must be between 1 and 500 characters")
    private String content;

}
