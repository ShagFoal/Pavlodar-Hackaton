package blog.auth.auth.controllers;

import blog.auth.auth.jwt.JwtUtil;
import blog.auth.auth.user.UserDto;
import blog.auth.auth.user.UserEntity;
import blog.auth.auth.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
        if (userService.findByEmail(userDto.getEmail()) != null)
            return ResponseEntity.badRequest().body("Пользователь с такой почтой уже существует");
        String token = jwtUtil.generateToken(userDto);
        UserEntity user = new UserEntity();
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        userService.save(user);
        log.info("Успешно зарегистрирован {}",user);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(),userDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.badRequest().body(ex.toString());
        }
        String token = jwtUtil.generateToken(userDto);
        log.info("Успешно вошел {}",userDto);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/status")
    public ResponseEntity<?> status() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Не авторизован");
        }
        UserEntity user = (UserEntity) auth.getPrincipal();
        return ResponseEntity.ok("Авторизован: " + user.getUsername());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> test(@PathVariable Long id) {
        UserEntity user = userService.findById(id);
        return ResponseEntity.ok("Авторизован: " + user.getUsername());
    }

}
