package blog.auth.auth.services;

import blog.auth.auth.dto.ProfessionRecommendation;
import blog.auth.auth.dto.ProfessionVacancies;
import blog.auth.auth.dto.VacancyData;
import blog.auth.auth.dto.VacancyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CareerService {

    private final NeuroService neuroService;   // Обращение к ChatGPT
    private final VacancyService vacancyService; // Поиск вакансий

    /**
     * Получить топ-5 подходящих профессий и их вакансии
     */
    public List<ProfessionVacancies> fetchData(
            List<String> skills,
            List<String> interests,
            String education,
            String region
    ) {
        // 1. Прогоняем через нейросеть — получаем 5 профессий
        List<Mono<ProfessionRecommendation>> professions = neuroService.getTopProfessions(skills, interests, education);

        // 2. Для каждой профессии получаем вакансии
        List<ProfessionVacancies> result = new ArrayList<>();

        for (Mono<ProfessionRecommendation> prof : professions) {
            VacancyResponse vacancyResponse = vacancyService.analyzeVacancies(prof.block().getProfession(), region);

            // Объединяем данные
            ProfessionVacancies pv = new ProfessionVacancies(
                    prof.block().getProfession(),
                    prof.block().getDescription(),
                    vacancyResponse.vacancies(),
                    (vacancyResponse.averageSalary() == null) ? 0.0 : vacancyResponse.averageSalary(),
                    (vacancyResponse.medianSalary() == null) ? 0.0 : vacancyResponse.medianSalary(),
                    vacancyResponse.levels(),
                    vacancyResponse.trend(),
                    prof.block().getMatchScore()
            );
            result.add(pv);
        }

        return result;
    }
}
