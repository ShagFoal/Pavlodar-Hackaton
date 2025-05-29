package blog.auth.auth.dto;

import java.util.Map;

public record VacancyResponse(
        String profession,
        String region,
        int vacancies,
        Double averageSalary,
        Double medianSalary,
        Map<String, LevelStats> levels,
        Trend trend
) {}