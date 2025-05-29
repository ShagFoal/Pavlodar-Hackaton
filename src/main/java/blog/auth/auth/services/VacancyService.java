package blog.auth.auth.services;

import blog.auth.auth.dto.*;
import blog.auth.auth.entity.TrendEntity;
import blog.auth.auth.repository.TrendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VacancyService {

    private final TrendRepository trendRepository;
    private final HhService hhService;
    private final JobLabService jobLabService; // Пока не используется, но предусмотрен для будущей интеграции

    /**
     * Основной метод анализа вакансий:
     * - Получает вакансии с разных платформ (пока только hh.kz)
     * - Обновляет информацию о трендах
     * - Формирует итоговый ответ со статистикой
     */
    public VacancyResponse analyzeVacancies(String keyword, String region) {
        int pages = 5; // Кол-во страниц, которые будем запрашивать у API

        // Собираем вакансии с всех источников
        List<Vacancy> vacancies = new ArrayList<>();
        vacancies.addAll(hhService.fetchVacancies(keyword, region, pages));
        //vacancies.addAll(jobLabService.fetchVacancies(keyword, region, pages)); // можно подключить при необходимости

        // Обновляем тренд (в БД будет актуальная дата - понедельник текущей недели)
        updateTrend(keyword, region, vacancies.size());

        // Формируем и возвращаем ответ
        return buildResponse(keyword, region, vacancies);
    }

    /**
     * Обновляет или создаёт новую запись о количестве вакансий за текущую неделю
     */
    private void updateTrend(String keyword, String region, int vacancyCount) {
        // Определяем понедельник текущей недели
        LocalDate monday = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // Пытаемся найти уже существующую запись
        Optional<TrendEntity> existing = trendRepository.findByKeywordAndRegionAndPeriod(keyword, region, monday);

        if (existing.isPresent()) {
            // Если есть — обновляем количество вакансий
            TrendEntity trend = existing.get();
            trend.setVacancies(vacancyCount);
            trendRepository.save(trend);
        } else {
            // Если нет — создаём новую запись
            TrendEntity newTrend = new TrendEntity(null, keyword, region, monday, vacancyCount);
            trendRepository.save(newTrend);
        }
    }

    /**
     * Формирует объект ответа со статистикой по вакансиям:
     * - Средняя и медианная зарплата
     * - Разбивка по уровням (intern, junior, и т.д.)
     * - Динамика (тренд) по неделям
     */
    private VacancyResponse buildResponse(String keyword, String region, List<Vacancy> vacancies) {
        // Группируем вакансии по уровням на основе ключевых слов
        Map<String, List<Vacancy>> levels = Map.of(
                "intern", filterByLevel(vacancies, List.of("intern", "стажер")),
                "junior", filterByLevel(vacancies, List.of("junior", "младший")),
                "middle", filterByLevel(vacancies, List.of("middle", "средний")),
                "senior", filterByLevel(vacancies, List.of("senior", "старший")),
                "lead", filterByLevel(vacancies, List.of("lead", "ведущий"))
        );

        // Формируем статистику по каждому уровню
        Map<String, LevelStats> levelStats = new LinkedHashMap<>();
        for (var entry : levels.entrySet()) {
            List<Double> salaries = entry.getValue().stream()
                    .map(Vacancy::salary)
                    .filter(Objects::nonNull)
                    .toList();

            levelStats.put(entry.getKey(), new LevelStats(
                    entry.getValue().size(), // кол-во вакансий
                    salaries.isEmpty() ? null : average(salaries) // средняя зарплата
            ));
        }

        // Общая зарплатная статистика (по всем вакансиям)
        List<Double> allSalaries = vacancies.stream()
                .map(Vacancy::salary)
                .filter(Objects::nonNull)
                .toList();

        // Возвращаем финальный ответ
        return new VacancyResponse(
                keyword,
                region,
                vacancies.size(),
                allSalaries.isEmpty() ? null : average(allSalaries),
                allSalaries.isEmpty() ? null : median(allSalaries),
                levelStats,
                loadTrendData(keyword, region)
        );
    }

    /**
     * Фильтрует список вакансий по вхождению ключевых слов в название
     */
    private List<Vacancy> filterByLevel(List<Vacancy> list, List<String> keywords) {
        return list.stream()
                .filter(v -> keywords.stream().anyMatch(k -> v.name().toLowerCase().contains(k)))
                .toList();
    }

    /**
     * Вычисляет среднее значение
     */
    private double average(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    /**
     * Вычисляет медиану значений
     */
    private double median(List<Double> values) {
        List<Double> sorted = new ArrayList<>(values);
        sorted.sort(Comparator.naturalOrder());
        int middle = sorted.size() / 2;
        if (sorted.size() % 2 == 0) {
            return (sorted.get(middle - 1) + sorted.get(middle)) / 2;
        }
        return sorted.get(middle);
    }

    /**
     * Загружает историю трендов из БД (по неделям)
     */
    private Trend loadTrendData(String keyword, String region) {
        List<TrendEntity> entities = trendRepository.findAllByKeywordAndRegionOrderByPeriodAsc(keyword, region);

        List<TrendPoint> trendPoints = entities.stream()
                .map(e -> new TrendPoint(e.getPeriod().toString(), e.getVacancies()))
                .toList();

        return new Trend("week", trendPoints); // "week" — единица измерения тренда
    }
}
