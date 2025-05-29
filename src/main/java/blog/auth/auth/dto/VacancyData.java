package blog.auth.auth.dto;

import java.util.Map;

public class VacancyData {
    private String profession;
    private int vacancies;
    private double averageSalary;
    private double medianSalary;
    private Map<String, LevelStats> levelStats;
    private Map<String, Object> trend;

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getVacancies() {
        return vacancies;
    }

    public void setVacancies(int vacancies) {
        this.vacancies = vacancies;
    }

    public double getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(double averageSalary) {
        this.averageSalary = averageSalary;
    }

    public double getMedianSalary() {
        return medianSalary;
    }

    public void setMedianSalary(double medianSalary) {
        this.medianSalary = medianSalary;
    }

    public Map<String, LevelStats> getLevelStats() {
        return levelStats;
    }

    public void setLevelStats(Map<String, LevelStats> levelStats) {
        this.levelStats = levelStats;
    }

    public Map<String, Object> getTrend() {
        return trend;
    }

    public void setTrend(Map<String, Object> trend) {
        this.trend = trend;
    }
}
