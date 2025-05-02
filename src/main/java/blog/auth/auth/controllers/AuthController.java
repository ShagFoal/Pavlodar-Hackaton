package blog.auth.auth.controllers;

import blog.auth.auth.jwt.JwtUtil;
import blog.auth.auth.user.UserDto;
import blog.auth.auth.user.UserEntity;
import blog.auth.auth.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        if (userService.findByUsername(userDto.getUsername()) != null)
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует");
        String token = jwtUtil.generateToken(userDto);
        UserEntity user = new UserEntity();
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());
        userService.save(user);
        log.info("Успешно зарегистрирован {}",user);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(),userDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.badRequest().body(ex.toString());
        }
        String token = jwtUtil.generateToken(userDto);
        log.info("Успешно вошел {}",userDto);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/status")
    public ResponseEntity<?> status(Principal principal) {
        if (principal == null) return ResponseEntity.badRequest().body("Не авторизован");
        return ResponseEntity.ok("Авторизован");
    }

    @GetMapping
    public String hello () {
        return "Hello world";
    }
}
