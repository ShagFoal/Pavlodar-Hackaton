package blog.auth.auth.UserProfile;

import blog.auth.auth.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository repo;

    public UserProfileEntity findByUser(UserEntity user) {
        return repo.findByUser(user);
    }

}
