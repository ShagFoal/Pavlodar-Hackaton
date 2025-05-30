package blog.auth.auth.configs;

import blog.auth.auth.jwt.JwtRequestFilter;
import blog.auth.auth.user.UserEntity;
import blog.auth.auth.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Slf4j
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .cors(cors -> cors
                        .configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
//                        .configurationSource(request -> configuration()))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/register").permitAll()
                        .anyRequest().authenticated())
//                        .anyRequest().permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private CorsConfiguration configuration() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type","Access-Control-Allow-Origin"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return config;
    }

    @Bean
    public AuthenticationManager authenticationManager(UserService userService) {
        return authentication -> {
            String email = authentication.getName();
            String password = authentication.getCredentials().toString();
            UserEntity user = userService.findByEmail(email);
            if (user == null) throw new BadCredentialsException("Такого пользователя нет");
            log.warn("User password {}",user.getPassword());
            log.warn("Password {}",password);
            if (!userService.passwordMathes(password,user.getPassword())) throw new BadCredentialsException("Пароли не совпадают");
            return new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        };
    }
}
