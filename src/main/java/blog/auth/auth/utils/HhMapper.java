package blog.auth.auth.utils;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class HhMapper {

    // URL API HeadHunter, возвращающий список всех стран, регионов и городов
    private static final String AREAS_API_URL = "https://api.hh.ru/areas";

    // Кэш для быстрого поиска ID по имени города или региона (в нижнем регистре)
    private final Map<String, String> cityToId = new HashMap<>();

    // Флаг готовности: true, если init() успешно отработал и кэш сформирован
    @Getter
    private boolean ready = false;

    /**
     * Метод инициализации, выполняемый после создания компонента Spring.
     * Загружает и обрабатывает список регионов и городов Казахстана.
     */
    @PostConstruct
    public void init() {
        RestTemplate restTemplate = new RestTemplate();

        // Получаем список всех стран и их регионов из hh.ru
        List<Map<String, Object>> countries = restTemplate.getForObject(AREAS_API_URL, List.class);
        if (countries == null) return;

        for (Map<String, Object> country : countries) {
            // Нас интересует только Казахстан
            if (!"Казахстан".equals(country.get("name"))) continue;

            List<Map<String, Object>> regions = (List<Map<String, Object>>) country.get("areas");

            for (Map<String, Object> region : regions) {
                String regionId = (String) region.get("id");
                String regionName = ((String) region.get("name")).toLowerCase();

                // Сохраняем название региона (например, "алматы") и его ID
                cityToId.put(regionName, regionId);

                // Проходим по вложенным городам региона
                List<Map<String, Object>> cities = (List<Map<String, Object>>) region.get("areas");
                for (Map<String, Object> city : cities) {
                    String cityId = (String) city.get("id");
                    String cityName = ((String) city.get("name")).toLowerCase();

                    // Сохраняем название города и его ID
                    cityToId.put(cityName, cityId);
                }
            }
        }

        // Устанавливаем флаг, что данные загружены
        ready = true;
    }

    /**
     * Разрешает ID по названию города или региона.
     *
     * @param cityName название региона/города (регистр не важен)
     * @return Optional c ID, если найдено, иначе Optional.empty()
     */
    public Optional<String> resolveCity(String cityName) {
        if (!ready) return Optional.empty();
        return Optional.ofNullable(cityToId.get(cityName.toLowerCase()));
    }

    /**
     * Возвращает все доступные названия городов/регионов (для отладки или подсказок).
     *
     * @return множество всех ключей cityToId (в нижнем регистре)
     */
    public Set<String> availableCities() {
        return cityToId.keySet();
    }
}
