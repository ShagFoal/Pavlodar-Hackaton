package blog.auth.auth.controllers;

import blog.auth.auth.UserProfile.UserProfileDto;
import blog.auth.auth.UserProfile.UserProfileEntity;
import blog.auth.auth.UserProfile.UserProfileService;
import blog.auth.auth.user.UserEntity;
import blog.auth.auth.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final UserProfileService userProfileService;
    private final UserService userService;

    @PostMapping("/setProfile")
    public String setProfile(@RequestBody UserProfileDto profileDto, Authentication authentication) {

        UserEntity user = (UserEntity) authentication.getPrincipal();

        UserProfileEntity profile = user.getUserProfile();
        if (profile == null) {
            profile = new UserProfileEntity();
            profile.setUser(user);
        }

        profile.setSkills(profileDto.getSkills());
        profile.setInterests(profileDto.getInterests());
        profile.setEducationLevel(profileDto.getEducationLevel());
        profile.setPreferredRegion(profileDto.getPreferredRegion());

        user.setUserProfile(profile);

        userService.save(user);
        return "Профиль обновлен";
    }

    @GetMapping("/myProfile")
    public ResponseEntity<UserProfileEntity> getProfile(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        UserProfileEntity profile = userProfileService.findByUser(user);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profile);
    }


}
