package pw.react.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.models.User;
import pw.react.backend.models.UserType;
import pw.react.backend.services.UserService;
import pw.react.backend.web.UserDto;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping(path = "/users")
@Profile({"jwt"})
public class JwtUserController {

    private static final Logger log = LoggerFactory.getLogger(JwtUserController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public JwtUserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void init() {
        userService.setPasswordEncoder(passwordEncoder);
    }

    @PutMapping(path = "/createOrUpdate")
    public ResponseEntity<Void> createUser(@RequestBody UserDto userDto) {
        User callingUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(callingUser.getUserType() != UserType.SUPER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        User user = UserDto.convertToUser(userDto);
        user = userService.validateAndSave(user);
        log.info("Password is going to be encoded.");
        userService.updatePassword(user, user.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/getCurrent")
    public ResponseEntity<UserDto> getCurrentUser() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setPassword("");
        return ResponseEntity.status(HttpStatus.OK).body(UserDto.valueFrom(user));
    }
}
