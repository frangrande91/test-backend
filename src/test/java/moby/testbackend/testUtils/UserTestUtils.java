package moby.testbackend.testUtils;

import moby.testbackend.model.User;

public class UserTestUtils {

    public static User getUser() {
        return User.builder()
                .idUser(1)
                .username("mobydigital")
                .password("123")
                .email("moby@mobydigital.com")
                .build();
    }
}
