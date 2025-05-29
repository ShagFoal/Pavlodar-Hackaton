package blog.auth.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "trend_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;
    private String region;
    private LocalDate period; // должен быть понедельником
    private int vacancies;
}
