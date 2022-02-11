package moby.testbackend.service;

import moby.testbackend.exception.UserAlreadyExistsException;
import moby.testbackend.model.User;
import moby.testbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User addUser(User user) throws UserAlreadyExistsException {
        if(isNull(userRepository.findByUsername(user.getUsername())))
            return userRepository.save(user);
        else
            throw new UserAlreadyExistsException("User already exists");
    }
}
