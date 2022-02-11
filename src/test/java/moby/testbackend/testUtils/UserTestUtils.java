package moby.testbackend.testUtils;

import moby.testbackend.model.User;
import moby.testbackend.model.dto.LoginRequestDto;
import moby.testbackend.model.dto.UserDto;

public class UserTestUtils {

    public static User getUser() {
        return User.builder()
                .idUser(1)
                .username("mobydigital")
                .password("123")
                .email("moby@mobydigital.com")
                .build();
    }

    public static UserDto getUserDto() {
        return UserDto.builder()
                .id(1)
                .username("mobydigital")
                .build();
    }

    public static LoginRequestDto getLoginRequestDto() {
        return LoginRequestDto.builder()
                .username("mobydigital")
                .password("123")
                .build();
    }
}
