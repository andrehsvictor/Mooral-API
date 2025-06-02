package andrehsvictor.mooral.post.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePostDto {
    
    @Size(min = 1, max = 500, message = "Content must be between 1 and 500 characters")
    private String content;

    @Pattern(regexp = "^(https?://.*\\.(?:png|jpg|jpeg)$)", message = "Image URL must be a valid URL ending with .png, .jpg, or .jpeg")
    @Size(min = 1, max = 255, message = "Image URL must be between 1 and 255 characters")
    private String imageUrl;
    
}
