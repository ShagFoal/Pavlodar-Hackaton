package blog.auth.auth.UserProfile;

import blog.auth.auth.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ElementCollection
    private List<String> skills;
    @ElementCollection
    private List<String> interests;
    private String educationLevel;
    private String preferredRegion;
    private String surname; // Фамилия
    private String lastname; // Отечество
    private String studyPlace; // место обучение
    private String profession;
    private String phoneNumber; // номер телефона
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private UserEntity user;
}
