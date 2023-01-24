package pw.react.backend.web;

import org.hibernate.usertype.UserType;
import pw.react.backend.models.User;

import javax.validation.constraints.Email;

public record UserDto(Long id, String username, String password, @Email String email, String userType) {

    public static UserDto valueFrom(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getUserTypeString());
    }

    public static User convertToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.id());
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setUserType(userDto.userType);
        return user;
    }
}
