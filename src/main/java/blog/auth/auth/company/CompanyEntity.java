package blog.auth.auth.company;

import blog.auth.auth.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String companyName;
    private String industry; // ниша компании по типу IT сектор и тд
    private String region; // Регион где находится компания например Алматы Астана
    private Long turnover; // Оборот компании сколько зарабатывает и тд
    private Long employees; // кол-во сотрудников
    private String IIN;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // у одной комании всего один владелец но у владельца может быть много компаний
    @JsonBackReference
    private UserEntity owner; // владелец комании
}
