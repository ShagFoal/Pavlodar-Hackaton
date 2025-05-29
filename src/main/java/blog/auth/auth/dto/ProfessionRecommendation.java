package blog.auth.auth.dto;

public class ProfessionRecommendation {
    private String profession;
    private String description;
    private double matchScore; // от 4.0 до 5.0

    public ProfessionRecommendation(String profession, String description, double matchScore) {
        this.profession = profession;
        this.description = description;
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

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }
}
