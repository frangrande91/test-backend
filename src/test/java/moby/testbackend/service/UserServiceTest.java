package moby.testbackend.service;

import moby.testbackend.exception.UserAlreadyExistsException;
import moby.testbackend.model.User;
import moby.testbackend.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import static moby.testbackend.testUtils.UserTestUtils.getUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    public void loginOkTest() {
        when(userRepository.findByUsernameAndPassword("mobydigital", "123")).thenReturn(getUser());

        User user = userService.login("mobydigital", "123");

        assertEquals(getUser(), user);
        verify(userRepository, times(1)).findByUsernameAndPassword("mobydigital", "123");
    }

    @Test
    public void addUserOkTest() throws UserAlreadyExistsException {
        when(userRepository.findByUsername("mobydigital")).thenReturn(null);
        when(userRepository.save(getUser())).thenReturn(getUser());

        User user = userService.addUser(getUser());

        assertEquals(getUser(), user);
        verify(userRepository, times(1)).findByUsername("mobydigital");
        verify(userRepository, times(1)).save(getUser());
    }

    @Test
    public void addUserAlreadyExistsTest() {
        when(userRepository.findByUsername("mobydigital")).thenReturn(getUser());

        assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(getUser()));
        verify(userRepository, times(1)).findByUsername("mobydigital");
        verify(userRepository, times(0)).save(getUser());
    }
}
