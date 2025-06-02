package andrehsvictor.mooral.post.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDto {

    private String id;
    private String content;
    private String imageUrl;
    private Integer viewsCount;
    private String createdAt;
    private String updatedAt;
    
}