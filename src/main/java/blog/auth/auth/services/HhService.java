package blog.auth.auth.services;

import blog.auth.auth.dto.Vacancy;
import blog.auth.auth.utils.HhMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class HhService {

    // RestTemplate используется для выполнения HTTP-запросов к внешнему API hh.kz
    private final RestTemplate restTemplate = new RestTemplate();

    // Сервис сопоставления названий городов с их ID в API hh.kz
    private final HhMapper hhMapper;

    // Базовый URL API HeadHunter Kazakhstan
    private final String HH_API_URL = "https://api.hh.kz/vacancies";

    /**
     * Получает список вакансий с сайта hh.kz по заданному ключевому слову и региону.
     *
     * @param keyword    ключевое слово (например, "Java", "аналитик")
     * @param regionName название региона (например, "Алматы")
     * @param pages      количество страниц для парсинга (по умолчанию обычно около 5)
     * @return список вакансий, нормализованных под локальный формат
     */
    public List<Vacancy> fetchVacancies(String keyword, String regionName, int pages) {
        // Получаем ID региона из пользовательского ввода
        Optional<String> areaIdOpt = hhMapper.resolveCity(regionName);
        if (areaIdOpt.isEmpty()) {
            throw new IllegalArgumentException("Регион не найден: " + regionName);
        }

        String areaId = areaIdOpt.get();
        List<Vacancy> allVacancies = new ArrayList<>();

        // Обходим страницы пагинации hh.kz
        for (int page = 0; page < pages; page++) {
            String url = HH_API_URL + "?text=" + keyword + "&area=" + areaId + "&page=" + page;

            // Выполняем GET-запрос к API
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            // Получаем список вакансий (ключ "items" в ответе API)
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");

            if (items == null) continue;

            // Преобразуем каждую вакансию в локальную DTO-модель
            for (Map<String, Object> item : items) {
                allVacancies.add(parseVacancy(item));
            }
        }
        return allVacancies;
    }

    /**
     * Преобразует данные одной вакансии из формата API в объект Vacancy.
     *
     * @param data сырые данные вакансии из ответа API
     * @return нормализованный объект Vacancy
     */
    private Vacancy parseVacancy(Map<String, Object> data) {
        Map<String, Object> salary = (Map<String, Object>) data.get("salary");
        Double avgSalary = null;

        // Если зарплата задана диапазоном, считаем среднее значение
        if (salary != null && salary.get("from") != null && salary.get("to") != null) {
            avgSalary = ((Number) salary.get("from")).doubleValue() * 0.5 +
                    ((Number) salary.get("to")).doubleValue() * 0.5;
        }

        // Возвращаем только название и среднюю зарплату
        return new Vacancy((String) data.get("name"), avgSalary);
    }
}
