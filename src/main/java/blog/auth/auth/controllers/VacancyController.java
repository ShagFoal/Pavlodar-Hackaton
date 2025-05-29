package blog.auth.auth.controllers;

import blog.auth.auth.dto.VacancyResponse;
import blog.auth.auth.services.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vacancies")
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;

    @GetMapping
    public VacancyResponse getVacancies(
            @RequestParam String keyword,
            @RequestParam String region
    ) {
        return vacancyService.analyzeVacancies(keyword, region);
    }
}

