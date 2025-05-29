package blog.auth.auth.UserProfile;

import lombok.Data;
import java.util.List;

@Data
public class UserProfileDto {
    private List<String> skills;
    private List<String> interests;
    private String educationLevel;
    private String preferredRegion;
}

