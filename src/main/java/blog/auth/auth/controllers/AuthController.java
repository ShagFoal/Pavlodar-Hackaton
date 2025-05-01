package blog.auth.auth.controllers;

import blog.auth.auth.jwt.JwtUtil;
import blog.auth.auth.user.UserDto;
import blog.auth.auth.user.UserEntity;
import blog.auth.auth.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public String hello(@RequestBody UserDto userDto) {
        String token = jwtUtil.generateToken(userDto);
        UserEntity user = new UserEntity();
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());
        userService.save(user);
        return token;
    }
}
