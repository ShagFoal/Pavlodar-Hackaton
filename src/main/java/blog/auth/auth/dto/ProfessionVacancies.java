package blog.auth.auth.dto;

import java.util.Map;

public class ProfessionVacancies {
    private String profession;
    private String description;
    private int vacancies;
    private double averageSalary;
    private double medianSalary;
    private Map<String, LevelStats> levelStats; // junior/middle/senior и т.п.
    private Trend trend;
    private double matchScore; // 4.0-5.0

    public ProfessionVacancies(String profession, String description, int vacancies, double averageSalary, double medianSalary, Map<String, LevelStats> levelStats, Trend trend, double matchScore) {
        this.profession = profession;
        this.description = description;
        this.vacancies = vacancies;
        this.averageSalary = averageSalary;
        this.medianSalary = medianSalary;
        this.levelStats = levelStats;
        this.trend = trend;
        this.matchScore = matchScore;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Trend getTrend() {
        return trend;
    }

    public void setTrend(Trend trend) {
        this.trend = trend;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }
}
