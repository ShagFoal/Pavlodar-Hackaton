package blog.auth.auth.controllers;

import blog.auth.auth.UserProfile.UserProfileEntity;
import blog.auth.auth.UserProfile.UserProfileService;
import blog.auth.auth.dto.ProfessionVacancies;
import blog.auth.auth.services.CareerService;
import blog.auth.auth.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final UserProfileService userProfileService;
    private final CareerService careerService;

    @GetMapping
    public ResponseEntity<?> getRecommendation(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        UserProfileEntity userProfile = userProfileService.findByUser(user);

        List<ProfessionVacancies> recommendations = careerService.fetchData(
                userProfile.getSkills(),
                userProfile.getInterests(),
                userProfile.getEducationLevel(),
                userProfile.getPreferredRegion()
        );

        return ResponseEntity.ok(recommendations);
    }

}
