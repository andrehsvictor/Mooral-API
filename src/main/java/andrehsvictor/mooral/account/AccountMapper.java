package andrehsvictor.mooral.account;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import andrehsvictor.mooral.account.dto.AccountDto;
import andrehsvictor.mooral.user.User;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "plan", expression = "java(user.getPlan().name())")
    AccountDto userToAccountDto(User user);
}
