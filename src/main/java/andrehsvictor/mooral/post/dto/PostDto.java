package andrehsvictor.mooral.post.dto;

import andrehsvictor.mooral.user.dto.UserDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDto {

    private String id;
    private UserDto user;
    private String content;
    private String imageUrl;
    private Integer viewsCount;
    private String createdAt;
    private String updatedAt;
    
}