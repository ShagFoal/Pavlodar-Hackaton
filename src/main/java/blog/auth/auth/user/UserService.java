package blog.auth.auth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository repo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserEntity findByUsername(String username) {
        return repo.findByUsername(username).orElse(null);
    }

    public UserEntity save(UserEntity user) {
        if (user != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            repo.save(user);
        }
        return null;
    }

    public boolean passwordMathes(String password1, String password2) {
        return passwordEncoder.matches(password1,password2);
    }

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username);
    }
}
