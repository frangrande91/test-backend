package moby.testbackend.controller;

import moby.testbackend.exception.UserAlreadyExistsException;
import moby.testbackend.model.utils.ResponseMessage;
import moby.testbackend.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

import static moby.testbackend.testUtils.UserTestUtils.getUser;
import static moby.testbackend.utils.EntityURLBuilder.buildURL;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @Before
    public void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void addUserOkTest() throws UserAlreadyExistsException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(userService.addUser(getUser())).thenReturn(getUser());

        ResponseEntity<ResponseMessage> response = userController.addUser(getUser());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(buildURL("users", getUser().getIdUser()).toString()
                , Objects.requireNonNull(response.getHeaders().get("Location")).get(0));
        verify(userService, times(1)).addUser(getUser());
    }
}
