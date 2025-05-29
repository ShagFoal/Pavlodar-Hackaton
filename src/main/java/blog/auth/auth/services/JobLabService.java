package blog.auth.auth.services;

import blog.auth.auth.dto.Vacancy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobLabService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String JOBLAB_API_URL = "https://joblab.kz/vacancy?r=vac&";

    public List<Vacancy> fetchVacancies(String keyword, String region, int pages) {
        List<Vacancy> allVacancies = new ArrayList<>();

        for (int page = 0; page < pages; page++) {
            String url = JOBLAB_API_URL + "srprofecy==" + keyword + "&srcity=" + "all" + "&page=" + page;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");

            if (items == null) continue;

            for (Map<String, Object> item : items) {
                allVacancies.add(parseVacancy(item));
            }
        }
        return allVacancies;
    }

    private Vacancy parseVacancy(Map<String, Object> data) {
        Map<String, Object> salary = (Map<String, Object>) data.get("salary");
        Double avgSalary = null;

        if (salary != null && salary.get("from") != null && salary.get("to") != null) {
            avgSalary = ((Number) salary.get("from")).doubleValue() * 0.5 +
                    ((Number) salary.get("to")).doubleValue() * 0.5;
        }

        return new Vacancy((String) data.get("name"), avgSalary);
    }
}
