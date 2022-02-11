package moby.testbackend.controller;

import moby.testbackend.exception.UserAlreadyExistsException;
import moby.testbackend.model.User;
import moby.testbackend.model.utils.ResponseMessage;
import moby.testbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static moby.testbackend.utils.EntityURLBuilder.buildURL;
import static moby.testbackend.utils.ResponseUtil.messageResponse;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> addUser(@RequestBody User user) throws UserAlreadyExistsException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(buildURL("users", userService.addUser(user).getIdUser()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(messageResponse("User has been created"));
    }
}
