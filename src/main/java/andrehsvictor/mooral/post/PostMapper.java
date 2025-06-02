package andrehsvictor.mooral.post;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import andrehsvictor.mooral.post.dto.CreatePostDto;
import andrehsvictor.mooral.post.dto.PostDto;
import andrehsvictor.mooral.post.dto.UpdatePostDto;
import andrehsvictor.mooral.user.UserMapper;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface PostMapper {

    PostDto postToPostDto(Post post);

    Post createPostDtoToPost(CreatePostDto createPostDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Post updatePostFromUpdatePostDto(
            UpdatePostDto updatePostDto,
            @MappingTarget Post post);

    @AfterMapping
    default void afterMapping(UpdatePostDto updatePostDto, @MappingTarget Post post) {
        if (updatePostDto.getContent() != null && updatePostDto.getContent().isBlank()) {
            post.setContent(null);
        }
    }

}
