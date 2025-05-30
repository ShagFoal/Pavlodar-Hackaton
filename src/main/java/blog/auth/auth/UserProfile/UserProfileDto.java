package blog.auth.auth.UserProfile;

import blog.auth.auth.user.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;

@Data
@NoArgsConstructor
public class UserProfileDto {
    private List<String> skills;
    private List<String> interests;
    private String educationLevel;
    private String preferredRegion;
    private String username;

    public UserProfileDto(UserProfileEntity userProfileEntity, UserEntity userEntity) {
        if (userProfileEntity != null) {
            this.skills = userProfileEntity.getSkills();
            this.interests = userProfileEntity.getInterests();
            this.educationLevel = userProfileEntity.getEducationLevel();
            this.preferredRegion = userProfileEntity.getPreferredRegion();
        }
        this.username = userEntity.getUsername();
    }
}

