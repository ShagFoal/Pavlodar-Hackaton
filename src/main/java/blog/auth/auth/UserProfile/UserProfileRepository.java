package blog.auth.auth.UserProfile;

import blog.auth.auth.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity,Long> {
    UserProfileEntity findByUser(UserEntity user);
}
