package andrehsvictor.mooral.user;

import org.mapstruct.Mapper;

import andrehsvictor.mooral.user.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User user);

}