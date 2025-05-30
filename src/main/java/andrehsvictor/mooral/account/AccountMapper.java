package andrehsvictor.mooral.account;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import andrehsvictor.mooral.account.dto.AccountDto;
import andrehsvictor.mooral.account.dto.CreateAccountDto;
import andrehsvictor.mooral.account.dto.UpdateAccountDto;
import andrehsvictor.mooral.user.User;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto userToAccountDto(User user);

    User createAccountDtoToUser(CreateAccountDto createAccountDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUserFromUpdateAccountDto(UpdateAccountDto updateAccountDto, @MappingTarget User user);

    @AfterMapping
    default void resetEmptyFields(UpdateAccountDto dto, @MappingTarget User user) {
        if (isEmptyString(dto.getPictureUrl())) {
            user.setPictureUrl(null);
        }
    }

    default boolean isEmptyString(String value) {
        return value != null && value.isBlank();
    }

}