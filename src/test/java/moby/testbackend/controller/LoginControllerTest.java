package moby.testbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import moby.testbackend.model.dto.LoginResponseDto;
import moby.testbackend.model.dto.UserDto;
import moby.testbackend.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static moby.testbackend.testUtils.UserTestUtils.getLoginRequestDto;
import static moby.testbackend.testUtils.UserTestUtils.getUser;
import static moby.testbackend.testUtils.UserTestUtils.getUserDto;
import static moby.testbackend.utils.JWTUtil.JWT_SECRET;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginControllerTest {

    private UserService userService;
    private ObjectMapper objectMapper;
    private ModelMapper modelMapper;
    private LoginController loginController;

    @Before
    public void setUp() {
        userService = mock(UserService.class);
        objectMapper = mock(ObjectMapper.class);
        modelMapper = mock(ModelMapper.class);
        loginController = new LoginController(userService, objectMapper, modelMapper);
    }

    @Test
    public void loginOkTest() throws JsonProcessingException {
        when(userService.login("mobydigital", "123")).thenReturn(getUser());
        when(modelMapper.map(getUser(), UserDto.class)).thenReturn(getUserDto());
        when(objectMapper.writeValueAsString(getUserDto())).thenReturn(anyString());

        String token = Jwts.builder()
                .setId("JWT")
                .setSubject(getUserDto().getUsername())
                .claim("user", objectMapper.writeValueAsString(getUserDto()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1800000))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET.getBytes()).compact();

        ResponseEntity<LoginResponseDto> response = loginController.login(getLoginRequestDto());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token.length(), response.getBody().getToken().length());
        verify(userService, times(1)).login("mobydigital", "123");
    }

    @Test
    public void loginUnauthorizedTest() {
        when(userService.login("mobydigital", "123")).thenReturn(null);

        ResponseEntity<LoginResponseDto> response = loginController.login(getLoginRequestDto());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
