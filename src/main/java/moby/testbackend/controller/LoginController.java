package moby.testbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import moby.testbackend.model.User;
import moby.testbackend.model.dto.LoginRequestDto;
import moby.testbackend.model.dto.LoginResponseDto;
import moby.testbackend.model.dto.UserDto;
import moby.testbackend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static moby.testbackend.utils.JWTUtil.JWT_SECRET;

@RestController
@RequestMapping("/")
public class LoginController {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public LoginController(UserService userService, ObjectMapper objectMapper, ModelMapper modelMapper){
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        User user = userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        if(user != null) {
            UserDto userDto = modelMapper.map(user, UserDto.class);
            return ResponseEntity.ok(LoginResponseDto.builder().token(generateToken(userDto)).build());
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private String generateToken(UserDto userDto){
        try {
            return Jwts.builder()
                    .setId("JWT")
                    .setSubject(userDto.getUsername())
                    .claim("user", objectMapper.writeValueAsString(userDto))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1800000))
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET.getBytes()).compact();
        }
        catch (JsonProcessingException e) {
            return "dummy";
        }
    }
}
