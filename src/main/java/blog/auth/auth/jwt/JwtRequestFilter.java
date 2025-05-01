package blog.auth.auth.jwt;

import blog.auth.auth.user.UserEntity;
import blog.auth.auth.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt;
        String username = null;
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            jwt = headerAuth.substring(7);
            try {
                username = jwtUtil.getUsername(jwt);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Jwt закончился");
                return;
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserEntity userDetails = userService.loadUserByUsername(username);
            if (userDetails == null) throw new BadCredentialsException("Такого пользователя не существует");
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,
                    null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request,response);
    }
}
